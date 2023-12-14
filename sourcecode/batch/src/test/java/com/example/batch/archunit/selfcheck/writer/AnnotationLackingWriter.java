package com.example.batch.archunit.selfcheck.writer;


import org.springframework.batch.item.Chunk;
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
    public void write(Chunk<?> items) throws Exception {

    }
}
