package com.example.common.nablarch.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 指定された整数の範囲の金額であることを検証するバリデータ。
 *
 * @author sample
 */
public class MoneyRangeValidator implements ConstraintValidator<MoneyRange, Integer> {

    /**
     * 最小値
     */
    private long min;

    /**
     * 最大値
     */
    private long max;

    /**
     * 最小値・最大値を初期化する。
     *
     * @param constraintAnnotation 対象プロパティに付与されたアノテーション
     */
    @Override
    public void initialize(MoneyRange constraintAnnotation) {
        max = constraintAnnotation.max();
        min = constraintAnnotation.min();
    }

    /**
     * 対象の値が{@code min}～{@code max}で指定する範囲内であるか検証する。
     *
     * @param value   対象の値
     * @param context バリデーションコンテキスト
     * @return 範囲内である場合{@code true}
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value >= min && value <= max;
    }
}
