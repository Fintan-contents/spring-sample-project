package com.example.common.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DataNotFoundExceptionTest {

    @Test
    void test() {
        DataNotFoundException sut = new DataNotFoundException();
        assertNotNull(sut);
    }
}
