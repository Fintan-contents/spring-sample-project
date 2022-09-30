package com.example.web.security.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * ログインのConfig。
 * 
 * @author sample
 *
 */
@Configuration
@PropertySource(value = "classpath:/properties/security/SecurityLogin.properties", encoding = "UTF-8")
public class SecurityLoginConfig {

    /**
     * Propertiesを定義する。
     * 
     * @return Propertiesのインスタンス
     */
    @Bean
    @ConfigurationProperties(prefix = "security.security-login")
    public SecurityLoginProperties securityLoginProperties() {
        return new SecurityLoginProperties();
    }
}
