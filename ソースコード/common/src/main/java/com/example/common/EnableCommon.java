package com.example.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * commonプロジェクト内をSpring管理するためのアノテーション。
 * 各処理方式のメインクラスに付けて使用する。
 * 
 * @author sample
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CommonConfigurationSelector.class)
public @interface EnableCommon {
}
