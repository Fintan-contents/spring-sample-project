package com.example.api.archunit;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.syntax.elements.GivenClasses;

/**
 * ArchUnitを実行する際に使用するメソッドを提供するユーティリティクラス。
 */
public class ArchUnitRuleUtil {

    /**
     * 指定されたクラスをテスト対象から除外する。
     *
     * {@link GivenClasses#that(DescribedPredicate)}や{@link DescribedPredicate#and(DescribedPredicate)}
     * と組み合わせて使用することで、任意のクラスをテスト対象外に指定できる。
     *
     * @param classes 対象外とするクラス（複数指定可）
     * @return 条件に当てはまらないクラスを表すPredicate
     */
    public static DescribedPredicate<JavaClass> notType(Class<?>... classes) {
        return DescribedPredicate.not(JavaClass.Predicates.belongToAnyOf(classes));
    }

}