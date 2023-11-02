package com.example.common.nablarch.validation;

import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.ParameterNameProvider;
import jakarta.validation.TraversableResolver;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorContext;
import jakarta.validation.ValidatorFactory;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import nablarch.core.validation.ee.ValidatorFactoryBuilder;

/**
 * 特定の{@link Validator}実装クラスを返す{@link ValidatorFactoryBuilder}サブクラス。
 *
 * @author sample
 *
 */
public class ValidatorFactoryBuilderImpl extends ValidatorFactoryBuilder implements ValidatorFactory {

    /**
     * {@link Validator}実装クラスのインスタンス
     */
    private final LocalValidatorFactoryBean localValidatorFactoryBean;

    /**
     * コンストラクタ。
     *
     * @param localValidatorFactoryBean {@link Validator}実装クラスのインスタンス
     */
    public ValidatorFactoryBuilderImpl(LocalValidatorFactoryBean localValidatorFactoryBean) {
        this.localValidatorFactoryBean = localValidatorFactoryBean;
    }

    @Override
    protected ValidatorFactory build() {
        return this;
    }

    @Override
    public Validator getValidator() {
        return localValidatorFactoryBean;
    }

    @Override
    public ValidatorContext usingContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MessageInterpolator getMessageInterpolator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TraversableResolver getTraversableResolver() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConstraintValidatorFactory getConstraintValidatorFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParameterNameProvider getParameterNameProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClockProvider getClockProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
    }
}
