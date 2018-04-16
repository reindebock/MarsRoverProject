package com.reindebock.projects.statistics.delegate;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

import com.reindebock.projects.domain.LocalFileData;
import com.reindebock.projects.domain.Photo;

public class SlowestDownloadCalculatorDelegateTest {

    private Photo buildPhoto(long downloadTime, long fileSize) {
        return new Photo.Builder()
                .withSuccessful(true)
                .withRetrieveTime(downloadTime)
                .withLocalFileData(new LocalFileData("fileName", fileSize))
                .build();
    }

    @Test
    public void findsSlowestDownloadInList() {
        List<Photo> photoList = new ArrayList<>();
        photoList.add(buildPhoto(90, 1));
        photoList.add(buildPhoto(5, 1));
        photoList.add(buildPhoto(30, 1));
        photoList.add(buildPhoto(500, 1));
        photoList.add(buildPhoto(91, 1));
        photoList.add(buildPhoto(6, 1));
        photoList.add(buildPhoto(34, 1));

        Photo photo = new SlowestDownloadCalculatorDelegate().getSlowestDownload(photoList);

        assertEquals(500, photo.getRetrieveTime());
    }

    @Test
    public void findsSlowestDownloadInListWhenTwoAreEqual() {
        List<Photo> photoList = new ArrayList<>();
        photoList.add(buildPhoto(90, 1));
        photoList.add(buildPhoto(500, 1));
        photoList.add(buildPhoto(5, 1));
        photoList.add(buildPhoto(30, 1));
        photoList.add(buildPhoto(500, 1));
        photoList.add(buildPhoto(91, 1));
        photoList.add(buildPhoto(6, 1));
        photoList.add(buildPhoto(34, 1));

        Photo photo = new SlowestDownloadCalculatorDelegate().getSlowestDownload(photoList);

        assertEquals(500, photo.getRetrieveTime());
    }

    @Test(expected = NoSuchElementException.class)
    public void empyList() {
        List<Photo> photoList = new ArrayList<>();

        Photo photo = new SlowestDownloadCalculatorDelegate().getSlowestDownload(photoList);
    }

}