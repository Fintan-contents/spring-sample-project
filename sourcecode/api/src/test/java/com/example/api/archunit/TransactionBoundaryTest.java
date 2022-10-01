package com.example.api.archunit;

import static com.example.api.archunit.ArchUnitConfiguration.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * トランザクション境界の指定をチェックするテストクラス。
 */
public class TransactionBoundaryTest {

    /**
     * トランザクション境界の指定がServiceクラスで行われていること。
     */
    @Test
    void testClassTransactionalAnnotation() {
        classes()
                .that().areAnnotatedWith(Transactional.class)
                .should().resideInAnyPackage("..service")
                .because("トランザクション境界はServiceで定義されるべきです。")
                .check(appJavaClasses);
    }

    @Test
    void testMethodTransactionalAnnotation() {
        methods()
                .that().areAnnotatedWith(Transactional.class)
                .should().beDeclaredInClassesThat().resideInAPackage("..service")
                .because("トランザクション境界はServiceで定義されるべきです。")
                .check(appJavaClasses);
    }

}
