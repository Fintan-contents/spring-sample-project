package com.example.batch.archunit.selfcheck.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * {@link org.springframework.batch.core.configuration.annotation.StepScope}が
 * 付与されていないWriterクラス。
 *
 * @see com.example.batch.archunit.StepScopeAnnotationTest
 */
@Component
public class AnnotationLackingWriter implements ItemWriter<Object> {
    @Override
    public void write(List<?> items) throws Exception {

    }
}
