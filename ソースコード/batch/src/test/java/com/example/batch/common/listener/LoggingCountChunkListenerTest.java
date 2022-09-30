package com.example.batch.common.listener;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.test.MetaDataInstanceFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

class LoggingCountChunkListenerTest {
    Logger root;
    Appender<ILoggingEvent> mockAppender;
    LoggingCountChunkListener sut = new LoggingCountChunkListener();

    @BeforeEach
    void setUp() {
        root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        mockAppender = mock(Appender.class);
        root.addAppender(mockAppender);
    }

    @Test
    void testAfterJob() {
        StepExecution mockStepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContext mockStepContext = new StepContext(mockStepExecution);
        ChunkContext mockChunkContext = new ChunkContext(mockStepContext);

        mockStepExecution.setReadCount(20);
        mockStepExecution.setReadSkipCount(5);
        mockStepExecution.setProcessSkipCount(5);
        mockStepExecution.setCommitCount(3);

        sut.afterChunk(mockChunkContext);

        ArgumentCaptor<ILoggingEvent> captor = ArgumentCaptor.forClass(ILoggingEvent.class);
        verify(mockAppender).doAppend(captor.capture());

        ILoggingEvent event = captor.getValue();

        assertThat(event.getLevel()).isEqualTo(Level.INFO);
        assertThat(event.getFormattedMessage()).isEqualTo("入力件数=25, スキップ件数=10, コミット回数=3");
    }

    @AfterEach
    void tearDown() {
        root.detachAppender(mockAppender);
    }
}