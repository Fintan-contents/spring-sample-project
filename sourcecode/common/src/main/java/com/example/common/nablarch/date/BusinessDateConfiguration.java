package com.example.common.nablarch.date;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.example.common.nablarch.db.DbAccessConfiguration;

import nablarch.core.date.BasicBusinessDateProvider;
import nablarch.core.date.BusinessDateProvider;
import nablarch.core.db.transaction.SimpleDbTransactionManager;

/**
 * Nablarchの業務日付管理機能を使用するための設定。
 * 
 * @author sample
 *
 */
@Import(DbAccessConfiguration.class)
public class BusinessDateConfiguration {

    /**
     * BasicBusinessDateProviderを構築する。
     * 
     * @param transactionManager SimpleDbTransactionManager
     * @return 構築されたインスタンス
     */
    @Bean(initMethod = "initialize")
    @ConfigurationProperties(prefix = "nablarch.business-date")
    public BasicBusinessDateProvider businessDateProvider(
            SimpleDbTransactionManager transactionManager) {
        BasicBusinessDateProvider businessDateProvider = new BasicBusinessDateProvider();
        businessDateProvider.setTableName("business_date");
        businessDateProvider.setSegmentColumnName("segment_id");
        businessDateProvider.setDateColumnName("biz_date");
        businessDateProvider.setDefaultSegment("00");
        businessDateProvider.setDbTransactionManager(transactionManager);
        return businessDateProvider;
    }

    /**
     * BusinessDateSystemRepositoryLoaderを構築する。
     * 
     * @param businessDateProvider BusinessDateProvider
     * @return 構築されたインスタンス
     */
    @Bean
    public BusinessDateSystemRepositoryLoader businessDateSystemRepositoryLoader(
            BusinessDateProvider businessDateProvider) {
        return new BusinessDateSystemRepositoryLoader(businessDateProvider);
    }
}