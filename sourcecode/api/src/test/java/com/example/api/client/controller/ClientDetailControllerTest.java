package com.example.api.client.controller;

import com.example.api.test.ApiTest;
import com.example.api.test.RestControllerTestBase;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 顧客詳細APIのテスト。
 * 
 * @author sample
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DBRider
@ApiTest
class ClientDetailControllerTest extends RestControllerTestBase {
    public static final String ENDPOINT = "/clients";
    private static final String BASE_PATH = "com/example/api/client/controller/ClientDetailControllerTest/";

    /**
     * 正常に顧客を取得できる。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testGetClient/testGetClient.xlsx")
    void testGetClient() {
        RequestEntity<Void> request = RequestEntity.get(ENDPOINT + "/3").build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testGetClient/expected.json"));
    }

    /**
     * テーブルにレコードが無い場合、404 Not Found。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testClientTableHasNoRecord/testClientTableHasNoRecord.xlsx")
    void testClientTableHasNoRecord() {
        RequestEntity<Void> request = RequestEntity.get(ENDPOINT + "/1").build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testClientTableHasNoRecord/expected.json"));
    }

    /**
     * 対象のレコードが無い場合、404 Not Found。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testNoClientWithMatchingPathParameter/testNoClientWithMatchingPathParameter.xlsx")
    void testNoClientWithMatchingPathParameter() {
        RequestEntity<Void> request = RequestEntity.get(ENDPOINT + "/11").build();

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
        RequestEntity<Void> request = RequestEntity.get(ENDPOINT + "/test").build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testInvalidPathParameter/expected.json"));
    }
}
