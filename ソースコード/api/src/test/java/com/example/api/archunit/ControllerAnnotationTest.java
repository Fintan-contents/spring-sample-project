package com.example.api.archunit;

import static com.example.api.archunit.ArchUnitConfiguration.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controllerに付与されたアノテーションが妥当であることを確認するテストクラス。
 */
public class ControllerAnnotationTest {

    /**
     * Controllerには{@link RestController}アノテーションが付与されていること。
     */
    @Test
    public void testControllerAnnotation() {
        classes()
                .that().resideInAPackage("..controller")
                .should().beAnnotatedWith(RestController.class)
                .check(appJavaClasses);
    }
}
