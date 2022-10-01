package com.example.api.common.error;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.api.test.ApiTest;
import com.example.api.test.TestController;

/**
 * {@link DefaultErrorController}のテスト。
 * テストの補助として{@link TestController}を使用している。
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ApiTest
public class DefaultErrorControllerTest {

    @Autowired
    TestRestTemplate http;

    /**
     * {@link ExceptionHandler}でハンドリングされない例外がスローされた場合。
     * 
     */
    @Test
    void testUnhandleException() {
        ResponseEntity<Void> response = http.getForEntity("/tests/unhandle-exception", Void.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.hasBody());
    }

    /**
     * 存在しないパスにアクセスした場合。
     * 
     */
    @Test
    void testNotFound() {
        ResponseEntity<Void> response = http.getForEntity("/tests/not-found", Void.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
    }

    /**
     * エラーパスに直接アクセスした場合。
     * 
     */
    @Test
    void testErrorPathDirectAccess() {
        ResponseEntity<Void> response = http.getForEntity("/error", Void.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.hasBody());
    }
}
