package com.reindebock.projects.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

import com.reindebock.projects.completionlistener.JobCompletionNotificationListener;
import com.reindebock.projects.domain.NASAPhoto;
import com.reindebock.projects.domain.Photo;
import com.reindebock.projects.domain.RoverPhotoList;
import com.reindebock.projects.domain.RoverQueryParameters;
import com.reindebock.projects.itemreader.ItemReaderFromListItemWriter;
import com.reindebock.projects.photodelegate.PhotoDelegate;
import com.reindebock.projects.processor.DateFileProcessor;
import com.reindebock.projects.processor.RetrievePhotoProcessor;
import com.reindebock.projects.processor.RoverQueryProcessor;
import com.reindebock.projects.statistics.AggregateDownloadStatsCalculator;
import com.reindebock.projects.statistics.CountDistinctCamerasCalculator;
import com.reindebock.projects.statistics.CountDistinctRoversCalculator;
import com.reindebock.projects.statistics.CountPhotosCalculator;
import com.reindebock.projects.statistics.ErrorReportCalculator;
import com.reindebock.projects.statistics.LargestPhotoCalculator;
import com.reindebock.projects.statistics.LongestDownloadTimeCalculator;
import com.reindebock.projects.statistics.PhotoStatisticsCalculator;
import com.reindebock.projects.tasklet.PhotoListExtractingTasklet;

@Configuration
@EnableBatchProcessing
public class RoverJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<String> dateFileReader() {
        return new FlatFileItemReaderBuilder<String>()
                .name("dateFileReader")
                .resource(new ClassPathResource("MarsDates.txt"))
                .lineMapper(new PassThroughLineMapper())
                .build();
    }

    @Bean
    public DateFileProcessor dateFileProcessor() {
        return new DateFileProcessor();
    }

    @Bean
    public ListItemWriter<RoverQueryParameters> roverQueryParametersListItemWriter() {
        return new ListItemWriter<>();
    }

    @Bean()
    public ItemReaderFromListItemWriter<RoverQueryParameters> roverQueryParametersListItemReader(ListItemWriter<RoverQueryParameters> parameterListWriter) {
        return new ItemReaderFromListItemWriter<>(parameterListWriter);
    }

    @Bean
    public RoverQueryProcessor roverQueryProcessor(RestTemplate restTemplate) {
        return new RoverQueryProcessor(restTemplate);
    }

    @Bean
    public ListItemWriter<RoverPhotoList> roverPhotoListItemWriter() {
        return new ListItemWriter<>();
    }

    @Bean
    public ItemReaderFromListItemWriter<RoverPhotoList> roverPhotoListItemReader(ListItemWriter<RoverPhotoList> roverPhotoListWriter) {
        return new ItemReaderFromListItemWriter<>(roverPhotoListWriter);
    }

    @Bean
    public ListItemWriter<NASAPhoto> nasaPhotoListItemWriter() {
        return new ListItemWriter<>();
    }

    @Bean
    public Tasklet photoListExtractingTasklet(ItemReaderFromListItemWriter<RoverPhotoList> roverPhotoListItemReader, ListItemWriter<NASAPhoto> photoListItemWriter) {
        return new PhotoListExtractingTasklet(roverPhotoListItemReader, photoListItemWriter);
    }

    @Bean
    public ItemReaderFromListItemWriter<NASAPhoto> nasaPhotoListItemReader(ListItemWriter<NASAPhoto> nasaPhotoListItemWriter) {
        return new ItemReaderFromListItemWriter<>(nasaPhotoListItemWriter);
    }

    @Bean
    public RetrievePhotoProcessor retrievePhotoProcessor(PhotoDelegate photoDelegate) {
        return new RetrievePhotoProcessor(photoDelegate);
    }

    @Bean
    public ListItemWriter<Photo> photoListItemWriter() {
        return new ListItemWriter<>();
    }

    @Bean
    public List<PhotoStatisticsCalculator> statisticsCalculators() {
        List<PhotoStatisticsCalculator> statisticsCalculators = new ArrayList<>();

        statisticsCalculators.add(new CountDistinctRoversCalculator());
        statisticsCalculators.add(new CountDistinctCamerasCalculator());
        statisticsCalculators.add(new CountPhotosCalculator());
        statisticsCalculators.add(new LargestPhotoCalculator());
        statisticsCalculators.add(new LongestDownloadTimeCalculator());
        statisticsCalculators.add(new AggregateDownloadStatsCalculator());
        statisticsCalculators.add(new ErrorReportCalculator());

        return statisticsCalculators;
    }

    @Bean
    public Step dateFileReadStep(FlatFileItemReader<String> dateFileReader, DateFileProcessor dateFileProcessor, ListItemWriter<RoverQueryParameters> roverQueryParametersListItemWriter) {
        return stepBuilderFactory.get("dateFileReadStep")
                .<String, RoverQueryParameters>chunk(10)
                .reader(dateFileReader)
                .processor(dateFileProcessor)
                .writer(roverQueryParametersListItemWriter)
                .build();
    }

    @Bean
    public Step roverQueryStep(ItemReaderFromListItemWriter<RoverQueryParameters> roverQueryParametersListItemReader, RoverQueryProcessor roverQueryProcessor, ListItemWriter<RoverPhotoList> roverPhotoListItemWriter) {
        return stepBuilderFactory.get("roverQueryStep")
                .<RoverQueryParameters, RoverPhotoList>chunk(10)
                .reader(roverQueryParametersListItemReader)
                .processor(roverQueryProcessor)
                .writer(roverPhotoListItemWriter)
                .build();
    }

    @Bean
    public Step buildPhotoListStep(Tasklet photoListExtractingTasklet) {
        return stepBuilderFactory.get("buildPhotoListStep")
                .tasklet(photoListExtractingTasklet)
                .build();
    }

    @Bean
    public Step retrievePhotosStep(ItemReaderFromListItemWriter<NASAPhoto> nasaPhotoListItemReader, RetrievePhotoProcessor retrievePhotoProcessor, ListItemWriter<Photo> photoListItemWriter) {
        return stepBuilderFactory.get("retrievePhotosStep")
                .<NASAPhoto, Photo>chunk(100)
                .reader(nasaPhotoListItemReader)
                .processor(retrievePhotoProcessor)
                .writer(photoListItemWriter)
                .build();
    }

    @Bean
    public Job importJob(JobCompletionNotificationListener listener, Step dateFileReadStep, Step roverQueryStep, Step buildPhotoListStep, Step retrievePhotosStep) {
        return jobBuilderFactory.get("importJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(dateFileReadStep)
                .next(roverQueryStep)
                .next(buildPhotoListStep)
                .next(retrievePhotosStep)
                .build();
    }

}
