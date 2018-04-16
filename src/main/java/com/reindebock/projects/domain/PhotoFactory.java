package com.reindebock.projects.domain;

public class PhotoFactory {

    public static Photo buildSuccessfullyRetrievedPhoto(NASAPhoto nasaPhoto, LocalFileData localFileData, String statusCode, String statusMessage, String mimeType, long retriveTime) {

        return new Photo.Builder().withNASAPhoto(nasaPhoto)
                .withSuccessful(true)
                .withRetrieveStatusCode(statusCode)
                .withRetrieveStatusMessage(statusMessage)
                .withLocalFileData(localFileData)
                .withMimeType(mimeType)
                .withRetrieveTime(retriveTime)
                .build();
    }

    public static Photo buildUnsuccessfullyRetrievedPhoto(NASAPhoto nasaPhoto, String statusCode, String statusMessage, String mimeType) {
        return new Photo.Builder().withNASAPhoto(nasaPhoto)
                .withSuccessful(false)
                .withRetrieveStatusCode(statusCode)
                .withRetrieveStatusMessage(statusMessage)
                .withMimeType(mimeType)
                .build();
    }

    public static Photo buildPhotoFromThrowable(NASAPhoto nasaPhoto, Throwable e) {
        return new Photo.Builder().withNASAPhoto(nasaPhoto)
                .withSuccessful(false)
                .withRetrieveStatusCode(e.getClass().getSimpleName())
                .withRetrieveStatusMessage(e.getMessage())
                .build();
    }

}
