package com.reindebock.projects.statistics;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reindebock.projects.domain.Photo;

public class CountDistinctCamerasCalculator extends PhotoStatisticsCalculator {
    @Override
    public void calculate(Collection<? extends Photo> photos) {
        long cameraCount = photos.stream()
                .map(photo -> photo.getNasaPhoto().getCamera())
                .distinct()
                .count();

        LOGGER.info("Total cameras in results: {}", cameraCount);
    }
}
