package com.example.batch.common.listener;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.example.batch.common.exception.BatchSkipItemException;
import com.example.batch.common.item.LineNumberItem;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

@SpringJUnitConfig(classes = { LoggingSkipItemListener.class })
public class LoggingSkipItemListenerTest {
    Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    Appender<ILoggingEvent> mockAppender = mock(Appender.class);
    BindingResult mockBindingResult = mock(BindingResult.class);
    BindException bindException = new BindException(mockBindingResult);
    ArgumentCaptor<ILoggingEvent> captor = ArgumentCaptor.forClass(ILoggingEvent.class);

    @Autowired
    LoggingSkipItemListener sut;
    @MockBean(name = "messageSource")
    MessageSource mockMessageSource;

    @BeforeEach
    void setup() {
        root.addAppender(mockAppender);

        FieldError mockFieldError = mock(FieldError.class);
        when(mockMessageSource.getMessage(eq(mockFieldError), eq(Locale.getDefault())))
                .thenReturn("field error 1", "field error 2");
        ObjectError mockObjectError = mock(ObjectError.class);
        when(mockMessageSource.getMessage(eq(mockObjectError), eq(Locale.getDefault())))
                .thenReturn("global error 1", "global error 2");

        when(mockBindingResult.getAllErrors())
                .thenReturn(List.of(mockObjectError, mockObjectError, mockFieldError, mockFieldError));
    }

    @Test
    void testOnSkipInReadWhenBindingResultException() {
        int lineNumber = 14;
        FlatFileParseException flatFileParseException = new FlatFileParseException("test", bindException, "input", lineNumber);

        sut.onSkipInRead(flatFileParseException);

        verify(mockAppender, atLeastOnce()).doAppend(captor.capture());

        ILoggingEvent event = captor.getValue();

        assertThat(event.getLevel()).isEqualTo(Level.WARN);
        assertThat(event.getFormattedMessage())
                .isEqualTo("スキップしました 行番号=14, エラー内容=global error 1, global error 2, field error 1, field error 2");
    }

    @Test
    void testOnSkipInProcessWhenBindingResultException() {
        ValidationException validationException = new ValidationException("test", bindException);

        LineNumberItem lineNumberItem = mock(LineNumberItem.class);
        when(lineNumberItem.getLineNumber()).thenReturn(13);

        sut.onSkipInProcess(lineNumberItem, validationException);

        verify(mockAppender, atLeastOnce()).doAppend(captor.capture());

        ILoggingEvent event = captor.getValue();

        assertThat(event.getLevel()).isEqualTo(Level.WARN);
        assertThat(event.getFormattedMessage())
                .isEqualTo("スキップしました 行番号=13, エラー内容=global error 1, global error 2, field error 1, field error 2");
    }

    @Test
    void testOnSkipInProcessWhenBindingResultExceptionButItemIsNotLineNumberItem() {
        ValidationException validationException = new ValidationException("test", bindException);

        Object item = new Object();

        sut.onSkipInProcess(item, validationException);

        verify(mockAppender, atLeastOnce()).doAppend(captor.capture());

        ILoggingEvent event = captor.getValue();

        assertThat(event.getLevel()).isEqualTo(Level.WARN);
        assertThat(event.getFormattedMessage())
                .isEqualTo("スキップしました");
    }

    @Test
    void testOnSkipInProcessWhenLineNumberItemButNotBindingResultException() {
        ValidationException validationException = new ValidationException("test", new Exception());

        LineNumberItem lineNumberItem = mock(LineNumberItem.class);
        when(lineNumberItem.getLineNumber()).thenReturn(13);

        sut.onSkipInProcess(lineNumberItem, validationException);

        verify(mockAppender, atLeastOnce()).doAppend(captor.capture());

        ILoggingEvent event = captor.getValue();

        assertThat(event.getLevel()).isEqualTo(Level.WARN);
        assertThat(event.getFormattedMessage())
                .isEqualTo("スキップしました");
    }

    @Test
    void testOnSkipInReadWhenOtherException() {
        sut.onSkipInRead(new Exception("other exception", bindException));

        verify(mockAppender, atLeastOnce()).doAppend(captor.capture());

        ILoggingEvent event = captor.getValue();

        assertThat(event.getLevel()).isEqualTo(Level.WARN);
        assertThat(event.getFormattedMessage()).isEqualTo("スキップしました");
        assertThat(event.getThrowableProxy().getClassName()).isEqualTo(Exception.class.getName());
        assertThat(event.getThrowableProxy().getMessage()).isEqualTo("other exception");
    }

    @Test
    void testOnSkipInProcessWhenBatchSkipItemException() {
        String code = "aaa";
        Object[] args = { "a", "b" };
        when(mockMessageSource.getMessage(eq(code), eq(args), eq(Locale.getDefault())))
                .thenReturn("test message");

        sut.onSkipInProcess(null, new BatchSkipItemException(code, args));

        verify(mockAppender, atLeastOnce()).doAppend(captor.capture());

        ILoggingEvent event = captor.getValue();

        assertThat(event.getLevel()).isEqualTo(Level.WARN);
        assertThat(event.getFormattedMessage()).isEqualTo("スキップしました エラー内容=test message");
    }

    @Test
    void testOnSkipInProcessWhenOtherException() {
        sut.onSkipInProcess(null, new Exception("other exception", bindException));

        verify(mockAppender, atLeastOnce()).doAppend(captor.capture());

        ILoggingEvent event = captor.getValue();

        assertThat(event.getLevel()).isEqualTo(Level.WARN);
        assertThat(event.getFormattedMessage()).isEqualTo("スキップしました");
        assertThat(event.getThrowableProxy().getClassName()).isEqualTo(Exception.class.getName());
        assertThat(event.getThrowableProxy().getMessage()).isEqualTo("other exception");
    }

    @AfterEach
    void tearDown() {
        root.detachAppender(mockAppender);
    }
}
