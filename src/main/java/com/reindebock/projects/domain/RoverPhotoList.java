package com.reindebock.projects.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoverPhotoList {
    @JsonProperty("photos")
    private List<NASAPhoto> photos;

    public List<NASAPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<NASAPhoto> photos) {
        this.photos = photos;
    }
}
