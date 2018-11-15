package com.hpe.data;

import java.util.*;

/**
 * Counters used per JSON file.
 */
public class Counters {
    private int rowsWithMissingFieldsCounter;
    private int messagesWithBlankContentCounter;
    private int rowsWithFieldsErrorsCounter;
    private Map<Long, Integer> originCallsByCountryCode = new HashMap<>();
    private Map<Long, Integer> destinationCallsByCountryCode = new HashMap<>();
    private int okCallsCounter;
    private int koCallsCounter;
    private Map<Long, Float> averageCallsDurationByCountryCode = new HashMap<>();
    private List<String> wordsOccurrenceRanking = new ArrayList<>();
    private int rowsCounter;
    private int callsCounter;
    private int messagesCounter;
    private Set<Long> distinctOriginCountryCodes = new HashSet<>();
    private Set<Long> distinctDestinationCountryCodes = new HashSet<>();
    private long startTimestamp;
    private long endTimestamp;

    public int getRowsWithMissingFieldsCounter() {
        return rowsWithMissingFieldsCounter;
    }

    public void setRowsWithMissingFieldsCounter(int rowsWithMissingFieldsCounter) {
        this.rowsWithMissingFieldsCounter = rowsWithMissingFieldsCounter;
    }

    public int getMessagesWithBlankContentCounter() {
        return messagesWithBlankContentCounter;
    }

    public void setMessagesWithBlankContentCounter(int messagesWithBlankContentCounter) {
        this.messagesWithBlankContentCounter = messagesWithBlankContentCounter;
    }

    public int getRowsWithFieldsErrorsCounter() {
        return rowsWithFieldsErrorsCounter;
    }

    public void setRowsWithFieldsErrorsCounter(int rowsWithFieldsErrorsCounter) {
        this.rowsWithFieldsErrorsCounter = rowsWithFieldsErrorsCounter;
    }

    public Map<Long, Integer> getOriginCallsByCountryCode() {
        return originCallsByCountryCode;
    }

    public void setOriginCallByCountryCode(long countryCode, int count) {
        this.originCallsByCountryCode.put(countryCode, count);
    }

    public Map<Long, Integer> getDestinationCallsByCountryCode() {
        return destinationCallsByCountryCode;
    }

    public void setDestinationCallByCountryCode(long countryCode, int count) {
        this.destinationCallsByCountryCode.put(countryCode, count);
    }

    public int getOkCallsCounter() {
        return okCallsCounter;
    }

    public void setOkCallsCounter(int okCallsCounter) {
        this.okCallsCounter = okCallsCounter;
    }

    public int getKoCallsCounter() {
        return koCallsCounter;
    }

    public void setKoCallsCounter(int koCallsCounter) {
        this.koCallsCounter = koCallsCounter;
    }

    public Map<Long, Float> getAverageCallsDurationByCountryCode() {
        return averageCallsDurationByCountryCode;
    }

    public void addAverageCallDurationByCountryCode(long countryCode, float duration) {
        this.averageCallsDurationByCountryCode.put(countryCode, duration);
    }

    public List<String> getWordsOccurrenceRanking() {
        return wordsOccurrenceRanking;
    }

    public void setWordsOccurrenceRanking(List<String> wordsOccurrenceRanking) {
        this.wordsOccurrenceRanking = wordsOccurrenceRanking;
    }

    public int getRowsCounter() {
        return rowsCounter;
    }

    public void setRowsCounter(int rowsCounter) {
        this.rowsCounter = rowsCounter;
    }

    public int getCallsCounter() {
        return callsCounter;
    }

    public void setCallsCounter(int callsCounter) {
        this.callsCounter = callsCounter;
    }

    public int getMessagesCounter() {
        return messagesCounter;
    }

    public void setMessagesCounter(int messagesCounter) {
        this.messagesCounter = messagesCounter;
    }

    public Set<Long> getDistinctOriginCountryCodes() {
        return distinctOriginCountryCodes;
    }

    public void addDistinctOriginCountryCode(long origin) {
        this.distinctOriginCountryCodes.add(origin);
    }

    public Set<Long> getDistinctDestinationCountryCodes() {
        return distinctDestinationCountryCodes;
    }

    public void addDistinctDestinationCountryCode(long destination) {
        this.distinctDestinationCountryCodes.add(destination);
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public long getProcessingTimeInMillis() {
        return endTimestamp - startTimestamp;
    }
}
