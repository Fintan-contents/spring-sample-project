package com.example.web.archunit;

import static com.example.web.archunit.ArchUnitConfiguration.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.Test;

/**
 * 命名規約をチェックするテストクラス。
 */
public class NamingConventionTest {

    /**
     * controllerパッケージ、ajaxパッケージにあるクラス名はxxxControllerであること。
     */
    @Test
    void testControllerNamingConvention() {
        classes()
                .that().resideInAnyPackage("..controller", "..ajax")
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
                .check(appJavaClasses);
    }

    /**
     * formパッケージにあるクラス名はxxxFormであること。
     */
    @Test
    void testFormNamingConvention() {
        classes()
                .that().resideInAPackage("..form")
                .and().areTopLevelClasses()
                .should().haveSimpleNameEndingWith("Form")
                .check(appJavaClasses);
    }

    /**
     * requestパッケージにあるクラス名はxxxRequestであること。
     */
    @Test
    void testRequestNamingConvention() {
        classes()
                .that().resideInAPackage("..request")
                .and().areTopLevelClasses()
                .should().haveSimpleNameEndingWith("Request")
                .allowEmptyShould(true) // サンプルにはRequestがないため設定しておく
                .check(appJavaClasses);
    }

    /**
     * responseパッケージにあるクラス名はxxxResponseであること。
     */
    @Test
    void testResponseNamingConvention() {
        classes()
                .that().resideInAPackage("..response")
                .and().areTopLevelClasses()
                .should().haveSimpleNameEndingWith("Response")
                .allowEmptyShould(true) // サンプルにはResponseがないため設定しておく
                .check(appJavaClasses);
    }

    /**
     * helperパッケージにあるクラス名はxxxViewHelperであること。
     */
    @Test
    void testViewHelperNamingConvention() {
        classes()
                .that().resideInAPackage("..helper")
                .should().haveSimpleNameEndingWith("ViewHelper")
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
     * configurationパッケージにあるクラス名はxxxPropertiesであること。
     */
    @Test
    public void testPropertiesNamingConvention() {
        classes()
                .that().resideInAPackage("..configuration")
                .should().haveSimpleNameEndingWith("Properties")
                .orShould().haveSimpleNameEndingWith("Config")
                .allowEmptyShould(true)
                .check(appJavaClasses);
    }
}
