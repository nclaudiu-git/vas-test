package com.hpe.services;

import com.hpe.Cache;
import com.hpe.data.Counters;
import com.hpe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The service is a helper for working with the cache database.
 */
@Service
public class CacheService {

    @Autowired
    private Cache cache;

    public boolean hasJsonFile(String file) {
        return cache.containsKey(file);
    }

    /**
     * Returns the latest JSON processed file based on the end timestamp.
     */
    public Result<String> getLatestJsonFile() {
        List<Map.Entry<String, Counters>> list = new ArrayList<>(cache.entrySet());
        Collections.sort(list, (a, b) -> (int) (b.getValue().getEndTimestamp() - a.getValue().getEndTimestamp()));
        return list.size() > 0 ? Result.ok(list.get(0).getKey()) : Result.error("No json processed so far");
    }

    private Counters getCounters(String file) {
        return cache.get(file);
    }

    public void addJsonFile(String file) {
        cache.put(file, new Counters());
    }

    public void incrementRowsWithMissingFieldsCounter(String file) {
        Counters counters = getCounters(file);
        counters.setRowsWithMissingFieldsCounter(counters.getRowsWithMissingFieldsCounter() + 1);
    }

    public void incrementMessagesWithBlankContentCounter(String file) {
        Counters counters = getCounters(file);
        counters.setMessagesWithBlankContentCounter(counters.getMessagesWithBlankContentCounter() + 1);
    }

    public void incrementRowsWithFieldsErrorsCounter(String file) {
        Counters counters = getCounters(file);
        counters.setRowsWithFieldsErrorsCounter(counters.getRowsWithFieldsErrorsCounter() + 1);
    }

    public void incrementOriginCallByCountryCode(String file, long countryCode) {
        Counters counters = getCounters(file);
        Integer value = getOriginCallsByCountryCode(file).get(countryCode);
        counters.setOriginCallByCountryCode(countryCode, value == null ? 1 : value + 1);
    }

    public void incrementDestinationCallByCountryCode(String file, long countryCode) {
        Counters counters = getCounters(file);
        Integer value = getDestinationCallsByCountryCode(file).get(countryCode);
        counters.setDestinationCallByCountryCode(countryCode, value == null ? 1 : value + 1);
    }

    public void incrementOkCallsCounter(String file) {
        Counters counters = getCounters(file);
        counters.setOkCallsCounter(counters.getOkCallsCounter() + 1);
    }

    public void incrementKoCallsCounter(String file) {
        Counters counters = getCounters(file);
        counters.setKoCallsCounter(counters.getKoCallsCounter() + 1);
    }

    public void addAverageCallDurationByCountryCode(String file, long countryCode, float duration) {
        Counters counters = getCounters(file);
        counters.addAverageCallDurationByCountryCode(countryCode, duration);
    }

    public void setWordsOccurrenceRanking(String file, List<String> sortedWordLists) {
        Counters counters = getCounters(file);
        counters.setWordsOccurrenceRanking(sortedWordLists);
    }

    public void incrementRowsCounter(String file) {
        Counters counters = getCounters(file);
        counters.setRowsCounter(counters.getRowsCounter() + 1);
    }

    public void incrementCallsCounter(String file) {
        Counters counters = getCounters(file);
        counters.setCallsCounter(counters.getCallsCounter() + 1);
    }

    public void incrementMessagesCounter(String file) {
        Counters counters = getCounters(file);
        counters.setMessagesCounter(counters.getMessagesCounter() + 1);
    }

    public void addOriginCountryCode(String file, long code) {
        getCounters(file).addDistinctOriginCountryCode(code);
    }

    public void addDestinationCountryCode(String file, long code) {
        getCounters(file).addDistinctDestinationCountryCode(code);
    }

    public void setStartTimestamp(String file, long timestamp) {
        getCounters(file).setStartTimestamp(timestamp);
    }

    public void setEndTimestamp(String file, long timestamp) {
        getCounters(file).setEndTimestamp(timestamp);
    }

    public int getRowsWithMissingFieldsCounter(String file) {
        Counters counters = cache.get(file);
        return counters == null ? 0 : counters.getRowsWithMissingFieldsCounter();
    }

    public int getMessagesWithBlankContentCounter(String file) {
        Counters counters = cache.get(file);
        return counters == null ? 0 : counters.getMessagesWithBlankContentCounter();
    }

    public int getRowsWithFieldsErrorsCounter(String file) {
        Counters counters = cache.get(file);
        return counters == null ? 0 : counters.getRowsWithFieldsErrorsCounter();
    }

    public Map<Long, Integer> getOriginCallsByCountryCode(String file) {
        Counters counters = cache.get(file);
        return counters == null ? Collections.emptyMap() : counters.getOriginCallsByCountryCode();
    }

    public Map<Long, Integer> getDestinationCallsByCountryCode(String file) {
        Counters counters = cache.get(file);
        return counters == null ? Collections.emptyMap() : counters.getDestinationCallsByCountryCode();
    }

    public int getOkCallsCounter(String file) {
        Counters counters = cache.get(file);
        return counters == null ? 0 : counters.getOkCallsCounter();
    }

    public int getKoCallsCounter(String file) {
        Counters counters = cache.get(file);
        return counters == null ? 0 : counters.getKoCallsCounter();
    }

    public Map<Long, Float> getAverageCallDurationByCountryCode(String file) {
        Counters counters = cache.get(file);
        return counters == null ? Collections.emptyMap() : counters.getAverageCallsDurationByCountryCode();
    }

    public List<String> getWordsOccurrenceRanking(String file) {
        Counters counters = cache.get(file);
        return counters == null ? Collections.emptyList() : counters.getWordsOccurrenceRanking();
    }

    public int getProcessedJsonFilesCounter() {
        return cache.keySet().size();
    }

    public int getTotalRowsCounter() {
        return cache.values().stream()
                .mapToInt(Counters::getRowsCounter)
                .sum();
    }

    public int getTotalCallsCounter() {
        return cache.values().stream()
                .mapToInt(Counters::getCallsCounter)
                .sum();
    }

    public int getTotalMessagesCounter() {
        return cache.values().stream()
                .mapToInt(Counters::getMessagesCounter)
                .sum();
    }

    public long getTotalDistinctOriginCountryCodesCounter() {
        return cache.values().stream()
                .flatMap(counters -> counters.getDistinctOriginCountryCodes().stream())
                .count();
    }

    public long getTotalDistinctDestinationCountryCodesCounter() {
        return cache.values().stream()
                .flatMap(counters -> counters.getDistinctDestinationCountryCodes().stream())
                .count();
    }

    public Map<String, Long> getProcessingTimeInMillisByJsonFileCounter() {
        return cache.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue().getProcessingTimeInMillis()
                ));
    }
}
