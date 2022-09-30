package com.example.common.nablarch.date;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import nablarch.core.date.BusinessDateProvider;
import nablarch.core.repository.SystemRepository;

/**
 * 業務日付管理機能に必要なインスタンスを{@link SystemRepository}へ登録するクラス。
 * {@link InitializingBean}を使用してSpringの初期化時に
 * {@link SystemRepository}への登録を行っている。
 * 
 * @author sample
 *
 */
public class BusinessDateSystemRepositoryLoader implements InitializingBean {

    /**
     * BusinessDateProvider
     */
    private final BusinessDateProvider businessDateProvider;

    /**
     * コンストラクタ。
     * 
     * @param businessDateProvider BusinessDateProvider
     */
    public BusinessDateSystemRepositoryLoader(BusinessDateProvider businessDateProvider) {
        this.businessDateProvider = businessDateProvider;
    }

    @Override
    public void afterPropertiesSet() {
        SystemRepository.load(() -> Map.of("businessDateProvider", businessDateProvider));
    }
}