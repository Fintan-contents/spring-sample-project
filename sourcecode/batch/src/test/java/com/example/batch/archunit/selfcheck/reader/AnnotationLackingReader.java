package com.example.batch.archunit.selfcheck.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

/**
 * {@link org.springframework.batch.core.configuration.annotation.StepScope}が
 * 付与されていないReaderクラス。
 *
 * @see com.example.batch.archunit.StepScopeAnnotationTest
 */
@Component
public class AnnotationLackingReader implements ItemReader<Object> {

    @Override
    public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }
}
