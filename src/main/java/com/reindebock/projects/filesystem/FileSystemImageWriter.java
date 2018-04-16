package com.reindebock.projects.filesystem;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.reindebock.projects.domain.LocalFileData;
import com.reindebock.projects.domain.NASAPhoto;
import com.reindebock.projects.exception.FileSystemImageWriterException;
import com.reindebock.projects.statistics.FileStatsFormatter;

@Component
public class FileSystemImageWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemImageWriter.class);

    public LocalFileData writeImageToFileSystem(NASAPhoto photo, InputStream data) throws Exception {
        ReadableByteChannel inputChannel = null;
        FileOutputStream outputStream = null;
        LocalFileData localFileData = null;
        Path file = Paths.get(System.getProperty("user.home"), "Documents", "rover_images", photo.getRover().getName(), photo.getCamera().getName(), photo.getEarthDate(), getFileName(photo));

        try {
            if (Files.exists(file)) {
                throw new FileAlreadyExistsException("Cannot write image file - file already exists -- " + file.toString());
            }

            Path dir = Paths.get(System.getProperty("user.home"), "Documents", "rover_images", photo.getRover().getName(), photo.getCamera().getName(), photo.getEarthDate());
            if (Files.notExists(dir)) {
                Files.createDirectories(dir);
            }

            inputChannel = Channels.newChannel(data);
            outputStream = new FileOutputStream(file.toString());
            long bytesRead = outputStream.getChannel().transferFrom(inputChannel, 0, Long.MAX_VALUE);

            localFileData = new LocalFileData(file.toString(), bytesRead);
            LOGGER.info("Downloaded image for rover {} from camera {} at URI {} to local file {}.  File size: {}", photo.getRover().getName(), photo.getCamera().getName(),
                    photo.getImgSrc(), file.toString(), FileStatsFormatter.getFileSizeShortStringInPowersOf1024(localFileData.getFileSize()));
        } catch (Exception e) {
            handleException(photo, file.toString(), e);
        } finally {
            if (inputChannel != null) {
                try {
                    inputChannel.close();
                } catch (Exception e) {
                    handleException(photo, file.toString(), e);
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    handleException(photo, file.toString(), e);
                }
            }
        }

        return localFileData;
    }

    private String getFileName(NASAPhoto nasaPhoto) {
        return nasaPhoto.getImgSrc().substring(nasaPhoto.getImgSrc().lastIndexOf("/") + 1);
    }

    private void handleException(NASAPhoto photo, String fileName,  Exception e) throws Exception {
        String message = e.getClass().getName() + " caught trying to write image " + photo.getImgSrc() + "\n    to file " + fileName + "\n   message: " + e.getMessage();
        throw new FileSystemImageWriterException(message, e);
    }

}
