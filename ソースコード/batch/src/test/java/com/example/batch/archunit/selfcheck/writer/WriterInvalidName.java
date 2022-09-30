package com.example.batch.archunit.selfcheck.writer;

import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
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
    public void write(List<?> items) throws Exception {

    }
}
