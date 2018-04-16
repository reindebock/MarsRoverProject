package com.reindebock.projects.processor;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.batch.item.ItemProcessor;

import com.reindebock.projects.domain.RoverQueryParameters;

public class DateFileProcessor implements ItemProcessor<String, RoverQueryParameters> {

    @Override
    public RoverQueryParameters process(String item) throws Exception {
        DateTime queryDate = DateTime.parse(item, DateTimeFormat.forPattern("dd-MMM-yy"));
        return new RoverQueryParameters(queryDate.toString("yyyy-MM-dd"), "curiosity");
    }
}
