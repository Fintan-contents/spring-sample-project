package com.example.batch.archunit.selfcheck.processor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * 命名規約に反したProcessorクラス。
 *
 * @see com.example.batch.archunit.NamingConventionTest
 */
@Component
@StepScope
public class ProcessorInvalidName implements ItemProcessor<Object, Object> {
    @Override
    public Object process(Object item) throws Exception {
        return null;
    }
}
