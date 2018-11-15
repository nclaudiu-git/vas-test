package com.hpe.services;

import com.hpe.data.Kpis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The service builds a KPIs wrapper out of the cached counters.
 */
@Service
public class KpisService {

    @Autowired
    private CacheService cacheService;

    public Kpis getKpis() {
        return Kpis.Builder.newInstance()
                .withProcessedJsonFilesCounter(cacheService.getProcessedJsonFilesCounter())
                .withTotalRowsCounter(cacheService.getTotalRowsCounter())
                .withTotalCallsCounter(cacheService.getTotalCallsCounter())
                .withTotalMessagesCounter(cacheService.getTotalMessagesCounter())
                .withTotalDistinctOriginCountryCodes(cacheService.getTotalDistinctOriginCountryCodesCounter())
                .withTotalDistinctDestinationCountryCodes(cacheService.getTotalDistinctDestinationCountryCodesCounter())
                .withJsonProcessDurationInMillis(cacheService.getProcessingTimeInMillisByJsonFileCounter())
                .build();
    }
}
