package com.example.api.common;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.common.GlobalExceptionHandlerTest.TestController;
import com.example.api.common.exception.BusinessException;
import com.example.api.test.ApiTest;
import com.example.api.test.RestControllerTestBase;
import com.example.common.exception.OptimisticLockException;
import com.fasterxml.jackson.annotation.JsonFormat;

import nablarch.core.validation.ee.Domain;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ApiTest
@Import(TestController.class)
class GlobalExceptionHandlerTest extends RestControllerTestBase {

    @Test
    void testHandleBindException() {
        TestRequest request = new TestRequest();
        request.setClientName("invalid value");
        ResponseEntity<String> response = http.postForEntity("/BindException", request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson("GlobalExceptionHandlerTest/testHandleBindException/expected.json");
    }

    @Test
    void testHandleMethodArgumentTypeMismatchException() {
        ResponseEntity<String> response = http.getForEntity("/MethodArgumentTypeMismatchException/xxx", String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson("GlobalExceptionHandlerTest/testHandleMethodArgumentTypeMismatchException/expected.json");
    }

    @Test
    void testHandleHttpMessageNotReadableException() {
        Map<String, String> request = Map.of("date", "20220931");
        ResponseEntity<String> response = http.postForEntity("/HttpMessageNotReadableException", request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson("GlobalExceptionHandlerTest/testHandleHttpMessageNotReadableException/expected.json");
    }

    @Test
    void testHandleBusinessException() {
        ResponseEntity<String> response = http.getForEntity("/BusinessException", String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson("GlobalExceptionHandlerTest/testHandleBusinessException/expected.json");
    }

    @Test
    void testHandleDuplicateKeyException() {
        ResponseEntity<String> response = http.getForEntity("/DuplicateKeyException", String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);
        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson("GlobalExceptionHandlerTest/testHandleDuplicateKeyException/expected.json");
    }

    @Test
    void testHandleOptimisticLockException() {
        ResponseEntity<String> response = http.getForEntity("/OptimisticLockException", String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);
        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson("GlobalExceptionHandlerTest/testHandleOptimisticLockException/expected.json");
    }

    @TestComponent
    @RestController
    public static class TestController {
        @PostMapping("/BindException")
        public void throwBindException(@RequestBody @Validated TestRequest request) {
        }

        @GetMapping("/MethodArgumentTypeMismatchException/{id}")
        public void throwMethodArgumentTypeMismatchException(@PathVariable Integer id) {
        }

        @PostMapping("/HttpMessageNotReadableException")
        public void throwHttpMessageNotReadableException(@RequestBody TestDateRequest request) {
        }

        @GetMapping("/BusinessException")
        public Object throwBusinessException() {
            throw new BusinessException("FX9999999", HttpStatus.BAD_REQUEST, "errors.nothing");
        }

        @GetMapping("/DuplicateKeyException")
        public Object throwDuplicateKeyException() {
            throw new DuplicateKeyException("...");
        }

        @GetMapping("/OptimisticLockException")
        public Object throwOptimisticLockException() {
            throw new OptimisticLockException();
        }
    }

    public static class TestRequest {

        @Domain("clientName")
        private String clientName;

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }
    }

    public static class TestDateRequest {

        @JsonFormat(pattern = "yyyy/MM/dd")
        private LocalDate date;

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }
    }
}
