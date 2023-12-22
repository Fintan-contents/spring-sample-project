package com.example.common.nablarch.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 金額範囲のバリデーションを行うクラス。
 *
 * @author sample
 */
@Documented
@Constraint(validatedBy = MoneyRangeValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface MoneyRange {

    /**
     * グループを取得する。
     *
     * @return グループ
     */
    Class<?>[] groups() default {};

    /**
     * バリデーションエラー発生時に設定するメッセージ。
     *
     * @return メッセージ
     */
    String message() default "{com.nablarch.example.app.entity.core.validation.validator.MoneyRange.message}";

    /**
     * Payloadを取得する。
     *
     * @return Payload
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 最小値を取得する。
     *
     * @return 最小値
     */
    long min() default 0;

    /**
     * 最大値を取得する。
     *
     * @return 最大値
     */
    long max() default Long.MAX_VALUE;

}
