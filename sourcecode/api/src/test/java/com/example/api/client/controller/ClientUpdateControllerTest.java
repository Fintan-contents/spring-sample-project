package com.example.api.client.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.example.api.test.ApiTest;
import com.example.api.test.RestControllerTestBase;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

/**
 * 顧客更新APIのテスト。
 *
 * @author sample
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DBRider
@ApiTest
class ClientUpdateControllerTest extends RestControllerTestBase {

    public static final String ENDPOINT = "/clients";
    private static final String BASE_PATH = "com/example/api/client/controller/ClientUpdateControllerTest/";

    /**
     * 必須項目がnullの場合、精査エラー。
     *
     */
    @Test
    void testRequiredParameterIsNull() {
        RequestEntity<String> request = RequestEntity.put(ENDPOINT + "/1")
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
        RequestEntity<String> request = RequestEntity.put(ENDPOINT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testRequiredParameterIsEmpty/request.json"));
        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testRequiredParameterIsEmpty/expected.json"));
    }

    /**
     * 正常に更新ができる。
     *
     */
    @Test
    @DataSet(value = BASE_PATH + "testUpdateClient/testUpdateClient.xlsx",
            executeScriptsBefore = BASE_PATH + "testUpdateClient/reset_sequence_val.sql")
    @ExpectedDataSet(BASE_PATH + "testUpdateClient/expected-testUpdateClient.xlsx")
    void testUpdateClient() {
        RequestEntity<String> request = RequestEntity.put(ENDPOINT + "/3")
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testUpdateClient/request.json"));
        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testUpdateClient/expected.json"));
    }

    /**
     * 顧客名を変更しない場合でも、重複エラーにならず、正常に更新ができる。
     *
     */
    @Test
    @DataSet(value = BASE_PATH + "testUpdateClientWithSameClientname/testUpdateClientWithSameClientname.xlsx",
            executeScriptsBefore = BASE_PATH + "testUpdateClientWithSameClientname/reset_sequence_val.sql")
    @ExpectedDataSet(BASE_PATH + "testUpdateClientWithSameClientname/expected-testUpdateClientWithSameClientname.xlsx")
    void testUpdateClientWithSameClientname() {
        RequestEntity<String> request = RequestEntity.put(ENDPOINT + "/3")
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testUpdateClientWithSameClientname/request.json"));
        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testUpdateClientWithSameClientname/expected.json"));
    }


    /**
     * 文字数が上限を超えている場合、精査エラー。
     *
     */
    @Test
    void testCharLengthIsOverLimit() {
        RequestEntity<String> request = RequestEntity.put(ENDPOINT + "/1")
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
        RequestEntity<String> request = RequestEntity.put(ENDPOINT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testInvalidParameter/request.json"));
        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testInvalidParameter/expected.json"));
    }

    /**
     * バージョン番号が不正な値の場合、400 Bad Request。
     *
     */
    @Test
    void testInvalidVersionParameter() {
        RequestEntity<String> request = RequestEntity.put(ENDPOINT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testInvalidVersionParameter/request.json"));

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testInvalidVersionParameter/expected.json"));
    }

    /**
     * パスパラメータが不正な値の場合、400 Bad Request。
     *
     */
    @Test
    void testInvalidPathParameter() {
        RequestEntity<String> request = RequestEntity.put(ENDPOINT + "/test")
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testInvalidPathParameter/request.json"));

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testInvalidPathParameter/expected.json"));
    }

    /**
     * すでに登録済みの顧客名の場合、重複エラー。
     *
     */
    @Test
    @DataSet(BASE_PATH + "testDuplicateKeyException/testDuplicateKeyException.xlsx")
    @ExpectedDataSet(BASE_PATH + "testDuplicateKeyException/expected-testDuplicateKeyException.xlsx")
    void testDuplicateKeyException() {
        RequestEntity<String> request = RequestEntity.put(ENDPOINT + "/3")
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testDuplicateKeyException/request.json"));
        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testDuplicateKeyException/expected.json"));
    }

    /**
     * 対象のレコードが無い場合、404 Not Found。
     *
     */
    @Test
    @DataSet(BASE_PATH + "testNoClientWithMatchingPathParameter/testNoClientWithMatchingPathParameter.xlsx")
    @ExpectedDataSet(BASE_PATH + "testNoClientWithMatchingPathParameter/expected-testNoClientWithMatchingPathParameter.xlsx")
    void testNoClientWithMatchingPathParameter() {

        RequestEntity<String> request = RequestEntity.put(ENDPOINT + "/11")
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testNoClientWithMatchingPathParameter/request.json"));

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testNoClientWithMatchingPathParameter/expected.json"));
    }

    /**
     * 他のユーザが更新した場合、409 Conflict。
     *
     */
    @Test
    @DataSet(BASE_PATH + "testOptimisticLock/testOptimisticLock.xlsx")
    @ExpectedDataSet(BASE_PATH + "testOptimisticLock/expected-testOptimisticLock.xlsx")
    void testOptimisticLock() {

        RequestEntity<String> request = RequestEntity.put(ENDPOINT + "/3")
                .contentType(MediaType.APPLICATION_JSON)
                .body(read("testOptimisticLock/request.json"));

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testOptimisticLock/expected.json"));
    }

}
