package com.reindebock.projects.statistics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.reindebock.projects.domain.Photo;
import com.reindebock.projects.statistics.delegate.LongestDownloadCalculatorDelegate;

public class LongestDownloadCalculatorDelegateTest {

    @Before
    public void setUp() {

    }

    private Photo buildPhoto(long downloadTime) {
        return new Photo.Builder().withRetrieveTime(downloadTime).withSuccessful(true).build();
    }

    @Test
    public void findsLongestDownloadInList() {
        List<Photo> photoList = new ArrayList<>();
        photoList.add(buildPhoto(17));
        photoList.add(buildPhoto(42));
        photoList.add(buildPhoto(38));
        photoList.add(buildPhoto(76));
        photoList.add(buildPhoto(15));
        photoList.add(buildPhoto(43));
        photoList.add(buildPhoto(27));
        photoList.add(buildPhoto(19));

        Photo photo = new LongestDownloadCalculatorDelegate().getLongestDownload(photoList);

        assertEquals(76, photo.getRetrieveTime());
    }

    @Test
    public void findsLongestDownloadInListWhenTwoAreEqual() {
        List<Photo> photoList = new ArrayList<>();
        photoList.add(buildPhoto(17));
        photoList.add(buildPhoto(42));
        photoList.add(buildPhoto(38));
        photoList.add(buildPhoto(76));
        photoList.add(buildPhoto(15));
        photoList.add(buildPhoto(43));
        photoList.add(buildPhoto(27));
        photoList.add(buildPhoto(76));
        photoList.add(buildPhoto(19));

        Photo photo = new LongestDownloadCalculatorDelegate().getLongestDownload(photoList);

        assertEquals(76, photo.getRetrieveTime());
    }

    @Test(expected = NoSuchElementException.class)
    public void throwsNoSuchElementExceptionWhenEmptyList() {
        List<Photo> photoList = new ArrayList<>();

        Photo photo = new LongestDownloadCalculatorDelegate().getLongestDownload(photoList);
    }

}