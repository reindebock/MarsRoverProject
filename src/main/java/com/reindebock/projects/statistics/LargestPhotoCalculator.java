package com.reindebock.projects.statistics;

import java.util.Collection;
import java.util.NoSuchElementException;

import com.reindebock.projects.domain.Photo;

public class LargestPhotoCalculator extends PhotoStatisticsCalculator {

    @Override
    public void calculate(Collection<? extends Photo> collection) {

        try {
            Photo largestPhoto = collection.stream()
                    .filter(photo -> photo.isSuccessful())
                    .max((thisPhoto, thatPhoto) -> (thisPhoto.getFileSize() - thatPhoto.getFileSize()) >= 0 ? 1 : -1)
                    .get();

            LOGGER.info("Largest photo file size: {}   time to download in milliseconds: {}ms   local file: {}   NASA URL: {}",
                    FileStatsFormatter.getFileSizeShortStringInPowersOf1000(largestPhoto.getFileSize()), largestPhoto.getRetrieveTimeInMilliseconds(), largestPhoto.getFileName(), largestPhoto.getNasaPhoto().getImgSrc());
        } catch (NoSuchElementException e) {
            LOGGER.info("No largest file was found");
        }
    }
}
