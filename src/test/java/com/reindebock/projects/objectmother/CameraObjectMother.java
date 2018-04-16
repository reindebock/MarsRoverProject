package com.reindebock.projects.objectmother;

import com.reindebock.projects.domain.Camera;

public class CameraObjectMother {

    public static Camera buildHappyCamera() {
        Camera camera = new Camera();
        camera.setId(1);
        camera.setName("camera");
        camera.setFullName("camera");
        camera.setRoverId(1);

        return camera;
    }

}
