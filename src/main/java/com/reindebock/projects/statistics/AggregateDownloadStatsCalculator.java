package com.reindebock.projects.statistics;

import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.NoSuchElementException;

import com.reindebock.projects.domain.Photo;

public class AggregateDownloadStatsCalculator extends PhotoStatisticsCalculator {

    @Override
    public void calculate(Collection<? extends Photo> collection) {
        try {
            long totalDownloadSize = collection.stream()
                    .filter(photo -> photo.isSuccessful())
                    .mapToLong(photo -> photo.getFileSize())
                    .sum();

            long totalRetrieveTime = collection.stream()
                    .filter(photo -> photo.isSuccessful())
                    .mapToLong(photo -> photo.getRetrieveTime())
                    .sum();

            double averageDownloadRate = (double) totalDownloadSize / (double) totalRetrieveTime;

            DoubleSummaryStatistics summaryStats = collection.stream()
                    .filter(photo -> photo.isSuccessful())
                    .mapToDouble(photo -> photo.getBytesPerNanosecond())
                    .summaryStatistics();

            Photo photoAtMedian = collection.stream()
                    .filter(photo -> photo.isSuccessful())
                    .sorted((thisPhoto, thatPhoto) -> thisPhoto.getBytesPerNanosecond() >= thatPhoto.getBytesPerNanosecond() ? 1 : -1)
                    .skip(summaryStats.getCount() / 2)
                    .findFirst()
                    .get();

            Photo photoAt90thPercentile = collection.stream()
                    .filter(photo -> photo.isSuccessful())
                    .sorted((thisPhoto, thatPhoto) -> thisPhoto.getBytesPerNanosecond() >= thatPhoto.getBytesPerNanosecond() ? -1 : 1)
                    .skip(Math.round(summaryStats.getCount() * 0.9) - 1)
                    .findFirst()
                    .get();


            double sumOfSquaredDifferencesFromMean = collection.stream()
                    .filter(photo -> photo.isSuccessful())
                    .mapToDouble(photo -> Math.pow(((double) photo.getFileSize() / (double) photo.getRetrieveTime()) - averageDownloadRate, 2))
                    .sum();

            double stdDev = Math.sqrt(sumOfSquaredDifferencesFromMean / (summaryStats.getCount() - 1));

            LOGGER.info("Cumulative download statistics -- number of files: {}   total file size: {}   total download rate: {} KB/sec   median file download rate: {} KB/sec   90th percentile slowest download rate: {} KB/sec   standard deviation: {} KB/sec",
                    summaryStats.getCount(),
                    FileStatsFormatter.getFileSizeShortStringInPowersOf1000(totalDownloadSize),
                    FileStatsFormatter.getKilobytesPerSecond(totalDownloadSize, totalRetrieveTime),
                    FileStatsFormatter.getKilobytesPerSecond(photoAtMedian == null ? 0 : photoAtMedian.getFileSize(), photoAtMedian == null ? 0 : photoAtMedian.getRetrieveTime()),
                    FileStatsFormatter.getKilobytesPerSecond(photoAtMedian == null ? 0 : photoAt90thPercentile.getFileSize(), photoAtMedian == null ? 0 : photoAt90thPercentile.getRetrieveTime()),
                    FileStatsFormatter.convertBytesPerNanoToKilobytesPerSecond(stdDev));
        } catch (NoSuchElementException e) {
            LOGGER.info("Unable to generate cumulative statistics -- no successful elements this time...");
        }
    }
}
