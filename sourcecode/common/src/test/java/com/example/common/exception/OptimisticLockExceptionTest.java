package com.example.common.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OptimisticLockExceptionTest {

    @Test
    void test() {
        OptimisticLockException sut = new OptimisticLockException();
        assertNotNull(sut);
    }
}
