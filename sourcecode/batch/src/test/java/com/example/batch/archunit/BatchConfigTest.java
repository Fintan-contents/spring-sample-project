package com.example.batch.archunit;

import static com.example.batch.archunit.ArchUnitConfiguration.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.Test;

import com.example.batch.common.configuration.BatchBaseConfig;

/**
 * {@link BatchBaseConfig}のArchUnitテストクラス。
 */
public class BatchConfigTest {

    /**
     * Configクラスは、{@link BatchBaseConfig}を継承すること。
     */
    @Test
    void testBatchConfigSuperClass() {
        classes()
                .that().resideInAPackage("..configuration")
                .and().resideOutsideOfPackage("..common..") // common配下は除外
                .and().haveSimpleNameEndingWith("Config")
                .should().beAssignableTo(BatchBaseConfig.class)
                .check(appJavaClasses);

    }
}
