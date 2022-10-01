package com.example.batch.archunit.selfcheck.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * 命名規約に反したListenerクラス。
 *
 * @see com.example.batch.archunit.NamingConventionTest
 */
public class ListenerInvalidName implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
