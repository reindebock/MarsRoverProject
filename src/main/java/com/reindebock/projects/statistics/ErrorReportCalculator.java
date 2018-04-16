package com.reindebock.projects.statistics;

import java.util.Collection;

import com.reindebock.projects.domain.Photo;

public class ErrorReportCalculator extends PhotoStatisticsCalculator {
    @Override
    public void calculate(Collection<? extends Photo> collection) {
        long errorCount = collection.stream().filter(photo -> !photo.isSuccessful()).count();

        if (errorCount == 0) {
            return;
        }

        LOGGER.error("\n");
        LOGGER.error("*** ERROR REPORT -- {} download attempts failed ***", errorCount);
        collection.stream().filter(photo -> !photo.isSuccessful())
                .forEach(photo -> LOGGER.error("HTTP Status Code/Java Exception: {}   Message: {}   NASA URI: {}", photo.getRetrieveStatusCode(), photo.getRetrieveStatusMessage(), photo.getNasaPhoto().getImgSrc()));

        LOGGER.error("\n");
    }
}
