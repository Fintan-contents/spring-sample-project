package com.example.common.nablarch.code;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import nablarch.common.code.CodeManager;
import nablarch.core.repository.SystemRepository;

/**
 * コード管理機能に必要なインスタンスを{@link SystemRepository}へ登録するクラス。
 * {@link InitializingBean}を使用してSpringの初期化時に
 * {@link SystemRepository}への登録を行っている。
 * 
 * @author sample
 *
 */
public class CodeManagementSystemRepositoryLoader implements InitializingBean {

    /**
     * CodeManager
     */
    private final CodeManager codeManager;

    /**
     * コンストラクタ。
     * 
     * @param codeManager CodeManager
     */
    public CodeManagementSystemRepositoryLoader(CodeManager codeManager) {
        this.codeManager = codeManager;
    }

    @Override
    public void afterPropertiesSet() {
        SystemRepository
                .load(() -> Map
                        .of(
                                "codeManager", codeManager));
    }
}