package com.example.api.common.security;

import static org.springframework.security.config.Customizer.*;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Webセキュリティ設定
 */
@Configuration
public class WebSecurityConfig {
    /**
     * セキュリティ設定
     * 
     * CSRF対策の無効化とCORS設定を行う。
     * 
     * @param http HTTPセキュリティ設定
     * @return セキュリティ設定フィルターチェイン
     * @throws Exception 例外
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(c -> c.disable())
                .cors(withDefaults())
                .headers(c -> c
                        // Referrer-Policyレスポンスヘッダの設定
                        // https://developer.mozilla.org/ja/docs/Web/HTTP/Headers/Referrer-Policy
                        .referrerPolicy(policy -> policy
                                .policy(ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)))
                .build();
    }

    /**
     * CORS設定値
     * 
     * @return propertiesファイルに設定された値をバインドしたCORS設定
     */
    @Bean
    @ConfigurationProperties(prefix = "cors")
    public CorsConfiguration corsConfiguration() {
        return new CorsConfiguration();
    }

    /**
     * CORS設定
     * 
     * @param corsConfiguration CORS設定値
     * @return CORS設定
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsConfiguration corsConfiguration) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /**
     * 暗黙的なデフォルトユーザーの作成を防止するため、空のInMemoryUserDetailsManagerを準備する。
     * 
     * @return 空のInMemoryUserDetailsManager
     */
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager();
    }
}
