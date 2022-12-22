package com.example.web.project.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * プロジェクトアップロードのConfig。
 * 
 * @author sample
 */
@Configuration
@PropertySource(value = "classpath:/properties/project/ProjectUpload.properties", encoding = "UTF-8")
public class ProjectUploadConfig {

    /**
     * プロジェクトアップロードのPropertiesをBean定義する。
     * 
     * @return プロジェクトアップロードのProperties
     */
    @Bean
    @ConfigurationProperties(prefix = "project.project-upload")
    public ProjectUploadProperties projectUploadProperties() {
        return new ProjectUploadProperties();
    }
}
