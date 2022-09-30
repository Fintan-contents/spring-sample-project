package com.example.web.archunit;

import static com.example.web.archunit.ArchUnitConfiguration.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controllerに付与されたアノテーションが妥当であることを確認するテストクラス。
 *
 */
public class ControllerAnnotationTest {

    /**
     * Controllerには{@link Controller}アノテーションが付与されていること。
     */
    @Test
    public void testControllerAnnotation() {
        classes()
                .that().resideInAPackage("..controller")
                .should().beAnnotatedWith(Controller.class)
                .check(appJavaClasses);
    }

    /**
     * Ajax用Controllerには{@link RestController}アノテーションが付与されていること。
     */
    @Test
    public void testAjaxControllerAnnotation() {
        classes()
                .that().resideInAPackage("..ajax")
                .should().beAnnotatedWith(RestController.class)
                .allowEmptyShould(true) // 本サンプルではAjax用Controllerが存在しないため設定。Ajax用Controllerを追加した場合この行を削除すること。
                .check(appJavaClasses);
    }

}
