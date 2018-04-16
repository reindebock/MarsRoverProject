package com.reindebock.projects.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.batch.repeat.RepeatStatus;

import com.reindebock.projects.domain.NASAPhoto;
import com.reindebock.projects.domain.RoverPhotoList;
import com.reindebock.projects.itemreader.ItemReaderFromListItemWriter;

public class PhotoListExtractingTasklet implements Tasklet {

    private final ItemReaderFromListItemWriter<RoverPhotoList> roverPhotoListItemReader;
    private final ListItemWriter<NASAPhoto> nasaPhotoListItemWriter;

    public PhotoListExtractingTasklet(ItemReaderFromListItemWriter<RoverPhotoList> roverPhotoListItemReader, ListItemWriter<NASAPhoto> nasaPhotoListItemWriter) {
        this.roverPhotoListItemReader = roverPhotoListItemReader;
        this.nasaPhotoListItemWriter = nasaPhotoListItemWriter;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        RoverPhotoList roverPhotoList = roverPhotoListItemReader.read();
        while(roverPhotoList != null) {
            nasaPhotoListItemWriter.write(roverPhotoList.getPhotos());
            roverPhotoList = roverPhotoListItemReader.read();
        }

        return RepeatStatus.FINISHED;
    }
}
