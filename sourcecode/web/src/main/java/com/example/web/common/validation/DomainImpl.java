package com.example.web.common.validation;

import java.lang.annotation.Annotation;

import jakarta.validation.Payload;

import nablarch.core.validation.ee.Domain;

/**
 * Domainアノテーションの実装クラス。
 * 
 * @author sample
 *
 */
public class DomainImpl implements Domain {

    private final String value;
    private final String message;
    private final Class<?>[] groups;
    private final Class<? extends Payload>[] payload;

    /**
     * コンストラクタ。
     * 
     * @param value ドメイン名
     * @param message メッセージ
     * @param groups グループ
     * @param payload ペイロード
     */
    public DomainImpl(String value, String message, Class<?>[] groups, Class<? extends Payload>[] payload) {
        this.value = value;
        this.message = message;
        this.groups = groups;
        this.payload = payload;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Domain.class;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public Class<?>[] groups() {
        return groups;
    }

    @Override
    public Class<? extends Payload>[] payload() {
        return payload;
    }

    @Override
    public String value() {
        return value;
    }
}
