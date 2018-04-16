package com.reindebock.projects.domain;

public class LocalFileData {
    private final long fileSize;
    private final String fileName;

    public LocalFileData(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }
}
