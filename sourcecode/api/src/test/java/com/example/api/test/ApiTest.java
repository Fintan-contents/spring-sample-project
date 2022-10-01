package com.example.api.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;

/**
 * APIのテストクラスに付けるアノテーション。
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TypeExcludeFilters(ArchUnitSelfCheckTypeExcludeFilter.class)
public @interface ApiTest {
}
