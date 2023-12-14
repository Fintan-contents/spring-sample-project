package com.example.batch.common.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.batch.common.listener.LoggingCountChunkListener;
import com.example.batch.common.listener.LoggingCountJobListener;
import com.example.batch.common.listener.LoggingSkipItemListener;
import com.example.batch.common.mapper.BatchCommonMapper;
import com.example.common.util.BusinessDateSupplier;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 各バッチジョブのConfigで共通する実装をまとめたクラス。
 * <p>
 * 各バッチジョブのConfigは、このクラスを継承して作成する。
 * </p>
 */
public abstract class BatchBaseConfig {
    @Autowired
    protected JobRepository jobRepository;
    @Autowired
    protected PlatformTransactionManager platformTransactionManager;
    @Autowired
    protected SqlSessionFactory sqlSessionFactory;
    @Autowired
    protected BusinessDateSupplier businessDateSupplier;
    @Autowired
    protected LoggingSkipItemListener loggingSkipItemListener;
    @Autowired
    protected LoggingCountChunkListener loggingCountChunkListener;
    @Autowired
    protected LoggingCountJobListener loggingCountJobListener;
    @Autowired
    protected BatchCommonMapper batchCommonMapper;
}
