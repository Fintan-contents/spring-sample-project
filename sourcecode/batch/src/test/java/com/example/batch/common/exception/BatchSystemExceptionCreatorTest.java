package com.example.batch.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class BatchSystemExceptionCreatorTest {
    @MockBean
    MessageSource messageSource;
    @Autowired
    BatchSystemExceptionCreator sut;

    @Test
    void testCreate() {
        String messageId = "message-id";
        Object[] args = { 1, "foo", false };
        String message = "TEST-MESSAGE";

        when(messageSource.getMessage(eq(messageId), eq(args), eq(Locale.getDefault())))
                .thenReturn(message);

        BatchSystemException exception = sut.create(messageId, args);

        assertThat(exception).hasMessage(message);
    }
}