package com.hpe.data;

import java.util.List;
import java.util.Map;

/**
 * Wrapper for the required metrics that we need to collect.
 */
public class Metrics {
    private int rowsWithMissingFieldsCounter;
    private int messagesWithBlankContentCounter;
    private int rowsWithFieldsErrors;
    private Map<Long, Integer> originCallsCounterByCountryCode;
    private Map<Long, Integer> destinationCallsCounterByCountryCode;
    private int okCallsCounter;
    private int koCallsCounter;
    private Map<Long, Float> averageCallDurationByCountryCode;
    private List<String> wordsRanking;

    private Metrics() {
    }

    public int getRowsWithMissingFieldsCounter() {
        return rowsWithMissingFieldsCounter;
    }

    public int getMessagesWithBlankContentCounter() {
        return messagesWithBlankContentCounter;
    }

    public int getRowsWithFieldsErrors() {
        return rowsWithFieldsErrors;
    }

    public Map<Long, Integer> getOriginCallsCounterByCountryCode() {
        return originCallsCounterByCountryCode;
    }

    public Map<Long, Integer> getDestinationCallsCounterByCountryCode() {
        return destinationCallsCounterByCountryCode;
    }

    public int getOkCallsCounter() {
        return okCallsCounter;
    }

    public int getKoCallsCounter() {
        return koCallsCounter;
    }

    public Map<Long, Float> getAverageCallDurationByCountryCode() {
        return averageCallDurationByCountryCode;
    }

    public List<String> getWordsRanking() {
        return wordsRanking;
    }

    public static class Builder {
        private Metrics metrics = new Metrics();

        private Builder() {
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder withRowsWithMissingFieldsCounter(int counter) {
            metrics.rowsWithMissingFieldsCounter = counter;
            return this;
        }

        public Builder withMessagesWithBlankContentCounter(int counter) {
            metrics.messagesWithBlankContentCounter = counter;
            return this;

        }

        public Builder withRowsWithFieldsErrors(int counter) {
            metrics.rowsWithFieldsErrors = counter;
            return this;
        }

        public Builder withOriginCallsCounterByCountryCode(Map<Long, Integer> callsByCountryCode) {
            metrics.originCallsCounterByCountryCode = callsByCountryCode;
            return this;
        }

        public Builder withDestinationCallsCounterByCountryCode(Map<Long, Integer> callsByCountryCode) {
            metrics.destinationCallsCounterByCountryCode = callsByCountryCode;
            return this;
        }

        public Builder withOkCallsCounter(int counter) {
            metrics.okCallsCounter = counter;
            return this;

        }

        public Builder withKoCallsCounter(int counter) {
            metrics.koCallsCounter = counter;
            return this;
        }

        public Builder withAverageCallDurationByCountryCode(Map<Long, Float> callDurationByCountryCode) {
            metrics.averageCallDurationByCountryCode = callDurationByCountryCode;
            return this;
        }

        public Builder withWordsOccurrenceRanking(List<String> wordRankings) {
            metrics.wordsRanking = wordRankings;
            return this;
        }

        public Metrics build() {
            return metrics;
        }
    }
}
