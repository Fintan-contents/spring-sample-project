package com.example.web.common.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.web.test.WebTest;

@SpringBootTest
@WebTest
class CodeViewHelperTest {

    @Autowired
    CodeViewHelper sut;

    @Test
    void testGetName() {
        String actual = sut.getName("C0100001", "01");
        assertEquals("農業", actual);
    }

    @Test
    void testGetNameReturnNullIfValueIsNull() {
        String actual = sut.getName("C0100001", null);
        assertNull(actual);
    }

    @Test
    void testGetValues() {
        List<String> actual = sut.getValues("C0100001", "pattern01");
        assertEquals(List.of("01", "02", "03"), actual);
    }
}
