package com.example.web.common.errorhandling;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.common.exception.ApplicationException;
import com.example.common.exception.OptimisticLockException;

/**
 * コントローラーで発生したエラーをハンドリングするためのアノテーション。
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnRejectError {

    /**
     * ハンドリング対象の例外クラス。
     * デフォルトは{@link ApplicationException}と{@link OptimisticLockException}。
     * 
     * @return ハンドリング対象の例外クラス
     */
    Class<? extends Exception>[] types() default {
            ApplicationException.class,
            OptimisticLockException.class
    };

    /**
     * 精査エラーもハンドリングするならtrueを設定する。
     * 精査エラーをコントローラー内で明示的にハンドリングするならfalseを設定する。
     * デフォルト値はtrue。
     * 
     * @return 精査エラーをハンドリングするかどうか
     */
    boolean handlingValidationError() default true;

    /**
     * エラー時に遷移するパス。
     * 
     * @return エラー時に遷移するパス
     */
    String path();
}
