package com.reindebock.projects.photodelegate;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;

import com.reindebock.projects.domain.LocalFileData;
import com.reindebock.projects.domain.NASAPhoto;
import com.reindebock.projects.domain.Photo;
import com.reindebock.projects.exception.FileSystemImageWriterException;
import com.reindebock.projects.filesystem.FileSystemImageWriter;
import com.reindebock.projects.objectmother.NASAPhotoObjectMother;

public class PhotoDelegateTest {

    private PhotoDelegate photoDelegate;
    private ClientHttpRequest mockHttpRequest;
    private ClientHttpRequestFactory mockClientRequestFactory;
    private ClientHttpResponse mockHttpResponse;
    private HttpHeaders httpHeaders;
    private FileSystemImageWriter mockFileSystemImageWriter;

    private NASAPhoto nasaPhoto;

    @Before
    public void setUp() throws Exception {
        mockHttpRequest = Mockito.mock(ClientHttpRequest.class);
        mockClientRequestFactory = Mockito.mock(ClientHttpRequestFactory.class);
        when(mockClientRequestFactory.createRequest(any(URI.class), eq(HttpMethod.GET))).thenReturn(mockHttpRequest);
        mockHttpResponse = Mockito.mock(ClientHttpResponse.class);
        when(mockHttpResponse.getBody()).thenReturn(null);
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        when(mockHttpResponse.getHeaders()).thenReturn(httpHeaders);
        when(mockHttpRequest.execute()).thenReturn(mockHttpResponse);
        mockFileSystemImageWriter = Mockito.mock(FileSystemImageWriter.class);
        when(mockFileSystemImageWriter.writeImageToFileSystem(any(NASAPhoto.class), any())).thenReturn(new LocalFileData("fileName", 1000));

        nasaPhoto = NASAPhotoObjectMother.buildHappyNASAPhoto();

        photoDelegate = new PhotoDelegate(mockClientRequestFactory, mockFileSystemImageWriter);
    }

    @Test
    public void photoSuccessfullyDownloaded() throws Exception {
        when(mockHttpResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockHttpResponse.getStatusText()).thenReturn(HttpStatus.OK.getReasonPhrase());

        Photo photo = photoDelegate.retrievePhoto(nasaPhoto);

        assertTrue(photo.isSuccessful());
        assertEquals("200", photo.getRetrieveStatusCode());
        assertEquals(HttpStatus.OK.getReasonPhrase(), photo.getRetrieveStatusMessage());
        assertSame(nasaPhoto, photo.getNasaPhoto());
        assertEquals(1000, photo.getFileSize());
        assertEquals("fileName", photo.getFileName());
        assertEquals(MediaType.IMAGE_JPEG_VALUE, photo.getMimeType());
    }

    @Test
    public void photoUnsuccessfullyDownloaded() throws Exception {
        when(mockHttpResponse.getStatusCode()).thenReturn(HttpStatus.I_AM_A_TEAPOT);
        when(mockHttpResponse.getStatusText()).thenReturn(HttpStatus.I_AM_A_TEAPOT.getReasonPhrase());

        Photo photo = photoDelegate.retrievePhoto(nasaPhoto);

        assertFalse(photo.isSuccessful());
        assertSame(nasaPhoto, photo.getNasaPhoto());
        assertEquals("418", photo.getRetrieveStatusCode());
        assertEquals(0, photo.getFileSize());
        assertEquals(HttpStatus.I_AM_A_TEAPOT.getReasonPhrase(), photo.getRetrieveStatusMessage());
    }

    @Test
    public void exceptionInPhotoDelegateDuringDownload() throws Exception {
        when(mockHttpResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockHttpResponse.getStatusText()).thenReturn(HttpStatus.OK.getReasonPhrase());
        when(mockFileSystemImageWriter.writeImageToFileSystem(nasaPhoto, null)).thenThrow(new FileSystemImageWriterException("Exception thrown from FileSystemImageWriter", new NullPointerException("null pointer in code")));

        Photo photo = photoDelegate.retrievePhoto(nasaPhoto);

        assertFalse(photo.isSuccessful());
        assertSame(nasaPhoto, photo.getNasaPhoto());
        assertEquals("FileSystemImageWriterException", photo.getRetrieveStatusCode());
        assertEquals("Exception thrown from FileSystemImageWriter", photo.getRetrieveStatusMessage());
        assertEquals(0, photo.getFileSize());
    }

}