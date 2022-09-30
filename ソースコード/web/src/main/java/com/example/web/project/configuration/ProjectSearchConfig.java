package com.example.web.project.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * プロジェクト検索機能のConfig。
 * 
 * @author sample
 *
 */
@Configuration
@PropertySource("classpath:/properties/project/ProjectSearch.properties")
public class ProjectSearchConfig {

    /**
     * プロジェクト検索機能のPropertiesをBean定義する。
     * 
     * @return プロジェクト検索機能のProperties
     */
    @Bean
    @ConfigurationProperties(prefix = "project.project-search")
    public ProjectSearchProperties projectSearchProperties() {
        return new ProjectSearchProperties();
    }
}
