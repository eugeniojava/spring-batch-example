package com.eugenioamn.springbatchexample.config;

import com.eugenioamn.springbatchexample.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<User> itemReader,
                   ItemProcessor<User, User> itemProcessor,
                   ItemWriter<User> itemWriter) {
        Step step = stepBuilderFactory.get("etl-file-load")
                .<User, User>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        return jobBuilderFactory.get("etl-load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public FlatFileItemReader<User> flatFileItemReader(
            @Value("${input}") Resource resource) {
        FlatFileItemReader<User> flatFileItemReader =
                new FlatFileItemReader<>();

        flatFileItemReader.setResource(resource);
        flatFileItemReader.setName("csv-reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());

        return flatFileItemReader;
    }

    @Bean
    public LineMapper<User> lineMapper() {
        BeanWrapperFieldSetMapper<User> beanWrapperFieldSetMapper =
                new BeanWrapperFieldSetMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer =
                new DelimitedLineTokenizer();
        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();

        beanWrapperFieldSetMapper.setTargetType(User.class);
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames(new String[]{"id", "name",
                "department", "salary"});
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        return defaultLineMapper;
    }
}
