package com.example.batch.archunit.selfcheck.writer;


import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * 命名規約に反したWriterクラス。
 *
 * @see com.example.batch.archunit.NamingConventionTest
 */
@Component
@StepScope
public class WriterInvalidName implements ItemWriter<Object> {
    @Override
    public void write(Chunk<?> items) throws Exception {

    }
}
