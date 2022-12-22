package com.example.batch.project.configuration;

import com.example.batch.common.configuration.BatchBaseConfig;
import com.example.batch.project.tasklet.UpdateBusinessDateTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 業務日付更新バッチのConfig。
 */
@Configuration
@PropertySource(value = {
        "classpath:/properties/project/UpdateBusinessDate.properties",
        "classpath:/properties/project/UpdateBusinessDate-${spring.profiles.active}.properties",
}, encoding = "UTF-8", ignoreResourceNotFound = true)
public class UpdateBusinessDateConfig extends BatchBaseConfig {
    @Autowired
    private UpdateBusinessDateTasklet updateBusinessDateTasklet;

    /**
     * 業務日付更新バッチのPropertiesを構築する。
     *
     * @return 構築されたインスタンス
     */
    @Bean
    @ConfigurationProperties(prefix = "project.update-business-date")
    public UpdateBusinessDateProperties updateBusinessDateProperties() {
        return new UpdateBusinessDateProperties();
    }

    /**
     * 業務日付更新バッチのStepを構築する。
     *
     * @return 構築したされたインスタンス
     */
    @Bean
    public Step updateBusinessDateStep() {
        return stepBuilderFactory
                .get("BA1070101")
                .tasklet(updateBusinessDateTasklet)
                .build();
    }

    /**
     * 業務日付更新バッチのJobを構築する。
     *
     * @return 構築したされたインスタンス
     */
    @Bean
    public Job updateBusinessDateJob() {
        return jobBuilderFactory
                .get("BA1070101")
                .start(updateBusinessDateStep())
                .build();
    }
}
