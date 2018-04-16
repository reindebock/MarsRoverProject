package com.reindebock.projects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
public class MarsRover {

    public static void main (String ...args) throws Exception {
        SpringApplication.run(MarsRover.class, args);
    }

}
