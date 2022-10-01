package com.example.common.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ApplicationExceptionTest {

    @Test
    void testFieldError() {
        ApplicationException sut = ApplicationException.fieldError("fooBar", "MSG001");
        assertEquals("fooBar", sut.getField());
        assertEquals("MSG001", sut.getCode());
        assertNull(sut.getCause());
    }

    @Test
    void testFieldErrorWithThrowable() {
        Exception t = new Exception();
        ApplicationException sut = ApplicationException.fieldError("bazQux", "MSG002", t);
        assertEquals("bazQux", sut.getField());
        assertEquals("MSG002", sut.getCode());
        assertEquals(t, sut.getCause());
    }

    @Test
    void testGlobalError() {
        ApplicationException sut = ApplicationException.globalError("MSG003");
        assertNull(sut.getField());
        assertEquals("MSG003", sut.getCode());
        assertNull(sut.getCause());
    }

    @Test
    void testGlobalErrorWithThrowable() {
        Exception t = new Exception();
        ApplicationException sut = ApplicationException.globalError("MSG004", t);
        assertNull(sut.getField());
        assertEquals("MSG004", sut.getCode());
        assertEquals(t, sut.getCause());
    }
}
