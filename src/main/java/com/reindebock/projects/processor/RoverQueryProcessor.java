package com.reindebock.projects.processor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.reindebock.projects.domain.RoverPhotoList;
import com.reindebock.projects.domain.RoverQueryParameters;

public class RoverQueryProcessor implements ItemProcessor<RoverQueryParameters, RoverPhotoList> {

    private final RestTemplate restTemplate;

    public RoverQueryProcessor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public RoverPhotoList process(RoverQueryParameters item) throws Exception {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("roverName", item.getRoverName());
        parameterMap.put("earthDate", item.getEarthDate());

        //earth_date={earthDate} -- a lot of photos
        //sol=1000&camera=FHAZ -- a lot fewer images, but notice that earth_date is not part of query params and same images will download for each date in date file -- only first attempt will succeed.
        ResponseEntity<RoverPhotoList> responseEntity = restTemplate.exchange("https://api.nasa.gov/mars-photos/api/v1/rovers/{roverName}/photos?sol=1000&camera=FHAZ&api_key=hY21LxygqNFIcrW52vRlqOW48Y3qUg4imPXszecv",
                                    HttpMethod.GET, null, RoverPhotoList.class, parameterMap);
        return responseEntity.getBody();
    }
}
