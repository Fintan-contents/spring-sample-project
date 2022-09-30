package com.example.batch.archunit.selfcheck.writer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class DummyFieldExtractor implements FieldExtractor<Object> {
    @Override
    public Object[] extract(Object item) {
        return new Object[0];
    }
}
