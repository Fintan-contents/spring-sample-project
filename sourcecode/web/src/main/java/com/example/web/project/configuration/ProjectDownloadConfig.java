package com.example.web.project.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * ユーザ別従事プロジェクトファイルダウンロードのConfig。
 * 
 * @author sample
 */
@Configuration
@PropertySource(value = "classpath:/properties/project/ProjectDownload.properties", encoding = "UTF-8")
public class ProjectDownloadConfig {

    /**
     * ユーザ別従事プロジェクトファイルダウンロードのPropertiesをBean定義する。
     * 
     * @return ユーザ別従事プロジェクトファイルダウンロードのProperties
     */
    @Bean
    @ConfigurationProperties(prefix = "project.project-download")
    public ProjectDownloadProperties projectDownloadProperties() {
        return new ProjectDownloadProperties();
    }
}
