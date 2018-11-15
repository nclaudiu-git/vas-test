package com.hpe.services;

import com.hpe.data.Metrics;
import com.hpe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The service builds a metrics wrapper out of the cached counters.
 */
@Service
public class MetricsService {

    @Autowired
    private CacheService cacheService;

    public Metrics getLatestMetrics() {
        Result<String> fileResult = cacheService.getLatestJsonFile();
        if (fileResult.isError()) {
            return null;
        }

        String file = fileResult.getValue();
        return Metrics.Builder.newInstance()
                .withRowsWithMissingFieldsCounter(cacheService.getRowsWithMissingFieldsCounter(file))
                .withMessagesWithBlankContentCounter(cacheService.getMessagesWithBlankContentCounter(file))
                .withRowsWithFieldsErrors(cacheService.getRowsWithFieldsErrorsCounter(file))
                .withOriginCallsCounterByCountryCode(cacheService.getOriginCallsByCountryCode(file))
                .withDestinationCallsCounterByCountryCode(cacheService.getDestinationCallsByCountryCode(file))
                .withOkCallsCounter(cacheService.getOkCallsCounter(file))
                .withKoCallsCounter(cacheService.getKoCallsCounter(file))
                .withAverageCallDurationByCountryCode(cacheService.getAverageCallDurationByCountryCode(file))
                .withWordsOccurrenceRanking(cacheService.getWordsOccurrenceRanking(file))
                .build();
    }
}
