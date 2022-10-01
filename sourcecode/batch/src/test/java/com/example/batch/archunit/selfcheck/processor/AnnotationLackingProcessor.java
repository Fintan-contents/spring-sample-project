package com.example.batch.archunit.selfcheck.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * {@link org.springframework.batch.core.configuration.annotation.StepScope}が
 * 付与されていないProcessorクラス。
 *
 * @see com.example.batch.archunit.StepScopeAnnotationTest
 */
@Component
public class AnnotationLackingProcessor implements ItemProcessor<Object, Object> {

    @Override
    public Object process(Object item) throws Exception {
        return null;
    }
}
