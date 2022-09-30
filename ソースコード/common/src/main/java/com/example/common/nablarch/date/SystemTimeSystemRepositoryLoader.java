package com.example.common.nablarch.date;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import nablarch.core.date.SystemTimeProvider;
import nablarch.core.repository.SystemRepository;

/**
 * システム日時管理機能に必要なインスタンスを{@link SystemRepository}へ登録するクラス。
 * {@link InitializingBean}を使用してSpringの初期化時に
 * {@link SystemRepository}への登録を行っている。
 * 
 * @author sample
 *
 */
public class SystemTimeSystemRepositoryLoader implements InitializingBean {

    /**
     * SystemTimeProvider
     */
    private final SystemTimeProvider systemTimeProvider;

    /**
     * コンストラクタ。
     * 
     * @param systemTimeProvider SystemTimeProvider
     */
    public SystemTimeSystemRepositoryLoader(SystemTimeProvider systemTimeProvider) {
        this.systemTimeProvider = systemTimeProvider;
    }

    @Override
    public void afterPropertiesSet() {
        SystemRepository.load(() -> Map.of("systemTimeProvider", systemTimeProvider));
    }
}