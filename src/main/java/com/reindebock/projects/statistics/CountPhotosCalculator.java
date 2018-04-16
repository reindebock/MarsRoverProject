package com.reindebock.projects.statistics;

import java.util.Collection;

import com.reindebock.projects.domain.Photo;

public class CountPhotosCalculator extends PhotoStatisticsCalculator {
    @Override
    public void calculate(Collection<? extends Photo> collection) {
        long successfulPhotoCount = collection.stream()
                                        .filter(photo -> photo.isSuccessful())
                                        .count();

        long errorCount = collection.size() - successfulPhotoCount;

        LOGGER.info("Total download attempts: {}   successfully retrieved: {}   errors: {}", collection.size(), successfulPhotoCount, errorCount);
    }
}
