package com.hpe.data;

import java.util.List;

/**
 * Wrapper for the required metrics that we need to collect.
 */
public class Metrics {
    private int rowsWithMissingFieldsCounter;
    private int messagesWithBlankContentCounter;
    private int rowsWithFieldsErrors;
    private int originCallsCounterByCountryCode;
    private int destinationCallsCounterByCountryCode;
    private int okCallsCounter;
    private int koCallsCounter;
    private float averageCallDurationByCountryCode;
    private List<WordRanking> wordsRanking;
}
