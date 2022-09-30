package com.example.batch.archunit;

import static com.example.batch.archunit.ArchUnitConfiguration.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

public class StepScopeAnnotationTest {

    /**
     * reader,writer,processorパッケージ配下のクラスには
     * {@link org.springframework.batch.core.configuration.annotation.StepScope}が
     * 付与されていること。
     */
    @Test
    void testClassAnnotation() {
        classes()
                .that().resideInAnyPackage("..reader", "..writer", "..processor")
                .and().resideOutsideOfPackage("..common..")
                .should().beAnnotatedWith(StepScope.class)
                .check(appJavaClasses);
    }

    /**
     * Configクラスのメソッドで{@link Bean}が付与されており、
     * かつ戻り値の型が以下のいずれかである場合、そのメソッドに
     * {@link StepScope}が付与されていること。
     *
     * <ul>
     *     <li>{@link ItemReader}</li>
     *     <li>{@link ItemWriter}</li>
     *     <li>{@link ItemProcessor}</li>
     * </ul>
     */
    @Test
    void testConfigMethodStepScopeAnnotation() {
        classes()
                .that().resideInAPackage("..configuration")
                .should(haveValidStepScopeAnnotationOnBeanMethod)
                .check(appJavaClasses);
    }

    /** {@link Bean}が付与されたメソッドに適切な{@link StepScope}アノテーションが付与されていることを表す{@link ArchCondition}。 */
    static ArchCondition<JavaClass> haveValidStepScopeAnnotationOnBeanMethod = new ArchCondition<>("have no fields.") {

        @Override
        public void check(JavaClass item, ConditionEvents events) {
            for (JavaMethod method : item.getMethods()) {

                // @Beanの有無
                boolean hasBeanAnnotation = method
                        .getAnnotations().stream()
                        .anyMatch(annotation -> annotation.getRawType().isAssignableFrom(Bean.class));

                // @StepScopeの有無
                boolean hasStepScopeAnnotation = method
                        .getAnnotations().stream()
                        .anyMatch(annotation -> annotation.getRawType().isAssignableFrom(StepScope.class));

                JavaClass returnClass = method.getReturnType().toErasure();
                // @StepScopeの要否
                boolean shouldHaveStepScopeAnnotation = returnClass.isAssignableTo(ItemReader.class) ||
                        returnClass.isAssignableTo(ItemWriter.class) ||
                        returnClass.isAssignableTo(ItemProcessor.class);

                // @Beanがあるメソッドで@StepScopeを付与すべきであるのに付与されていないものをエラーとする
                if (hasBeanAnnotation && shouldHaveStepScopeAnnotation && !hasStepScopeAnnotation) {
                    events
                            .add(SimpleConditionEvent
                                    .violated(
                                            item, "should have @StepScope Annotation. (" + toJavaSourceLocation(method) + ")"));
                }
            }
        }

        private String toJavaSourceLocation(JavaMethod method) {
            return method.getOwner().getSimpleName() + ".java:" + method.getSourceCodeLocation().getLineNumber();
        }
    };
}
