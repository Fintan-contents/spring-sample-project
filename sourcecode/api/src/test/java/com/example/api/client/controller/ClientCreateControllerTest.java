package com.example.api.client.controller;

import com.example.api.test.ApiTest;
import com.example.api.test.RestControllerTestBase;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 顧客登録APIのテスト。
 * 
 * @author sample
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DBRider
@ApiTest
class ClientCreateControllerTest extends RestControllerTestBase {

    public static final String ENDPOINT = "/clients";
    private static final String BASE_PATH = "com/example/api/client/controller/ClientCreateControllerTest/";

    /**
     * 必須項目がnullの場合、精査エラー。
     * 
     */
    @Test
    void testRequiredParameterIsNull() {
        RequestEntity<String> request = RequestEntity.post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testRequiredParameterIsNull/request.json"));
        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testRequiredParameterIsNull/expected.json"));
    }

    /**
     * 必須項目が空の場合、精査エラー。
     * 
     */
    @Test
    void testRequiredParameterIsEmpty() {
        RequestEntity<String> request = RequestEntity.post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testRequiredParameterIsEmpty/request.json"));
        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testRequiredParameterIsEmpty/expected.json"));
    }

    /**
     * 正常に登録ができる。
     * 
     */
    @Test
    @DataSet(value = BASE_PATH + "testCreateClient/testCreateClient.xlsx",
            executeScriptsBefore = BASE_PATH + "testCreateClient/reset_sequence_val.sql")
    @ExpectedDataSet(BASE_PATH + "testCreateClient/expected-testCreateClient.xlsx")
    void testCreateClient() {
        RequestEntity<String> request = RequestEntity.post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testCreateClient/request.json"));
        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testCreateClient/expected.json"));
    }

    /**
     * 文字数が上限を超えている場合、精査エラー。
     * 
     */
    @Test
    void testCharLengthIsOverLimit() {
        RequestEntity<String> request = RequestEntity.post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testCharLengthIsOverLimit/request.json"));
        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testCharLengthIsOverLimit/expected.json"));
    }

    /**
     * 不正な値の場合、精査エラー。
     * 
     */
    @Test
    void testInvalidParameter() {
        RequestEntity<String> request = RequestEntity.post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testInvalidParameter/request.json"));
        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testInvalidParameter/expected.json"));
    }

    /**
     * すでに登録済みの顧客名の場合、重複エラー。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testDuplicateKeyException/testDuplicateKeyException.xlsx")
    @ExpectedDataSet(BASE_PATH + "testDuplicateKeyException/expected-testDuplicateKeyException.xlsx")
    void testDuplicateKeyException() {
        RequestEntity<String> request = RequestEntity.post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testDuplicateKeyException/request.json"));
        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testDuplicateKeyException/expected.json"));
    }
}
