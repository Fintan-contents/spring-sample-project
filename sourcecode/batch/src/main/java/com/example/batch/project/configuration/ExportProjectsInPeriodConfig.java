package com.example.batch.project.configuration;

import java.util.Map;

import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.PathResource;

import com.example.batch.common.configuration.BatchBaseConfig;
import com.example.batch.project.item.ExportProjectsInPeriodItem;
import com.example.batch.project.mapper.ExportProjectsInPeriodMapper;
import com.example.batch.project.processor.ExportProjectsInPeriodItemProcessor;
import com.example.batch.project.writer.ExportProjectsInPeriodFieldExtractor;
import com.example.common.generated.model.Project;

/**
 * 期間内プロジェクト一括出力バッチのConfig。
 */
@Configuration
@PropertySource(value = {
        "classpath:/properties/project/ExportProjectsInPeriod.properties",
        "classpath:/properties/project/ExportProjectsInPeriod-${spring.profiles.active}.properties",
}, encoding = "UTF-8", ignoreResourceNotFound = true)
public class ExportProjectsInPeriodConfig extends BatchBaseConfig {
    @Autowired
    private ExportProjectsInPeriodItemProcessor exportProjectsInPeriodItemProcessor;
    @Autowired
    private ExportProjectsInPeriodFieldExtractor exportProjectsInPeriodFieldExtractor;

    /**
     * 期間内プロジェクト一括出力バッチのPropertiesを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    @ConfigurationProperties(prefix = "project.export-projects-in-period")
    public ExportProjectsInPeriodProperties exportProjectsInPeriodProperties() {
        return new ExportProjectsInPeriodProperties();
    }

    /**
     * 期間内プロジェクト一括出力バッチのItemReaderを構築する。
     * 
     * @param businessDate
     * @return 構築されたインスタンス
     */
    @Bean
    @StepScope
    public MyBatisCursorItemReader<Project> exportProjectsInPeriodItemReader(
            @Value("#{jobParameters['businessDate']}") String businessDate) {
        if (businessDate != null && businessDate.isBlank() == false) {
            businessDateSupplier.setFixedDate(businessDate);
        }

        return new MyBatisCursorItemReaderBuilder<Project>()
                .sqlSessionFactory(sqlSessionFactory)
                .parameterValues(Map.of("businessDate", businessDateSupplier.getDate()))
                .queryId(ExportProjectsInPeriodMapper.class.getName() + ".selectProjectsInPeriod")
                .build();
    }

    /**
     * 期間内プロジェクト一括出力バッチのItemWriterを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    @StepScope
    public FlatFileItemWriter<ExportProjectsInPeriodItem> exportProjectsInPeriodItemWriter() {
        return new FlatFileItemWriterBuilder<ExportProjectsInPeriodItem>()
                .encoding("UTF-8")
                .saveState(false)
                .name("BA1060101")
                .lineSeparator("\r\n")
                .resource(new PathResource(exportProjectsInPeriodProperties().getOutputFilePath()))
                .shouldDeleteIfEmpty(false)
                .delimited()
                .delimiter(",")
                .fieldExtractor(exportProjectsInPeriodFieldExtractor)
                .build();
    }

    /**
     * 期間内プロジェクト一括出力バッチのStepを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    public Step exportProjectsInPeriodStep() {
        int chunkSize = exportProjectsInPeriodProperties().getChunkSize();

        return new StepBuilder("BA1060101", jobRepository)
                .<Project, ExportProjectsInPeriodItem>chunk(chunkSize, platformTransactionManager)
                .reader(exportProjectsInPeriodItemReader(null))
                .processor(exportProjectsInPeriodItemProcessor)
                .writer(exportProjectsInPeriodItemWriter())
                .listener(loggingCountChunkListener)
                .build();
    }

    /**
     * 期間内プロジェクト一括出力バッチのJobを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    public Job exportProjectsInPeriodJob() {
        return new JobBuilder("BA1060101", jobRepository)
                .start(exportProjectsInPeriodStep())
                .listener(loggingCountJobListener)
                .build();
    }
}
