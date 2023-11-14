package com.example.web.project.form;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;

import com.example.web.test.ValidationTestBase;
import com.example.web.test.WebTest;

/**
 * プロジェクト検索画面の入力値精査のテスト。
 *
 */
@SpringBootTest
@Import(ValidationTestBase.SessionRepositoryConfiguration.class)
@WebTest
@AutoConfigureMockMvc
public class ProjectSearchFormTest extends ValidationTestBase {

    private static final String SEARCH_PATH = "/project/search/search";
    private static final String FORM_NAME = "projectSearchForm";

    // 検索処理のバリデーション

    /**
     * 検索を行う際、最大長+1の文字列を入力すると、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser
    void testSearchOverMaxLength() throws Exception {
        MvcResult mvcResult = mvc
                .perform(get(SEARCH_PATH)
                        .queryParam("projectName", StringUtils.repeat("あ", 128 + 1)))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("プロジェクト名は128文字以下の全角文字で入力してください。",
                getErrorMessage(bindingResult, "projectName"));
    }

    /**
     * 検索を行う際、最大桁を超える数値を入力すると、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser
    void testSearchOverMaxNumbers() throws Exception {
        MvcResult mvcResult = mvc
                .perform(get(SEARCH_PATH)
                        .param("divisionId", "12345")
                        .param("organizationId", "56789"))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "divisionId"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "organizationId"));
    }

    /**
     * 検索を行う際、売上高に上限金額を超えた値が入力されていると、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser
    void testSearchOverUpperLimitNumbers() throws Exception {
        MvcResult mvcResult = mvc
                .perform(get(SEARCH_PATH)
                        .param("salesFrom", "1000000000")
                        .param("salesTo", "1000000000"))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("0円から999999999円の範囲で入力してください。", getErrorMessage(bindingResult, "salesFrom"));
        assertEquals("0円から999999999円の範囲で入力してください。", getErrorMessage(bindingResult, "salesTo"));
    }

    /**
     * 検索を行う際、許可されない文字が入力された場合、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser
    void testSearchDeniedCharacters() throws Exception {
        MvcResult mvcResult = mvc
                .perform(get(SEARCH_PATH)
                        .param("divisionId", "A")
                        .param("organizationId", "C"))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "divisionId"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "organizationId"));
    }

    /**
     * 検索を行う際、存在しないコード値を入力した場合、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser
    void testSearchNotExistsCode() throws Exception {
        MvcResult mvcResult = mvc
                .perform(get(SEARCH_PATH)
                        .param("projectTypes", "44")
                        .param("projectClasses", "44"))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("不正な値が指定されました。", getErrorMessage(bindingResult, "projectTypes"));
        assertEquals("不正な値が指定されました。", getErrorMessage(bindingResult, "projectClasses"));
    }

    /**
     * 検索を行う際、無効な日付を入力した場合、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser
    void testSearchInvalidDate1() throws Exception {
        MvcResult mvcResult = mvc
                .perform(get(SEARCH_PATH)
                        .param("projectStartDateFrom", "xxxxxx")
                        .param("projectStartDateTo", "xxxxxx")
                        .param("projectEndDateFrom", "xxxxxx")
                        .param("projectEndDateTo", "xxxxxx"))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectStartDateFrom"));
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectStartDateTo"));
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectEndDateFrom"));
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectEndDateTo"));
    }

    /**
     * 検索を行う際、無効な日付を入力した場合、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser
    void testSearchInvalidDate2() throws Exception {
        MvcResult mvcResult = mvc
                .perform(get(SEARCH_PATH)
                        .param("projectStartDateFrom", "2022-06-32")
                        .param("projectStartDateTo", "2022-06-32")
                        .param("projectEndDateFrom", "2022-06-32")
                        .param("projectEndDateTo", "2022-06-32"))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectStartDateFrom"));
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectStartDateTo"));
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectEndDateFrom"));
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectEndDateTo"));
    }
}
