package com.example.batch.project.configuration;

import com.example.batch.common.configuration.BatchBaseConfig;
import com.example.batch.project.item.CreateUsersProjectsItem;
import com.example.batch.project.listener.CreateUserProjectsStepExecutionListener;
import com.example.batch.project.mapper.CreateUsersProjectsMapper;
import com.example.batch.project.processor.CreateUsersProjectsItemProcessor;
import com.example.batch.project.writer.CreateUsersProjectsFieldExtractor;
import com.example.common.generated.model.Project;
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

import java.nio.file.Path;
import java.util.Map;

/**
 * ユーザ別従事プロジェクト抽出バッチのConfig。
 */
@Configuration
@PropertySource(value = {
        "classpath:/properties/project/CreateUsersProjects.properties",
        "classpath:/properties/project/CreateUsersProjects-${spring.profiles.active}.properties",
}, encoding = "UTF-8", ignoreResourceNotFound = true)
public class CreateUsersProjectsConfig extends BatchBaseConfig {
    @Autowired
    private CreateUsersProjectsItemProcessor createUsersProjectsItemProcessor;
    @Autowired
    private CreateUsersProjectsFieldExtractor createUsersProjectsFieldExtractor;
    @Autowired
    private CreateUserProjectsStepExecutionListener createUserProjectsStepExecutionListener;

    /**
     * ユーザ別従事プロジェクト抽出バッチのPropertiesを構築する。
     *
     * @return 構築されたインスタンス
     */
    @Bean
    @ConfigurationProperties(prefix = "project.create-users-projects")
    public CreateUsersProjectsProperties createUsersProjectsProperties() {
        return new CreateUsersProjectsProperties();
    }


    /**
     * ユーザ別従事プロジェクト抽出バッチのItemReaderを構築する。
     *
     * @param requestId 要求ID
     * @return 構築されたインスタンス
     */
    @Bean
    @StepScope
    public MyBatisCursorItemReader<Project> createUsersProjectsItemReader(
            @Value("#{jobParameters['request.id']}") long requestId
    ) {
        return new MyBatisCursorItemReaderBuilder<Project>()
                .sqlSessionFactory(sqlSessionFactory)
                .parameterValues(Map.of("requestId", requestId))
                .queryId(CreateUsersProjectsMapper.class.getName() + ".selectProjectsByRequestId")
                .build();
    }

    /**
     * ユーザ別従事プロジェクト抽出バッチのItemWriterを構築する。
     *
     * @param requestId 要求ID
     * @return 構築されたインスタンス
     */
    @Bean
    @StepScope
    public FlatFileItemWriter<CreateUsersProjectsItem> createUsersProjectsItemWriter(
            @Value("#{jobParameters['request.id']}") long requestId
    ) {
        CreateUsersProjectsProperties properties = createUsersProjectsProperties();
        String fileName = properties.getOutputFileNamePrefix() + requestId + ".csv";
        Path outputFilePath = properties.getOutputDirPath().resolve(fileName);

        return new FlatFileItemWriterBuilder<CreateUsersProjectsItem>()
                .encoding("UTF-8")
                .saveState(false)
                .name("BA1060301")
                .lineSeparator("\r\n")
                .resource(new PathResource(outputFilePath))
                .shouldDeleteIfEmpty(false)
                .delimited()
                .delimiter(",")
                .fieldExtractor(createUsersProjectsFieldExtractor)
                .build();
    }

    /**
     * ユーザ別従事プロジェクト抽出バッチのStepを構築する。
     *
     * @return 構築されたインスタンス
     */
    @Bean
    public Step createUsersProjectsStep() {
        int chunkSize = createUsersProjectsProperties().getChunkSize();

        return new StepBuilder("BA1060301", jobRepository)
                .<Project, CreateUsersProjectsItem>chunk(chunkSize, platformTransactionManager)
                .reader(createUsersProjectsItemReader(0))
                .processor(createUsersProjectsItemProcessor)
                .writer(createUsersProjectsItemWriter(0))
                .listener(loggingCountChunkListener)
                .listener(createUserProjectsStepExecutionListener)
                .build();
    }

    /**
     * ユーザ別従事プロジェクト抽出バッチのJobを構築する。
     *
     * @return 構築されたインスタンス
     */
    @Bean
    public Job createUsersProjectsJob() {
        return new JobBuilder("BA1060301", jobRepository)
                .start(createUsersProjectsStep())
                .listener(loggingCountJobListener)
                .build();
    }
}
