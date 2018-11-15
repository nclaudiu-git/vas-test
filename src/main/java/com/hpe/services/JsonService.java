package com.hpe.services;

import com.hpe.data.*;
import com.hpe.parser.Parser;
import com.hpe.tokenizer.Tokenizer;
import com.hpe.utils.MsisdnUtils;
import com.hpe.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The service takes a stream of json raw strings, evaluates each string and builds a
 * list of structured key-value pairs. Based on these pairs, we'll process the list of counters
 * that will be used later on for metrics and KPIs.
 *
 * <p>
 * <b>Important:</b> note that all counters for CALL/MSG are updated only for fully valid JSONs (meaning all fields per each message type are present
 * and their values are valid - except of the format of origin and destination which is not part of this validation). As a result, rows that have a proper
 * message type say 'CALL' with all of the fields present but for example have an invalid status code, are marked as invalid and no other counter except
 * for the row fields error one is computed. Since the requirement aren't clear on how to architect these decisions, this is by choice of design.
 */
@Service
public class JsonService {
    private static final Logger logger = LoggerFactory.getLogger(JsonService.class);

    @Autowired
    private CacheService cacheService;

    public void process(String file, Stream<String> stream) {
        if (!markJsonToProcess(file)) {
            return;
        }

        cacheService.setStartTimestamp(file, System.currentTimeMillis());

        Map<Long, List<Long>> durationByCountryCode = new HashMap<>();
        StringBuffer messageContentBuffer = new StringBuffer();

        stream.forEach(line -> processLine(file, line, durationByCountryCode, messageContentBuffer));
        computeAverageCallsDurationByCountryCode(file, durationByCountryCode);
        computeWordRankings(file, messageContentBuffer.toString());

        cacheService.setEndTimestamp(file, System.currentTimeMillis());
    }

    private synchronized boolean markJsonToProcess(String file) {
        if (cacheService.hasJsonFile(file)) {
            return false;
        }

        cacheService.addJsonFile(file);
        return true;
    }

    private void processLine(String file, String line, Map<Long, List<Long>> durationByCountryCode, StringBuffer messageContentBuffer) {
        cacheService.incrementRowsCounter(file);

        Result<List<Token>> result = Tokenizer.run(line);
        if (result.isError()) {
            // the list tokenization failed; nothing left to do
            return;
        }

        List<KeyValue> keyValues = Parser.run(result.getValue());
        if (!validate(file, keyValues)) {
            return;
        }

        // below this point all mandatory fields are present and have a proper value (except origin and destination which need to be parsed further)
        MessageType messageType = (MessageType) getByField(keyValues, Field.MESSAGE_TYPE).get().getResult().getValue();
        if (messageType == MessageType.CALL) {
            cacheService.incrementCallsCounter(file);

            KeyValue originResult = getByField(keyValues, Field.ORIGIN).get();
            KeyValue destinationResult = getByField(keyValues, Field.DESTINATION).get();
            Result<Long> originCountryCodeResult = getCountryCodeIfValid(originResult);
            Result<Long> destinationCountryCodeResult = getCountryCodeIfValid(destinationResult);

            if (!originCountryCodeResult.isError()) {
                long countryCode = originCountryCodeResult.getValue();
                cacheService.addOriginCountryCode(file, countryCode);
                cacheService.incrementOriginCallByCountryCode(file, countryCode);

                durationByCountryCode.putIfAbsent(countryCode, new ArrayList<>());
                List<Long> durations = durationByCountryCode.get(countryCode);
                durations.add((long) getByField(keyValues, Field.DURATION).get().getResult().getValue());
            }
            if (!destinationCountryCodeResult.isError()) {
                long countryCode = destinationCountryCodeResult.getValue();
                cacheService.addDestinationCountryCode(file, countryCode);
                cacheService.incrementDestinationCallByCountryCode(file, countryCode);
            }

            StatusCode statusCode = (StatusCode) getByField(keyValues, Field.STATUS_CODE).get().getResult().getValue();
            if (statusCode == StatusCode.OK) {
                cacheService.incrementOkCallsCounter(file);
            } else if (statusCode == StatusCode.KO) {
                cacheService.incrementKoCallsCounter(file);
            }

        } else if (messageType == MessageType.MSG) {
            cacheService.incrementMessagesCounter(file);

            String messageContent = (String) getByField(keyValues, Field.MESSAGE_CONTENT).get().getResult().getValue();
            if ("".equals(messageContent)) {
                cacheService.incrementMessagesWithBlankContentCounter(file);
            } else {
                messageContentBuffer.append(" ").append(messageContent);
            }
        }
    }

    private boolean validate(String file, List<KeyValue> keyValues) {
        Optional<KeyValue> optionalMessageType = getByField(keyValues, Field.MESSAGE_TYPE);
        if (!optionalMessageType.isPresent()) {
            cacheService.incrementRowsWithMissingFieldsCounter(file);
            return false;
        }

        KeyValue messageTypeKeyValue = optionalMessageType.get();
        if (messageTypeKeyValue.getResult().isError() ||
                !Arrays.asList(MessageType.CALL, MessageType.MSG).contains(messageTypeKeyValue.getResult().getValue())) {
            cacheService.incrementRowsWithFieldsErrorsCounter(file);
            return false;
        }

        MessageType messageType = (MessageType) messageTypeKeyValue.getResult().getValue();
        Set<Field> parsedFields = getKeyValueFields(keyValues);
        if (!Field.ofMessageType(messageType).equals(parsedFields)) {
            cacheService.incrementRowsWithMissingFieldsCounter(file);
            return false;
        }

        if (!validateAllValuesArePresent(keyValues)) {
            cacheService.incrementRowsWithFieldsErrorsCounter(file);
            return false;
        }

        return true;
    }

    private Set<Field> getKeyValueFields(List<KeyValue> keyValues) {
        return keyValues.stream()
                .map(KeyValue::getField)
                .collect(Collectors.toSet());
    }

    private boolean validateAllValuesArePresent(List<KeyValue> keyValues) {
        return keyValues.stream()
                .noneMatch(keyValue -> keyValue.getResult().isError());
    }

    private Optional<KeyValue> getByField(List<KeyValue> keyValues, Field field) {
        return keyValues.stream()
                .filter(keyValue -> keyValue.getField() == field)
                .findFirst();
    }

    private Result<Long> getCountryCodeIfValid(KeyValue keyValue) {
        long possibleMsisdn = (long) keyValue.getResult().getValue();
        if (MsisdnUtils.isValid(possibleMsisdn)) {
            return Result.ok(MsisdnUtils.getCountryCode(possibleMsisdn));
        }
        return Result.error("Country code missing or invalid.");
    }

    private void computeAverageCallsDurationByCountryCode(String file, Map<Long, List<Long>> durationsByCountryCode) {
        durationsByCountryCode.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<Long> durations = entry.getValue();
                            return durations.size() == 0 ? 0 : entry.getValue().stream().mapToLong(Long::longValue).sum() / durations.size();
                        }
                ))
                .forEach((countryCode, averageDuration) -> cacheService.addAverageCallDurationByCountryCode(file, countryCode, averageDuration));
    }

    private void computeWordRankings(String file, String messageContent) {
        List<WordOccurrence> wordsOccurrences = WordOccurrence.Parser.run(messageContent);
        wordsOccurrences.sort(Comparator.comparing(WordOccurrence::getOccurrences).reversed());

        cacheService.setWordsOccurrenceRanking(file, wordsOccurrences.stream().map(WordOccurrence::getWord).collect(Collectors.toList()));
    }
}
