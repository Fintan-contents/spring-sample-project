package com.example.web.common.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * String配列に対してドメインバリデーションを行うためのアノテーション。
 * 
 * @author sample
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { ArrayDomainValidator.class })
public @interface ArrayDomain {

    /**
     * @return メッセージ
     */
    String message() default "{nablarch.core.validation.ee.Domain.message}";

    /**
     * @return グループ
     */
    Class<?>[] groups() default {};

    /**
     * @return ペイロード
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * @return ドメイン名
     */
    String value();

    /**
     * ArrayDomainを複数付けるためのアノテーション。
     * 
     * @author sample
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        /**
         * @return 複数のArrayDomain
         */
        ArrayDomain[] value();
    }
}
