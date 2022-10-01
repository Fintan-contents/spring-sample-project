package com.example.batch.archunit.selfcheck.configuration;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.batch.common.configuration.BatchBaseConfig;

/**
 * {@link org.springframework.batch.core.configuration.annotation.StepScope}が
 * 適切に付与されていないConfigクラス。
 *
 * @see com.example.batch.archunit.StepScopeAnnotationTest
 */
@Configuration
public class AnnotationLackingConfig extends BatchBaseConfig {

    /**
     * 戻り値の型が{@link ItemReader}だがアノテーションが不足している。
     */
    @Bean
    public ItemReader<?> itemReader() {
        return null;
    }

    /**
     * 戻り値の型が{@link ItemWriter}だがアノテーションが不足している。
     */
    @Bean
    public ItemWriter<?> itemWriter() {
        return null;
    }

    /**
     * 戻り値の型が{@link ItemProcessor}だがアノテーションが不足している。
     */
    @Bean
    public ItemProcessor<?, ?> itemProcessor() {
        return null;
    }
}
