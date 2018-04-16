package com.reindebock.projects.processor;

import org.springframework.batch.item.ItemProcessor;

import com.reindebock.projects.domain.NASAPhoto;
import com.reindebock.projects.domain.Photo;
import com.reindebock.projects.photodelegate.PhotoDelegate;

public class RetrievePhotoProcessor implements ItemProcessor<NASAPhoto, Photo> {

    private final PhotoDelegate photoDelegate;

    public RetrievePhotoProcessor(PhotoDelegate photoDelegate) {
        this.photoDelegate = photoDelegate;
    }

    @Override
    public Photo process(NASAPhoto photo) throws Exception {
        return photoDelegate.retrievePhoto(photo);

    }
}
