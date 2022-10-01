package com.example.common.nablarch.code;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import nablarch.common.code.CodeUtil;

@SpringBootTest
public class CodeManagementTest {

    @Test
    void getName() {
        String name = CodeUtil.getName("C0100001", "01");
        assertEquals("農業", name);
    }
}
