package com.example.api.archunit;

import static com.example.api.archunit.ArchUnitConfiguration.*;

import org.junit.jupiter.api.Test;

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
     * 本サンプルではDTOが存在しないため{@link Architectures.LayeredArchitecture#withOptionalLayers(boolean)}を設定している。
     * この設定により、クラスがひとつも存在しないレイヤーが許容される。
     * DTOを追加した場合この設定を削除すること。
     */
    private Architectures.LayeredArchitecture layeredArchitecture = Architectures
            .layeredArchitecture()
            .consideringAllDependencies()
            .layer("Properties").definedBy("..configuration")
            .layer("Controller").definedBy("..controller")
            .layer("Service").definedBy("..service")
            .layer("DTO").definedBy("..dto..") // dto配下はサブパッケージを使用する。
            .layer("Request").definedBy("..request")
            .layer("Response").definedBy("..response")
            .layer("Mapper").definedBy("..mapper")
            .layer("Model").definedBy("..model..") // model配下はサブパッケージを使用する。
            .withOptionalLayers(true); // 本サンプルではDTOが存在しないため設定。DTOを追加した場合この行を削除すること。

    /**
     * Propertiesの依存関係テスト。
     */
    @Test
    public void testPropertiesDependency() {
        layeredArchitecture
                .whereLayer("Properties")
                .mayOnlyBeAccessedByLayers("Service", "Controller")
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
     * Requestの依存関係テスト。
     */
    @Test
    void testRequestDependency() {
        layeredArchitecture
                .whereLayer("Request")
                .mayOnlyBeAccessedByLayers("Controller")
                .check(appJavaClasses);
    }

    /**
     * Responseの依存関係テスト。
     */
    @Test
    void testResponseDependency() {
        layeredArchitecture
                .whereLayer("Response")
                .mayOnlyBeAccessedByLayers("Controller")
                .check(appJavaClasses);
    }

    /**
     * Serviceの依存関係テスト。
     */
    @Test
    void testServiceDependency() {
        layeredArchitecture
                .whereLayer("Service")
                .mayOnlyBeAccessedByLayers("Controller")
                .check(appJavaClasses);
    }

    /**
     * DTOの依存関係テスト。
     */
    @Test
    void testDTODependency() {
        layeredArchitecture
                .whereLayer("DTO")
                .mayOnlyBeAccessedByLayers("Controller", "Service", "Request")
                .check(appJavaClasses);
    }

    /**
     * Mapperの依存関係テスト。
     */
    @Test
    void testMapperDependency() {
        layeredArchitecture
                .whereLayer("Mapper")
                .mayOnlyBeAccessedByLayers("Service")
                .check(appJavaClasses);
    }

    /**
     * Modelの依存関係テスト。
     */
    @Test
    void testModelDependency() {
        layeredArchitecture
                .whereLayer("Model")
                .mayNotAccessAnyLayer()
                .ignoreDependency( // java.lang等への依存でエラーとさせないための除外設定
                        DescribedPredicate.alwaysTrue(),
                        JavaClass.Predicates.resideInAnyPackage("java.."))
                .check(appJavaClasses);
    }
}
