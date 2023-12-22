package com.example.batch.common.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.example.batch.core.jsr.JsrJobParametersConverter;

/**
 * バッチで共通のBeanを定義するConfig。
 */
@Configuration
public class BatchConfig {
    @Autowired
    private LocalValidatorFactoryBean localValidatorFactoryBean;
    @Autowired
    private DataSource dataSource;

    /**
     * BeanValidatingItemProcessorを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    @StepScope
    public BeanValidatingItemProcessor<?> beanValidatingItemProcessor() {
        return new BeanValidatingItemProcessor<>(localValidatorFactoryBean);
    }

    /**
     * JsrJobParametersConverterを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    public JobParametersConverter jobParametersConverter() {
        return new JsrJobParametersConverter(dataSource);
    }

    /**
     * BatchExitCodeGeneratorを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    public BatchExitCodeGenerator exitCodeGenerator() {
        return new BatchExitCodeGenerator();
    }
}
