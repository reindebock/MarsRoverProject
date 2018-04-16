package com.reindebock.projects.processor;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import com.reindebock.projects.domain.NASAPhoto;
import com.reindebock.projects.domain.RoverPhotoList;

public class RoverPhotoListProcessor implements ItemProcessor<RoverPhotoList, List<NASAPhoto>> {
    @Override
    public List<NASAPhoto> process(RoverPhotoList item) throws Exception {
        return item.getPhotos();
    }
}
