package com.reindebock.projects.itemreader;

import java.util.LinkedList;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.ListItemWriter;

public class ItemReaderFromListItemWriter<T> implements ItemReader<T> {
    private final ListItemWriter<T> listWriter;
    private List<T> items;

    public ItemReaderFromListItemWriter(ListItemWriter<T> listWriter) {
        this.listWriter = listWriter;
    }

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (items == null) {
            items = new LinkedList<T>();
            items.addAll((List<T>)listWriter.getWrittenItems());
        }

        if (!items.isEmpty()) {
            return items.remove(0);
        }

        items = null;
        return null;
    }
}
