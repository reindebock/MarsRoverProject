package com.reindebock.projects.completionlistener;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.reindebock.projects.domain.Photo;
import com.reindebock.projects.statistics.PhotoStatisticsCalculator;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final Collection<? extends Photo> photoList;
    private final List<PhotoStatisticsCalculator> statsCalculators;

    @Autowired
    public JobCompletionNotificationListener(ListItemWriter<Photo> photoListItemWriter, List<PhotoStatisticsCalculator> statsCalculators) {
        this.photoList = photoListItemWriter.getWrittenItems();
        this.statsCalculators = statsCalculators;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        for (PhotoStatisticsCalculator statsCalculator : statsCalculators) {
            statsCalculator.calculate(photoList);
        }
    }

}
