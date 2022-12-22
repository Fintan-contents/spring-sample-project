package com.example.web.archunit;

import static com.example.web.archunit.ArchUnitConfiguration.*;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.web.common.security.WebSecurityConfig;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.library.Architectures;

/**
 * レイヤードアーキテクチャに準拠していることをチェックするテストクラス。
 */
public class LayeredArchitectureTest {

    /**
     * レイヤー定義。
     *
     * 本サンプルではPropertiesが存在しないため{@link Architectures.LayeredArchitecture#withOptionalLayers(boolean)}を設定している。
     * この設定により、クラスがひとつも存在しないレイヤーが許容される。
     * Propertiesを追加した場合この設定を削除すること。
     */
    private Architectures.LayeredArchitecture layeredArchitecture = Architectures
            .layeredArchitecture()
            .consideringAllDependencies()
            .layer("Properties").definedBy("..configuration")
            .layer("Controller").definedBy("..controller")
            .layer("AjaxController").definedBy("..ajax")
            .layer("Service").definedBy("..service")
            .layer("Form").definedBy("..form")
            .layer("Request").definedBy("..request")
            .layer("Response").definedBy("..response")
            .layer("DTO").definedBy("..dto..") // dto配下はサブパッケージを使用する。
            .layer("ViewHelper").definedBy("..helper")
            .layer("Mapper").definedBy("..mapper")
            .layer("Model").definedBy("..model..") // model配下はサブパッケージを使用する。
            .withOptionalLayers(true); // 本サンプルではPropertiesが存在しないため設定。Propertiesを追加した場合この行を削除すること。

    /**
     * Propertiesの依存関係テスト。
     */
    @Test
    public void testPropertiesDependency() {
        layeredArchitecture
                .whereLayer("Properties")
                .mayOnlyBeAccessedByLayers("Service", "Controller", "AjaxController")
                .check(appJavaClasses);
    }

    /**
     * Controllerの依存関係テスト。
     */
    @Test
    void testControllerDependency() {
        layeredArchitecture
                .whereLayer("Controller")
                .mayNotBeAccessedByAnyLayer()
                .check(appJavaClasses);
    }

    /**
     * AjaxControllerの依存関係テスト。
     */
    @Test
    void testAjaxControllerDependency() {
        layeredArchitecture
                .whereLayer("AjaxController")
                .mayNotBeAccessedByAnyLayer()
                .check(appJavaClasses);
    }

    /**
     * Serviceの依存関係テスト。
     */
    @Test
    void testServiceDependency() {
        layeredArchitecture
                .whereLayer("Service")
                .mayOnlyBeAccessedByLayers("Controller", "AjaxController")
                // WebSecurityConfigは全体的な設定でありステレオタイプConfigとは異なるため、UserDetailsServiceを参照して良い
                .ignoreDependency(WebSecurityConfig.class, UserDetailsService.class)
                .check(appJavaClasses);
    }

    /**
     * Formの依存関係テスト。
     */
    @Test
    void testFormDependency() {
        layeredArchitecture
                .whereLayer("Form")
                .mayOnlyBeAccessedByLayers("Controller")
                .check(appJavaClasses);
    }

    /**
     * Requestの依存関係テスト。
     */
    @Test
    void testRequestDependency() {
        layeredArchitecture
                .whereLayer("Request")
                .mayOnlyBeAccessedByLayers("AjaxController")
                .check(appJavaClasses);
    }

    /**
     * Responseの依存関係テスト。
     */
    @Test
    void testResponseDependency() {
        layeredArchitecture
                .whereLayer("Response")
                .mayOnlyBeAccessedByLayers("AjaxController")
                .check(appJavaClasses);
    }

    /**
     * DTOの依存関係テスト。
     *
     * （Formは自身をDTOに変換する責務を持つのでDTOに依存してよい）
     */
    @Test
    void testDTODependency() {
        layeredArchitecture
                .whereLayer("DTO")
                .mayOnlyBeAccessedByLayers("Controller", "AjaxController", "Service", "ViewHelper", "Form", "Request", "Response")
                .check(appJavaClasses);
    }

    /**
     * ViewHelperの依存関係テスト。
     */
    @Test
    void testViewHelperDependency() {
        layeredArchitecture
                .whereLayer("ViewHelper")
                .mayNotBeAccessedByAnyLayer()
                .check(appJavaClasses);
    }

    /**
     * Mapperの依存関係テスト。
     */
    @Test
    void testMapperDependency() {
        layeredArchitecture
                .whereLayer("Mapper")
                .mayOnlyBeAccessedByLayers("Service", "ViewHelper")
                .check(appJavaClasses);
    }

    /**
     * Modelの依存関係テスト。
     *
     * 他のレイヤーからModelに依存してよいが、Modelが他のレイヤーに依存してはいけない。
     */
    @Test
    void testModelDependency() {
        layeredArchitecture
                .whereLayer("Model")
                .mayNotAccessAnyLayer()
                .ignoreDependency( // java.lang等への依存でエラーとさせないための除外設定
                        DescribedPredicate.alwaysTrue(),
                        JavaClass.Predicates.resideInAnyPackage("java.."))
                .ignoreDependency(
                        DescribedPredicate.alwaysTrue(),
                        JavaClass.Predicates.belongToAnyOf(Pageable.class))
                .check(appJavaClasses);
    }
}
