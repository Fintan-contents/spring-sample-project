package com.example.api.archunit;

import static com.example.api.archunit.ArchUnitConfiguration.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.Test;

/**
 * 命名規約をチェックするテストクラス。
 */
public class NamingConventionTest {

    /**
     * controllerパッケージにあるクラス名はxxxControllerであること。
     */
    @Test
    void testControllerNamingConvention() {
        classes()
                .that().resideInAPackage("..controller")
                .should().haveSimpleNameEndingWith("Controller")
                .check(appJavaClasses);
    }

    /**
     * dtoパッケージにあるクラス名はxxxDtoであること。
     */
    @Test
    void testDtoNamingConvention() {
        classes()
                .that().resideInAPackage("..dto..") // dto配下はサブパッケージを使用する。
                .should().haveSimpleNameEndingWith("Dto")
                .allowEmptyShould(true) // 本サンプルではDTOが存在しないため設定。DTOを追加した場合この行を削除すること。
                .check(appJavaClasses);
    }

    /**
     * requestパッケージにあるクラス名はxxxRequestであること。
     */
    @Test
    void testRequestNamingConvention() {
        classes()
                .that().resideInAPackage("..request")
                .should().haveSimpleNameEndingWith("Request")
                .check(appJavaClasses);
    }

    /**
     * responseパッケージにあるクラス名はxxxResponseであること。
     */
    @Test
    void testResponseNamingConvention() {
        classes()
                .that().resideInAPackage("..response")
                .should().haveSimpleNameEndingWith("Response")
                .check(appJavaClasses);
    }

    /**
     * mapperパッケージにあるクラス名はxxxMapperであること。
     */
    @Test
    void testMapperNamingConvention() {
        classes()
                .that().resideInAPackage("..mapper")
                .should().haveSimpleNameEndingWith("Mapper")
                .check(appJavaClasses);
    }

    /**
     * serviceパッケージにあるクラス名はxxxServiceであること。
     */
    @Test
    void testServiceNamingConvention() {
        classes()
                .that().resideInAPackage("..service")
                .should().haveSimpleNameEndingWith("Service")
                .check(appJavaClasses);
    }

    /**
     * configurationパッケージにあるクラス名はxxxPropertiesまたはxxxConfigであること。
     */
    @Test
    public void testPropertiesNamingConvention() {
        classes()
                .that().resideInAPackage("..configuration")
                .should().haveSimpleNameEndingWith("Properties")
                .orShould().haveSimpleNameEndingWith("Config")
                .check(appJavaClasses);
    }
}
