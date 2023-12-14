package com.example.common.nablarch.validation;

import java.util.Map;

import jakarta.validation.Validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import nablarch.core.repository.SystemRepository;

/**
 * バリデーション機能に必要なインスタンスを{@link SystemRepository}へ登録するクラス。
 * {@link InitializingBean}を使用してSpringの初期化時に
 * {@link SystemRepository}への登録を行っている。
 * 
 * @author sample
 *
 */
public class ValidationSystemRepositoryLoader implements InitializingBean {

    /**
     * {@link Validator}実装クラスのインスタンス
     */
    private final LocalValidatorFactoryBean localValidatorFactoryBean;

    /**
     * コンストラクタ。
     * 
     * @param localValidatorFactoryBean {@link Validator}実装クラスのインスタンス
     */
    public ValidationSystemRepositoryLoader(LocalValidatorFactoryBean localValidatorFactoryBean) {
        this.localValidatorFactoryBean = localValidatorFactoryBean;
    }

    @Override
    public void afterPropertiesSet() {
        SystemRepository
                .load(() -> Map
                        .of(
                                "domainManager", new ExampleDomainManager(),
                                "validatorFactoryBuilder", new ValidatorFactoryBuilderImpl(localValidatorFactoryBean)));
    }
}
