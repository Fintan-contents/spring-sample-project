package com.example.web.common.errorhandling;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.common.exception.DataNotFoundException;
import com.example.web.common.errorhandling.ExceptionHandlersTest.TestController;
import com.example.web.test.WebTest;

import jp.fintan.keel.spring.web.token.transaction.InvalidTransactionTokenException;

@SpringBootTest
@AutoConfigureMockMvc
@WebTest
@Import(TestController.class)
class ExceptionHandlersTest {

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser
    void testHandleDataNotFoundException() throws Exception {
        mvc.perform(get("/DataNotFoundException"))
                .andExpectAll(
                        status().isNotFound(),
                        view().name("error/404"));
    }

    @Test
    @WithMockUser
    void testHandleInvalidTransactionTokenException() throws Exception {
        mvc.perform(get("/InvalidTransactionTokenException"))
                .andExpectAll(
                        status().isBadRequest(),
                        view().name("error/doubleSubmissionError"));
    }

    @TestComponent
    @Controller
    public static class TestController {

        @GetMapping("/DataNotFoundException")
        public Object throwDataNotFoundException() {
            throw new DataNotFoundException();
        }

        @GetMapping("/InvalidTransactionTokenException")
        public Object throwInvalidTransactionTokenException() {
            throw new InvalidTransactionTokenException();
        }
    }
}
