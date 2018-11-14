package com.hpe.data;

/**
 * Wrapper for the Kpis that we need to evaluate.
 */
public class Kpis {
    private int processedJsonFilesCounter;
    private int totalRowsCounter;
    private int totalCallsCounter;
    private int totalMessagesCounter;
    private int totalDistinctOriginCountryCodes;
    private int totalDistinctDestinationCountryCodes;
    private int jsonProcessDurationInMillis;

    public int getProcessedJsonFilesCounter() {
        return processedJsonFilesCounter;
    }

    public void setProcessedJsonFilesCounter(int processedJsonFilesCounter) {
        this.processedJsonFilesCounter = processedJsonFilesCounter;
    }

    public int getTotalRowsCounter() {
        return totalRowsCounter;
    }

    public void setTotalRowsCounter(int totalRowsCounter) {
        this.totalRowsCounter = totalRowsCounter;
    }

    public int getTotalCallsCounter() {
        return totalCallsCounter;
    }

    public void setTotalCallsCounter(int totalCallsCounter) {
        this.totalCallsCounter = totalCallsCounter;
    }

    public int getTotalMessagesCounter() {
        return totalMessagesCounter;
    }

    public void setTotalMessagesCounter(int totalMessagesCounter) {
        this.totalMessagesCounter = totalMessagesCounter;
    }

    public int getTotalDistinctOriginCountryCodes() {
        return totalDistinctOriginCountryCodes;
    }

    public void setTotalDistinctOriginCountryCodes(int totalDistinctOriginCountryCodes) {
        this.totalDistinctOriginCountryCodes = totalDistinctOriginCountryCodes;
    }

    public int getTotalDistinctDestinationCountryCodes() {
        return totalDistinctDestinationCountryCodes;
    }

    public void setTotalDistinctDestinationCountryCodes(int totalDistinctDestinationCountryCodes) {
        this.totalDistinctDestinationCountryCodes = totalDistinctDestinationCountryCodes;
    }

    public int getJsonProcessDurationInMillis() {
        return jsonProcessDurationInMillis;
    }

    public void setJsonProcessDurationInMillis(int jsonProcessDurationInMillis) {
        this.jsonProcessDurationInMillis = jsonProcessDurationInMillis;
    }
}
