package com.example.web.common.errorhandling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.DefaultErrorViewResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link DefaultErrorViewResolver}をコンポーネント登録するための設定クラス。
 * 
 * @author sample
 *
 */
@Configuration
public class DefaultErrorViewResolverConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    @Autowired
    private WebProperties webProperties;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * DefaultErrorViewResolverを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    public DefaultErrorViewResolver defaultErrorViewResolver() {
        return new DefaultErrorViewResolver(applicationContext, webProperties.getResources());
    }
}