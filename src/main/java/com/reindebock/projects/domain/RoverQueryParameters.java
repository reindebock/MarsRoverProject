package com.reindebock.projects.domain;

public class RoverQueryParameters {
    private final String earthDate;
    private final String roverName;

    public RoverQueryParameters(String earthDate, String roverName) {
        this.earthDate = earthDate;
        this.roverName = roverName;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public String getRoverName() {
        return roverName;
    }
}
