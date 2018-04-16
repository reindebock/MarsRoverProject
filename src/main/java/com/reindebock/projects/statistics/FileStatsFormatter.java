package com.reindebock.projects.statistics;

public class FileStatsFormatter {

    public static String getFileSizeShortStringInPowersOf1024(long fileSize) {
        String[] abbreviations = new String[] { "bytes", "KiB", "MiB", "GiB", "TiB"};
        int abbrIndex = 0;
        long magnitude = fileSize;
        int round = magnitude > 512 ? 1 : 0;
        while (magnitude > 1024) {
            abbrIndex++;
            magnitude >>= 10;
            round = magnitude > 512 ? 1 : 0;
        }

        return new StringBuilder().append(magnitude + round).append(" ").append(abbreviations[abbrIndex]).toString();
    }

    public static String getFileSizeShortStringInPowersOf1000(long fileSize) {
        String[] abbreviations = new String[] { "bytes", "KB", "MB", "GB", "TB"};
        int abbrIndex = 0;
        long magnitude = fileSize;
        int round = magnitude > 500 ? 1 : 0;
        while (magnitude > 1000) {
            abbrIndex++;
            magnitude /= 1000;
            round = magnitude > 500 ? 1 : 0;
        }

        return new StringBuilder().append(magnitude + round).append(" ").append(abbreviations[abbrIndex]).toString();
    }

    public static double getKilobytesPerSecond(long fileSize, long retrieveTime) {
        if (retrieveTime == 0) {
            return 0;
        }

        return ((double)fileSize / ((double)retrieveTime /  1000000000)) / 1000;
    }

    public static double convertBytesPerNanoToKilobytesPerSecond(double bytesPerNano) {
        return bytesPerNano * 1000000;
    }

}
