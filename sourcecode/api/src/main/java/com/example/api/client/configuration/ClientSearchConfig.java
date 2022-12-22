package com.example.api.client.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 顧客検索の設定
 */
@Configuration
@PropertySource(value = "classpath:/properties/Client/ClientSearch.properties", encoding = "UTF-8")
public class ClientSearchConfig {

    /**
     * 顧客検索のPropertiesを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    @ConfigurationProperties(prefix = "client.client-search")
    public ClientSearchProperties clientSearchProperties() {
        return new ClientSearchProperties();
    }
}
