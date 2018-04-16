package com.reindebock.projects.statistics;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reindebock.projects.domain.Photo;
import com.reindebock.projects.domain.Rover;

public abstract class PhotoStatisticsCalculator {
    protected static final Logger LOGGER = LoggerFactory.getLogger(PhotoStatisticsCalculator.class);

    public abstract void calculate(Collection<? extends Photo> collection);

}
