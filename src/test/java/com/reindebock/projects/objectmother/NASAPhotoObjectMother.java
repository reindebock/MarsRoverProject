package com.reindebock.projects.objectmother;

import com.reindebock.projects.domain.NASAPhoto;

public class NASAPhotoObjectMother {

    public static NASAPhoto buildHappyNASAPhoto() {
        NASAPhoto nasaPhoto = new NASAPhoto();
        nasaPhoto.setId(1);
        nasaPhoto.setCamera(CameraObjectMother.buildHappyCamera());
        nasaPhoto.setRover(RoverObjectMother.buildHappyRover());
        nasaPhoto.setEarthDate("2018-04-11");
        nasaPhoto.setImgSrc("imgSrc");
        nasaPhoto.setSol(1000);

        return nasaPhoto;
    }

}
