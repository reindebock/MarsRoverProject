package com.reindebock.projects.statistics.delegate;

import java.util.Collection;

import com.reindebock.projects.domain.Photo;

public class SlowestDownloadCalculatorDelegate {

    public Photo getSlowestDownload(Collection<? extends Photo> collection) {
        return collection.stream()
                .filter(photo -> photo.isSuccessful())
                .min((thisPhoto, thatPhoto) -> thisPhoto.getBytesPerNanosecond() >= thatPhoto.getBytesPerNanosecond() ? 1 : -1)
                .get();
    }

}
