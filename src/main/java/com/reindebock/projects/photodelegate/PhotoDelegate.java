package com.reindebock.projects.photodelegate;

import java.io.InputStream;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.reindebock.projects.domain.LocalFileData;
import com.reindebock.projects.domain.NASAPhoto;
import com.reindebock.projects.domain.Photo;
import com.reindebock.projects.domain.PhotoFactory;
import com.reindebock.projects.filesystem.FileSystemImageWriter;

@Component
public class PhotoDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoDelegate.class);

    private final ClientHttpRequestFactory requestFactory;
    private final FileSystemImageWriter imageWriter;


    @Autowired
    public PhotoDelegate(ClientHttpRequestFactory requestFactory, FileSystemImageWriter imageWriter) {
        this.requestFactory = requestFactory;
        this.imageWriter = imageWriter;
    }

    public Photo retrievePhoto(NASAPhoto nasaPhoto) {
        ClientHttpResponse httpResponse = null;
        InputStream imageStream = null;
        Photo photo = null;
        try {
            ClientHttpRequest httpRequest = requestFactory.createRequest(URI.create(nasaPhoto.getImgSrc()), HttpMethod.GET);
            long startTime = System.nanoTime();
            httpResponse = httpRequest.execute();

            imageStream = httpResponse.getBody();
            LocalFileData localFileData = null;
            if (isSuccessfulResponse(httpResponse.getStatusCode())) {
                localFileData = imageWriter.writeImageToFileSystem(nasaPhoto, imageStream);
                long endTime = System.nanoTime();
                photo = PhotoFactory.buildSuccessfullyRetrievedPhoto(nasaPhoto,
                        localFileData,
                        httpResponse.getStatusCode().toString(),
                        httpResponse.getStatusText(),
                        httpResponse.getHeaders().getContentType().toString(),
                        endTime - startTime);
            } else {
                photo = handleUnsuccessfulResponse(nasaPhoto, httpResponse);
            }
        } catch (Exception e) {
            photo = handleException(nasaPhoto, e);
        } finally {
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (Exception e) {
                    handleException(nasaPhoto, e);
                }
            }
            if (httpResponse != null) {
                httpResponse.close();
            }
        }

        return photo;
    }

    private boolean isSuccessfulResponse(HttpStatus statusCode) {
        return statusCode.is2xxSuccessful();
    }

    private Photo handleUnsuccessfulResponse(NASAPhoto nasaPhoto, ClientHttpResponse httpResponse) throws Exception {
        String statusCode = httpResponse.getStatusCode().toString();
        String statusMessage = httpResponse.getStatusText();
        LOGGER.error("Failed to retrieve image from {}  status code: {}   message: {}", nasaPhoto.getImgSrc(), statusCode, statusMessage);

        return PhotoFactory.buildUnsuccessfullyRetrievedPhoto(nasaPhoto,
                statusCode,
                statusMessage,
                httpResponse.getHeaders().getContentType().toString());
    }

    private Photo handleException(NASAPhoto photo, Exception e) {
        LOGGER.error("{} caught trying to write image {}   message: {}", e.getClass().getName(), photo.getImgSrc(), e.getMessage());

        return PhotoFactory.buildPhotoFromThrowable(photo, e);
    }
}
