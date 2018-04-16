package com.reindebock.projects.statistics.delegate;

import java.util.Collection;

import com.reindebock.projects.domain.Photo;

public class LongestDownloadCalculatorDelegate {

    public Photo getLongestDownload(Collection<? extends Photo> collection) {
        Photo longestDownload = collection.stream()
                .filter(photo -> photo.isSuccessful())
                .max((thisPhoto, thatPhoto) -> (thisPhoto.getRetrieveTime() - thatPhoto.getRetrieveTime()) >= 0 ? 1 : -1)
                .get();

        return longestDownload;
    }

}
