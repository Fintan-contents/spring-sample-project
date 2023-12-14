package com.example.batch.project.configuration;

import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.example.batch.common.configuration.BatchBaseConfig;
import com.example.batch.common.exception.BatchSkipItemException;
import com.example.batch.project.mapper.ImportProjectsMapper;
import com.example.batch.project.processor.ImportProjectsItemProcessor;
import com.example.batch.project.writer.ImportProjectsItemWriter;
import com.example.common.generated.model.Project;
import com.example.common.generated.model.ProjectWork;

/**
 * プロジェクト一括登録バッチ/本テーブル登録のConfig。
 */
@Configuration
@PropertySource(value = {
        "classpath:/properties/project/ImportProjects.properties",
        "classpath:/properties/project/ImportProjects-${spring.profiles.active}.properties",
}, encoding = "UTF-8", ignoreResourceNotFound = true)
public class ImportProjectsConfig extends BatchBaseConfig {
    @Autowired
    private ImportProjectsItemProcessor importProjectsItemProcessor;
    @Autowired
    private ImportProjectsItemWriter importProjectsItemWriter;

    /**
     * プロジェクト一括登録バッチ/本テーブル登録のPropertiesを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    @ConfigurationProperties(prefix = "project.import-projects")
    public ImportProjectsProperties importProjectsProperties() {
        return new ImportProjectsProperties();
    }

    /**
     * プロジェクト一括登録バッチ/本テーブル登録のItemReaderを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    @StepScope
    public MyBatisCursorItemReader<ProjectWork> importProjectsItemReader() {
        return new MyBatisCursorItemReaderBuilder<ProjectWork>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId(ImportProjectsMapper.class.getName() + ".selectProjectWorksInPeriod")
                .build();
    }

    /**
     * プロジェクト一括登録バッチ/本テーブル登録のStepを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    public Step importProjectsStep() {
        int chunkSize = importProjectsProperties().getChunkSize();

        return new StepBuilder("BA1060202", jobRepository)
                .<ProjectWork, Project> chunk(chunkSize, platformTransactionManager)

                .reader(importProjectsItemReader())
                .processor(importProjectsItemProcessor)
                .writer(importProjectsItemWriter)

                .faultTolerant()
                .skip(ValidationException.class)
                .skip(FlatFileParseException.class)
                .skip(BatchSkipItemException.class)
                .skipLimit(Integer.MAX_VALUE)

                .listener(loggingSkipItemListener)
                .listener(loggingCountChunkListener)
                .build();
    }

    /**
     * プロジェクト一括登録バッチ/本テーブル登録のJobを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    public Job importProjectsJob() {
        return new JobBuilder("BA1060202", jobRepository)
                .start(importProjectsStep())
                .listener(loggingCountJobListener)
                .build();
    }
}
