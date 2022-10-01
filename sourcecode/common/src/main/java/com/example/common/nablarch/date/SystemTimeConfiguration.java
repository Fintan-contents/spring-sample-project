package com.example.common.nablarch.date;

import org.springframework.context.annotation.Bean;

import nablarch.core.date.BasicSystemTimeProvider;
import nablarch.core.date.SystemTimeProvider;

/**
 * Nablarchのシステム日時管理機能を使用するための設定。
 * 
 * @author sample
 *
 */
public class SystemTimeConfiguration {

    /**
     * BasicSystemTimeProviderを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    public BasicSystemTimeProvider systemTimeProvider() {
        return new BasicSystemTimeProvider();
    }

    /**
     * SystemTimeSystemRepositoryLoaderを構築する。
     * 
     * @param systemTimeProvider SystemTimeProvider
     * @return 構築されたインスタンス
     */
    @Bean
    public SystemTimeSystemRepositoryLoader systemTimeSystemRepositoryLoader(
            SystemTimeProvider systemTimeProvider) {
        return new SystemTimeSystemRepositoryLoader(systemTimeProvider);
    }
}