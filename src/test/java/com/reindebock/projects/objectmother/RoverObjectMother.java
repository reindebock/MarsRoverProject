package com.reindebock.projects.objectmother;

import com.reindebock.projects.domain.Rover;

public class RoverObjectMother {

    public static Rover buildHappyRover() {
        Rover rover = new Rover();
        rover.setId(1);
        rover.setName("rover");
        rover.setStatus("status");
        rover.setLandingDate("landingDate");
        rover.setLaunchDate("launchDate");
        rover.setMaxDate("maxDate");
        rover.setMaxSol(Integer.MAX_VALUE);

        return rover;
    }
}
