package com.example.batch.common.configuration;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;

class BatchExitCodeGeneratorTest {

    StepExecution mockStepExecution1;
    StepExecution mockStepExecution2;
    JobExecution mockJobExecution;
    JobExecutionEvent event;
    BatchExitCodeGenerator sut = new BatchExitCodeGenerator();

    @BeforeEach
    void setUp() {
        mockJobExecution = MetaDataInstanceFactory.createJobExecution();
        mockStepExecution1 = mockJobExecution.createStepExecution("testStep1");
        mockStepExecution2 = mockJobExecution.createStepExecution("testStep2");
        event = new JobExecutionEvent(mockJobExecution);
    }

    @Test
    void testGetExitCodeWhenNotExistsSkipAndFailureException() {
        mockStepExecution1.setProcessSkipCount(0);
        mockStepExecution2.setProcessSkipCount(0);

        sut.onApplicationEvent(event);

        int actual = sut.getExitCode();

        assertThat(actual).isEqualTo(0);
    }

    @Test
    void testGetExitCodeWhenExistsSkipAndNotExistsFailureException() {
        mockStepExecution1.setProcessSkipCount(0);
        mockStepExecution2.setProcessSkipCount(3);

        sut.onApplicationEvent(event);

        int actual = sut.getExitCode();

        assertThat(actual).isEqualTo(2);
    }

    @Test
    void testGetExitCodeWhenExistsSkipAndFailureException() {
        mockStepExecution1.setProcessSkipCount(0);
        mockStepExecution2.setProcessSkipCount(3);
        mockStepExecution1.addFailureException(new Exception("test"));

        sut.onApplicationEvent(event);

        int actual = sut.getExitCode();

        assertThat(actual).isEqualTo(1);
    }

    @Test
    void testGetExitCodeWhenNotExistsSkipAndExistsFailureException() {
        mockStepExecution1.setProcessSkipCount(0);
        mockStepExecution2.setProcessSkipCount(0);
        mockStepExecution1.addFailureException(new Exception("test"));

        sut.onApplicationEvent(event);

        int actual = sut.getExitCode();

        assertThat(actual).isEqualTo(1);
    }
}