package com.example.web.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 顧客管理システムへのアクセスに関する設定をまとめたクラス。
 * 
 * @author sample
 *
 */
@Configuration
@ConfigurationProperties(prefix = "common")
public class ClientApiConfig {

    /** 顧客管理システムのURL */
    private String clientApiUrl;

    /**
     * 顧客管理システムへアクセスするためのRestTemplateを構築してBean登録する。
     * 
     * @param builder RestTemplateのビルダー
     * @return 顧客管理システムへアクセスするためのRestTemplate
     */
    @Bean
    public RestTemplate clientApiRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri(clientApiUrl).build();
    }

    /**
     * 顧客管理システムのURLを設定する。
     * 
     * @param clientApiUrl 顧客管理システムのURL
     */
    public void setClientApiUrl(String clientApiUrl) {
        this.clientApiUrl = clientApiUrl;
    }
}
