package com.example.web.archunit;

import static com.example.web.archunit.ArchUnitConfiguration.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.properties.HasAnnotations;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * トランザクション境界の指定をチェックするテストクラス。
 */
public class TransactionBoundaryTest {

    /**
     * トランザクション境界の指定がService、ViewHelperクラスで行われていること（クラス）。
     */
    @Test
    void testClassTransactionalAnnotation() {
        classes()
                .that().areAnnotatedWith(Transactional.class)
                .should().resideInAnyPackage("..service", "..helper")
                .because("トランザクション境界はServiceまたはViewHelperで定義されるべきです。")
                .check(appJavaClasses);
    }

    /**
     * トランザクション境界の指定がService、ViewHelperクラスで行われていること（メソッド）。
     */
    @Test
    void testMethodTransactionalAnnotation() {
        methods()
                .that().areAnnotatedWith(Transactional.class)
                .should().beDeclaredInClassesThat().resideInAnyPackage("..service", "..helper")
                .because("トランザクション境界はServiceで定義されるべきです。")
                .check(appJavaClasses);
    }

    /**
     * ViewHelperではトランザクションが読み取り専用になっていること（クラス）。
     */
    @Test
    void testTransactionalAnnotationInViewHelperClass() {
        classes()
                .that().resideInAPackage("..helper")
                .and().areAnnotatedWith(Transactional.class)
                .should(haveReadOnlyTransaction)
                .because("ViewHelperでは、トランザクションは読み取り専用でなければなりません。")
                .check(appJavaClasses);
    }

    /**
     * ViewHelperではトランザクションが読み取り専用になっていること（メソッド）。
     */
    @Test
    public void testTransactionalAnnotationInViewHelperMethod() {
        methods()
                .that().areAnnotatedWith(Transactional.class)
                .and().areDeclaredInClassesThat().resideInAnyPackage("..helper")
                .should(haveReadOnlyTransactionMethod)
                .allowEmptyShould(true) // 本サンプルにはメソッドに@Transactionalを付与するケースがないのでtrueを設定
                .because("ViewHelperでは、トランザクションは読み取り専用でなければなりません。")
                .check(appJavaClasses);

    }

    /**
     * クラスに付与された@Transactionalアノテーションが読み取り専用であることを表す{@link ArchCondition}。
     */
    static ArchCondition<JavaClass> haveReadOnlyTransaction = new HaveReadOnlyTransaction<>();

    /**
     * メソッドに付与された@Transactionalアノテーションが読み取り専用であることを表す{@link ArchCondition}。
     */
    static ArchCondition<JavaMethod> haveReadOnlyTransactionMethod = new HaveReadOnlyTransaction<>();

    /**
     * {@link Transactional}アノテーションが読み取り専用であることを表す{@link ArchCondition}サブクラス。
     */
    static class HaveReadOnlyTransaction<T extends HasAnnotations<?>> extends ArchCondition<T> {

        public HaveReadOnlyTransaction() {
            super("have readOnly attribute and it's value should be true.");
        }

        @Override
        public void check(T item, ConditionEvents events) {
            for (JavaAnnotation<?> annotation : item.getAnnotations()) {
                if (annotation.getRawType().isAssignableFrom(Transactional.class)) {
                    if (!annotation.hasExplicitlyDeclaredProperty("readOnly")) {
                        // readOnly属性が付与されていない場合
                        events
                                .add(SimpleConditionEvent
                                        .violated(
                                                item, "readOnly attribute must be set. (" + toJavaSourceLocation(item) + ")"));
                    } else if (!annotation.getExplicitlyDeclaredProperty("readOnly").equals(true)) {
                        // readOnly属性がtrueでない場合（読み取り専用でない場合）
                        events
                                .add(SimpleConditionEvent
                                        .violated(
                                                item, "readOnly attribute must be true. (" + toJavaSourceLocation(item) + ")"));
                    }
                }
            }
        }

        private String toJavaSourceLocation(HasAnnotations<?> item) {
            if (item instanceof JavaClass) {
                JavaClass javaClass = (JavaClass) item;
                return javaClass.getSimpleName() + ".java:" + javaClass.getSourceCodeLocation().getLineNumber();
            } else if (item instanceof JavaMethod) {
                JavaMethod javaMethod = (JavaMethod) item;
                return javaMethod.getOwner().getSimpleName() + ".java:" + javaMethod.getSourceCodeLocation().getLineNumber();
            } else {
                throw new IllegalArgumentException(item.getClass().toString());
            }
        }
    }

}
