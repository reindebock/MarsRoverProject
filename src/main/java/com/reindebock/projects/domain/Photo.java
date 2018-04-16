package com.reindebock.projects.domain;

public class Photo {
    private final NASAPhoto nasaPhoto;
    private final boolean successful;
    private final String retrieveStatusCode;
    private final String retrieveStatusMessage;
    private final LocalFileData localFileData;
    private final long retrieveTime;
    private final String mimeType;

    private Photo(NASAPhoto nasaPhoto, boolean successful, String retrieveStatusCode, String retrieveStatusMessage, LocalFileData localFileData, long retrieveTime, String mimeType) {
        this.nasaPhoto = nasaPhoto;
        this.successful = successful;
        this.retrieveStatusCode = retrieveStatusCode;
        this.retrieveStatusMessage = retrieveStatusMessage;
        this.localFileData = localFileData;
        this.retrieveTime = retrieveTime;
        this.mimeType = mimeType;
    }

    public NASAPhoto getNasaPhoto() {
        return nasaPhoto;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getRetrieveStatusCode() {
        return retrieveStatusCode;
    }

    public String getRetrieveStatusMessage() {
        return retrieveStatusMessage;
    }

    public long getFileSize() {
        if (localFileData == null) {
            return 0;
        }

        return localFileData.getFileSize();
    }

    public String getFileName() {
        return localFileData.getFileName();
    }

    public long getRetrieveTime() {
        return retrieveTime;
    }

    public String getMimeType() {
        return mimeType;
    }

    public long getRetrieveTimeInMilliseconds() {
        return retrieveTime / 1000000;
    }

    public double getBytesPerNanosecond() {
        if (localFileData == null) {
            return 0;
        }

        return ((double)localFileData.getFileSize()) / retrieveTime;
    }

    public static class Builder {
        private NASAPhoto nasaPhoto;
        private boolean successful;
        private String retrieveStatusCode;
        private String retrieveStatusMessage;
        private LocalFileData localFileData;
        private long retrieveTime = 0;
        private String mimeType = "";

        public Builder withNASAPhoto(NASAPhoto nasaPhoto) {
            this.nasaPhoto = nasaPhoto;
            return this;
        }

        public Builder withSuccessful(boolean successful) {
            this.successful = successful;
            return this;
        }

        public Builder withRetrieveStatusCode(String retrieveStatusCode) {
            this.retrieveStatusCode = retrieveStatusCode;
            return this;
        }

        public Builder withRetrieveStatusMessage(String retrieveStatusMessage) {
            this.retrieveStatusMessage = retrieveStatusMessage;
            return this;
        }

        public Builder withLocalFileData(LocalFileData localFileData) {
            this.localFileData = localFileData;
            return this;
        }

        public Builder withRetrieveTime(long retrieveTime) {
            this.retrieveTime = retrieveTime;
            return this;
        }

        public Builder withMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Photo build() {
            return new Photo(nasaPhoto, successful, retrieveStatusCode, retrieveStatusMessage, localFileData, retrieveTime, mimeType);
        }
    }
}

