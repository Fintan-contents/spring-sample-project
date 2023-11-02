package com.example.batch.archunit.selfcheck.writer;


import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class DummyWriter implements ItemWriter<Object> {
    @Override
    public void write(Chunk<?> items) throws Exception {

    }
}
