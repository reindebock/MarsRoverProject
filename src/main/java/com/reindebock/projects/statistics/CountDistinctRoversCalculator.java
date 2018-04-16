package com.reindebock.projects.statistics;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reindebock.projects.domain.Photo;
import com.reindebock.projects.domain.Rover;

public class CountDistinctRoversCalculator extends PhotoStatisticsCalculator {

    @Override
    public void calculate(Collection<? extends Photo> collection) {
        long roverCount = collection.stream()
                .map(photo -> photo.getNasaPhoto().getRover())
                .distinct()
                .count();

        LOGGER.info("Total rovers in results: {}", roverCount);
    }
}
