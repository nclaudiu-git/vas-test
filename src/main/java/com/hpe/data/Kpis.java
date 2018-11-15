package com.hpe.data;

import java.util.Map;

/**
 * Wrapper for the Kpis that we need to evaluate.
 */
public class Kpis {
    private int processedJsonFilesCounter;
    private int totalRowsCounter;
    private int totalCallsCounter;
    private int totalMessagesCounter;
    private long totalDistinctOriginCountryCodes;
    private long totalDistinctDestinationCountryCodes;
    private Map<String, Long> jsonProcessDurationInMillis;

    private Kpis() {
    }

    public int getProcessedJsonFilesCounter() {
        return processedJsonFilesCounter;
    }

    public int getTotalRowsCounter() {
        return totalRowsCounter;
    }

    public int getTotalCallsCounter() {
        return totalCallsCounter;
    }

    public int getTotalMessagesCounter() {
        return totalMessagesCounter;
    }

    public long getTotalDistinctOriginCountryCodes() {
        return totalDistinctOriginCountryCodes;
    }

    public long getTotalDistinctDestinationCountryCodes() {
        return totalDistinctDestinationCountryCodes;
    }

    public Map<String, Long> getJsonProcessDurationInMillis() {
        return jsonProcessDurationInMillis;
    }

    public static class Builder {
        private Kpis kpis = new Kpis();

        private Builder() {
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder withProcessedJsonFilesCounter(int counter) {
            kpis.processedJsonFilesCounter = counter;
            return this;
        }

        public Builder withTotalRowsCounter(int counter) {
            kpis.totalRowsCounter = counter;
            return this;
        }

        public Builder withTotalCallsCounter(int counter) {
            kpis.totalCallsCounter = counter;
            return this;
        }

        public Builder withTotalMessagesCounter(int counter) {
            kpis.totalMessagesCounter = counter;
            return this;
        }

        public Builder withTotalDistinctOriginCountryCodes(long counter) {
            kpis.totalDistinctOriginCountryCodes = counter;
            return this;
        }

        public Builder withTotalDistinctDestinationCountryCodes(long counter) {
            kpis.totalDistinctDestinationCountryCodes = counter;
            return this;
        }

        public Builder withJsonProcessDurationInMillis(Map<String, Long> duration) {
            kpis.jsonProcessDurationInMillis = duration;
            return this;
        }

        public Kpis build() {
            return kpis;
        }
    }
}
