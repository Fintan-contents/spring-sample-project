package com.example.api.client.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.example.api.test.ApiTest;
import com.example.api.test.RestControllerTestBase;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

/**
 * 顧客削除APIのテスト。
 *
 * @author sample
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DBRider
@ApiTest
class ClientDeleteControllerTest extends RestControllerTestBase {
    public static final String ENDPOINT = "/clients";
    private static final String BASE_PATH = "com/example/api/client/controller/ClientDeleteControllerTest/";

    /**
     * 正常に顧客を削除できる。
     *
     */
    @Test
    @DataSet(BASE_PATH + "testDeleteClient/testDeleteClient.xlsx")
    @ExpectedDataSet(BASE_PATH + "testDeleteClient/expected-testDeleteClient.xlsx")
    void testDeleteClient() {
        RequestEntity<Void> request = RequestEntity.delete(ENDPOINT + "/3").build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(forJson(response.getBody()))
                .isNull();
    }

    /**
     * 対象のレコードが無い場合、404 Not Found。
     *
     */
    @Test
    @DataSet(BASE_PATH + "testNoClientWithMatchingPathParameter/testNoClientWithMatchingPathParameter.xlsx")
    void testNoClientWithMatchingPathParameter() {
        RequestEntity<Void> request = RequestEntity.delete(ENDPOINT + "/11").build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testNoClientWithMatchingPathParameter/expected.json"));
    }

    /**
     * パスパラメータが不正な値の場合、400 Bad Request。
     *
     */
    @Test
    void testInvalidPathParameter() {
        RequestEntity<Void> request = RequestEntity.delete(ENDPOINT + "/test").build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testInvalidPathParameter/expected.json"));
    }
}
