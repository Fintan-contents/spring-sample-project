package com.example.batch.archunit.selfcheck.processor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class DummyProcessor implements ItemProcessor<Object, Object> {

    @Override
    public Object process(Object item) throws Exception {
        return null;
    }
}
