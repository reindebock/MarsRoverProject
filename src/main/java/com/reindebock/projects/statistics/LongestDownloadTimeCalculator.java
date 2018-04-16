package com.reindebock.projects.statistics;

import java.util.Collection;
import java.util.NoSuchElementException;

import com.reindebock.projects.domain.Photo;
import com.reindebock.projects.statistics.delegate.LongestDownloadCalculatorDelegate;
import com.reindebock.projects.statistics.delegate.SlowestDownloadCalculatorDelegate;

public class LongestDownloadTimeCalculator extends PhotoStatisticsCalculator {
    @Override
    public void calculate(Collection<? extends Photo> collection) {
        try {
            Photo longestDownloadPhoto = new LongestDownloadCalculatorDelegate().getLongestDownload(collection);

            LOGGER.info("Longest download time in milliseconds: {} ms   file size: {}   local file: {}   NASA URL: {}",
                    longestDownloadPhoto.getRetrieveTimeInMilliseconds(), FileStatsFormatter.getFileSizeShortStringInPowersOf1000(longestDownloadPhoto.getFileSize()), longestDownloadPhoto.getFileName(), longestDownloadPhoto.getNasaPhoto().getImgSrc());

            Photo slowestDownloadKBPerSecond = new SlowestDownloadCalculatorDelegate().getSlowestDownload(collection);

            LOGGER.info("Slowest download rate in KB/sec: {} KB/sec   file size: {}   download time: {} ms   local file: {}   NASA URL: {}",
                    FileStatsFormatter.getKilobytesPerSecond(slowestDownloadKBPerSecond.getFileSize(), slowestDownloadKBPerSecond.getRetrieveTime()),
                    FileStatsFormatter.getFileSizeShortStringInPowersOf1000(slowestDownloadKBPerSecond.getFileSize()),
                    slowestDownloadKBPerSecond.getRetrieveTimeInMilliseconds(),
                    slowestDownloadKBPerSecond.getFileName(),
                    slowestDownloadKBPerSecond.getNasaPhoto().getImgSrc());

        } catch (NoSuchElementException e) {
            LOGGER.info("No longest download file was found");
        }
    }
}
