package com.reindebock.projects.itemreader;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.ListItemWriter;

import com.reindebock.projects.domain.RoverQueryParameters;

public class RoverQueryParameterListReader implements ItemReader<RoverQueryParameters> {
    private final ListItemWriter<RoverQueryParameters> listWriter;
    private List<RoverQueryParameters> items;

    public RoverQueryParameterListReader(ListItemWriter<RoverQueryParameters> listWriter) {
        this.listWriter = listWriter;
    }

    @Override
    public RoverQueryParameters read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (items == null || items.isEmpty()) {
            items = (List<RoverQueryParameters>)listWriter.getWrittenItems();
        }

        if (!items.isEmpty()) {
            return items.remove(0);
        }

        return null;

    }
}
