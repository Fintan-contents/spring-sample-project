package com.example.api.client.controller;

import com.example.api.test.ApiTest;
import com.example.api.test.RestControllerTestBase;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 顧客検索APIのテスト。
 * 
 * @author sample
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DBRider
@ApiTest
class ClientSearchControllerTest extends RestControllerTestBase {

    public static final String ENDPOINT = "/clients";
    private static final String BASE_PATH = "com/example/api/client/controller/ClientSearchControllerTest/";
    private static final String CLIENT_NAME_MAX_LENGTH = StringUtils.repeat("あ", 128);
    private static final String CLIENT_NAME_OVER_MAX_LENGTH = StringUtils.repeat("あ", 129);

    /**
     * パラメータが無い場合、全件検索する。
     * 
     */
    @Test
    @DataSet(BASE_PATH
            + "testSearchClientWithoutParameterReturnsAllClients/testSearchClientWithoutParameterReturnsAllClients.xlsx")
    void testSearchClientWithoutParameterReturnsAllClients() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testSearchClientWithoutParameterReturnsAllClients/expected.json"));
    }

    /**
     * 文字数が上限を超えている場合、精査エラー。
     * 
     */
    @Test
    void testCharLengthIsOverLimit() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .queryParam("clientName", CLIENT_NAME_OVER_MAX_LENGTH)
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testCharLengthIsOverLimit/expected.json"));
    }

    /**
     * パラメータがある場合、条件を付けて検索する。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testValidParameters/testValidParameters.xlsx")
    void testValidParameters() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .queryParam("clientName", CLIENT_NAME_MAX_LENGTH)
                .queryParam("industryCode", "03")
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    /**
     * 不正な値の場合、精査エラー。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testInvalidParameters/testInvalidParameters.xlsx")
    void testInvalidParameters() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .queryParam("clientName", "invalid parameter")
                .queryParam("industryCode", "04")
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testInvalidParameters/expected.json"));
    }

    /**
     * 顧客名を条件に検索する。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testSearchClientByClientName/testSearchClientByClientName.xlsx")
    void testSearchClientByClientName() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .queryParam("clientName", "テスト会社３")
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testSearchClientByClientName/expected.json"));
    }

    /**
     * 業種コードを条件に検索する。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testSearchClientByIndustryCode/testSearchClientByIndustryCode.xlsx")
    void testSearchClientByIndustryCode() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .queryParam("industryCode", "01")
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testSearchClientByIndustryCode/expected.json"));
    }

    /**
     * 条件にした顧客名に合致しない場合、空の顧客リストが返される。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testSearchClientByClientNameReturnsEmpty/testSearchClientByClientNameReturnsEmpty.xlsx")
    void testSearchClientByClientNameReturnsEmpty() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .queryParam("clientName", "存在しない会社")
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testSearchClientByClientNameReturnsEmpty/expected.json"));
    }

    /**
     * 条件にした業種コードに合致しない場合、空の顧客リストが返される。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testSearchClientByIndustryCodeReturnsEmpty/testSearchClientByIndustryCodeReturnsEmpty.xlsx")
    void testSearchClientByIndustryCodeReturnsEmpty() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .queryParam("industryCode", "03")
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testSearchClientByIndustryCodeReturnsEmpty/expected.json"));
    }

    /**
     * 顧客名と業種コードを条件に検索する。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testSearchClientByClientNameAndIndustryCode/testSearchClientByClientNameAndIndustryCode.xlsx")
    void testSearchClientByClientNameAndIndustryCode() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .queryParam("clientName", "テスト会社３")
                .queryParam("industryCode", "02")
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testSearchClientByClientNameAndIndustryCode/expected.json"));
    }

    /**
     * 条件にした顧客名と業種コードに合致しない場合、空の顧客リストが返される。
     * 
     */
    @Test
    @DataSet(BASE_PATH
            + "testSearchClientByClientNameAndIndustryCodeReturnsEmpty/testSearchClientByClientNameAndIndustryCodeReturnsEmpty.xlsx")
    void testSearchClientByClientNameAndIndustryCodeReturnsEmpty() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .queryParam("clientName", "存在しない会社")
                .queryParam("industryCode", "03")
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testSearchClientByIndustryCodeReturnsEmpty/expected.json"));
    }

    /**
     * 検索結果が上限を超える場合、400 Bad Request。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testResultIsOverUpperLimit/testResultIsOverUpperLimit.xlsx")
    void testResultIsOverUpperLimit() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testResultIsOverUpperLimit/expected.json"));
    }

    /**
     * テーブルにレコードが無い場合、空の顧客リストが返される。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testClientTableHasNoRecord/testClientTableHasNoRecord.xlsx")
    void testClientTableHasNoRecord() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testClientTableHasNoRecord/expected.json"));
    }

    /**
     * 検索結果が上限と同じ件数の場合、正常に結果が返される。
     * 
     */
    @Test
    @DataSet(BASE_PATH + "testResultIsEqualsUpperLimit/testResultIsEqualsUpperLimit.xlsx")
    void testResultIsEqualsUpperLimit() {
        URI uri = UriComponentsBuilder.fromPath(ENDPOINT)
                .queryParam("industryCode", "02")
                .build().encode().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = http.exchange(request, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(forJson(response.getBody()))
                .isStrictlyEqualToJson(read("testResultIsEqualsUpperLimit/expected.json"))
                .extractingJsonPathValue("$.clients.length()")
                .isEqualTo(1000);
    }
}
