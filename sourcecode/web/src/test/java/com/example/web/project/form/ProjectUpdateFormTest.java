package com.example.web.project.form;

import static com.example.web.common.token.transaction.TransactionTokenRequestPostProcessor.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
 * プロジェクト更新画面の入力値精査のテスト。
 *
 */
@SpringBootTest
@Import(ValidationTestBase.SessionRepositoryConfiguration.class)
@WebTest
@AutoConfigureMockMvc
public class ProjectUpdateFormTest extends ValidationTestBase {

    private static final String CONFIRM_PATH = "/project/update/confirm";
    private static final String EXECUTE_PATH = "/project/update/execute?execute";
    private static final String FORM_NAME = "projectUpdateForm";
    private static final String TOKEN_NAME = "project/update";

    // 確認処理のバリデーション

    /**
     * 入力画面から確認画面へ遷移する際の必須チェックをテストする。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testConfirmRequired() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(CONFIRM_PATH)
                        .with(csrf()))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("入力してください。", getErrorMessage(bindingResult, "projectId"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "versionNo"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "projectClass"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "clientId"));
    }

    /**
     * 入力画面から確認画面へ遷移する際、最大長+1の文字列を入力すると、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testConfirmOverMaxLength() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(CONFIRM_PATH)
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "1")
                        .param("organizationId", "1")
                        .param("projectName", StringUtils.repeat("あ", 128 + 1))
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("clientId", "1")
                        .param("projectManager", StringUtils.repeat("あ", 128 + 1))
                        .param("projectLeader", StringUtils.repeat("あ", 128 + 1))
                        .param("projectStartDate", "2022-06-07")
                        .param("projectEndDate", "2022-06-07")
                        .param("note", StringUtils.repeat("あ", 512 + 1))
                        .with(csrf()))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("プロジェクト名は128文字以下の全角文字で入力してください。", getErrorMessage(bindingResult, "projectName"));
        assertEquals("氏名は128文字以下の全角文字で入力してください。", getErrorMessage(bindingResult, "projectManager"));
        assertEquals("氏名は128文字以下の全角文字で入力してください。", getErrorMessage(bindingResult, "projectLeader"));
        assertEquals("備考は512文字以下のシステム許容文字で入力してください。", getErrorMessage(bindingResult, "note"));
    }

    /**
     * 入力画面から確認画面へ遷移する際、最大桁を超える数値を入力すると、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testConfirmOverMaxNumbers() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(CONFIRM_PATH)
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "12345")
                        .param("organizationId", "56789")
                        .param("projectName", "あ")
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("clientId", "1234567890")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectStartDate", "2022-06-07")
                        .param("projectEndDate", "2022-06-07")
                        .with(csrf()))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "divisionId"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "organizationId"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "clientId"));
    }

    /**
     * 入力画面から確認画面へ遷移する際、売上高に上限金額を超えた値が入力されていると、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testConfirmOverUpperLimitNumbers() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(CONFIRM_PATH)
                        .param("sales", "1000000000")
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "1")
                        .param("organizationId", "1")
                        .param("projectName", "あ")
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("clientId", "1")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectStartDate", "2022-06-07")
                        .param("projectEndDate", "2022-06-07")
                        .with(csrf()))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("0円から999999999円の範囲で入力してください。", getErrorMessage(bindingResult, "sales"));
    }

    /**
     * 入力画面から確認画面へ遷移する際、許可されない文字が入力された場合、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testConfirmDeniedCharacters() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(CONFIRM_PATH)
                        .param("projectId", "X")
                        .param("versionNo", "X")
                        .param("divisionId", "A")
                        .param("organizationId", "C")
                        .param("projectName", "あ")
                        .param("clientId", "C")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("projectStartDate", "2022-06-07")
                        .param("projectEndDate", "2022-06-07")
                        .with(csrf()))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "projectId"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "versionNo"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "divisionId"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "organizationId"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "clientId"));
    }

    /**
     * 入力画面から確認画面へ遷移する際、存在しないコード値を入力した場合、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testConfirmNotExistsCode() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(CONFIRM_PATH)
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "1")
                        .param("organizationId", "1")
                        .param("projectName", "あ")
                        .param("projectType", "44")
                        .param("projectClass", "44")
                        .param("clientId", "1")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectStartDate", "2022-06-07")
                        .param("projectEndDate", "2022-06-07")
                        .with(csrf()))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("不正な値が指定されました。", getErrorMessage(bindingResult, "projectType"));
        assertEquals("不正な値が指定されました。", getErrorMessage(bindingResult, "projectClass"));
    }

    /**
     * 入力画面から確認画面へ遷移する際、無効な日付を入力した場合、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testConfirmInvalidDate1() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(CONFIRM_PATH)
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "1")
                        .param("organizationId", "1")
                        .param("projectName", "あ")
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("clientId", "1")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectStartDate", "xxxxxx")
                        .param("projectEndDate", "xxxxxx")
                        .with(csrf()))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectStartDate"));
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectEndDate"));
    }

    /**
     * 入力画面から確認画面へ遷移する際、無効な日付を入力した場合、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testConfirmInvalidDate2() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(CONFIRM_PATH)
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "1")
                        .param("organizationId", "1")
                        .param("projectName", "あ")
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("clientId", "1")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectStartDate", "2022-06-32")
                        .param("projectEndDate", "2022-06-32")
                        .with(csrf()))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectStartDate"));
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectEndDate"));
    }

    // 更新処理のバリデーション

    // 登録処理のバリデーション

    /**
     * 確認画面から完了画面へ遷移する際のバリデーションエラー発生時の遷移先をテストする。
     *
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testExecuteViewNameOnError() throws Exception {
        mvc
                .perform(post(EXECUTE_PATH)
                        .with(csrf())
                        // 確認画面から完了画面へ遷移する際は二重送信チェックが行われるが、
                        // そこでエラーにならないために以下のコードが必要となる。
                        .with(transactionToken(TOKEN_NAME)))
                .andExpect(view().name("project/update/index"));
    }

    /**
     * 確認画面から完了画面へ遷移する際の必須チェックをテストする。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testExecuteRequired() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(EXECUTE_PATH)
                        .with(csrf())
                        // 確認画面から完了画面へ遷移する際は二重送信チェックが行われるが、
                        // そこでエラーにならないために以下のコードが必要となる。
                        .with(transactionToken(TOKEN_NAME)))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("入力してください。", getErrorMessage(bindingResult, "projectId"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "versionNo"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "divisionId"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "organizationId"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "projectName"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "projectType"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "projectClass"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "clientId"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "projectManager"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "projectLeader"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "projectStartDate"));
        assertEquals("入力してください。", getErrorMessage(bindingResult, "projectEndDate"));
    }

    /**
     * 確認画面から完了画面へ遷移する際、最大長+1の文字列を入力すると、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testExecuteOverMaxLength() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(EXECUTE_PATH)
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "1")
                        .param("organizationId", "1")
                        .param("projectName", StringUtils.repeat("あ", 128 + 1))
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("clientId", "1")
                        .param("projectManager", StringUtils.repeat("あ", 128 + 1))
                        .param("projectLeader", StringUtils.repeat("あ", 128 + 1))
                        .param("projectStartDate", "2022-06-07")
                        .param("projectEndDate", "2022-06-07")
                        .param("note", StringUtils.repeat("あ", 512 + 1))
                        .with(csrf())
                        // 確認画面から完了画面へ遷移する際は二重送信チェックが行われるが、
                        // そこでエラーにならないために以下のコードが必要となる。
                        .with(transactionToken(TOKEN_NAME)))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("プロジェクト名は128文字以下の全角文字で入力してください。", getErrorMessage(bindingResult, "projectName"));
        assertEquals("氏名は128文字以下の全角文字で入力してください。", getErrorMessage(bindingResult, "projectManager"));
        assertEquals("氏名は128文字以下の全角文字で入力してください。", getErrorMessage(bindingResult, "projectLeader"));
        assertEquals("備考は512文字以下のシステム許容文字で入力してください。", getErrorMessage(bindingResult, "note"));
    }

    /**
     * 確認画面から完了画面へ遷移する際、最大桁を超える数値を入力すると、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testExecuteOverMaxNumbers() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(EXECUTE_PATH)
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "12345")
                        .param("organizationId", "56789")
                        .param("projectName", "あ")
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("clientId", "1234567890")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectStartDate", "2022-06-07")
                        .param("projectEndDate", "2022-06-07")
                        .with(csrf())
                        // 確認画面から完了画面へ遷移する際は二重送信チェックが行われるが、
                        // そこでエラーにならないために以下のコードが必要となる。
                        .with(transactionToken(TOKEN_NAME)))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "divisionId"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "organizationId"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "clientId"));
    }

    /**
     * 確認画面から完了画面へ遷移する際、売上高に上限金額を超えた値が入力されていると、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testExecuteOverUpperLimitNumbers() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(EXECUTE_PATH)
                        .param("sales", "1000000000")
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "1")
                        .param("organizationId", "1")
                        .param("projectName", "あ")
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("clientId", "1")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectStartDate", "2022-06-07")
                        .param("projectEndDate", "2022-06-07")
                        .with(csrf())
                        // 確認画面から完了画面へ遷移する際は二重送信チェックが行われるが、
                        // そこでエラーにならないために以下のコードが必要となる。
                        .with(transactionToken(TOKEN_NAME)))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("0円から999999999円の範囲で入力してください。", getErrorMessage(bindingResult, "sales"));
    }

    /**
     * 確認画面から完了画面へ遷移する際、売上高に下限金額を下回る値が入力されていると、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testExecuteBelowLowerLimitNumbers() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(EXECUTE_PATH)
                        .param("sales", "-1")
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "1")
                        .param("organizationId", "1")
                        .param("projectName", "あ")
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("clientId", "1")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectStartDate", "2022-06-07")
                        .param("projectEndDate", "2022-06-07")
                        .with(csrf())
                        // 確認画面から完了画面へ遷移する際は二重送信チェックが行われるが、
                        // そこでエラーにならないために以下のコードが必要となる。
                        .with(transactionToken(TOKEN_NAME)))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("0円から999999999円の範囲で入力してください。", getErrorMessage(bindingResult, "sales"));
    }

    /**
     * 確認画面から完了画面へ遷移する際、許可されない文字が入力された場合、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testExecuteDeniedCharacters() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(EXECUTE_PATH)
                        .param("projectId", "X")
                        .param("versionNo", "X")
                        .param("divisionId", "A")
                        .param("organizationId", "C")
                        .param("projectName", "Test")
                        .param("sales", "五百")
                        .param("clientId", "C")
                        .param("projectManager", "Yamada")
                        .param("projectLeader", "Leader")
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("projectStartDate", "2022-06-07")
                        .param("projectEndDate", "2022-06-07")
                        .with(csrf())
                        // 確認画面から完了画面へ遷移する際は二重送信チェックが行われるが、
                        // そこでエラーにならないために以下のコードが必要となる。
                        .with(transactionToken(TOKEN_NAME)))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "projectId"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "versionNo"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "divisionId"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "organizationId"));
        assertEquals("プロジェクト名は128文字以下の全角文字で入力してください。", getErrorMessage(bindingResult, "projectName"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "sales"));
        assertEquals("数値を入力してください。", getErrorMessage(bindingResult, "clientId"));
        assertEquals("氏名は128文字以下の全角文字で入力してください。", getErrorMessage(bindingResult, "projectManager"));
        assertEquals("氏名は128文字以下の全角文字で入力してください。", getErrorMessage(bindingResult, "projectLeader"));
    }

    /**
     * 確認画面から完了画面へ遷移する際、存在しないコード値を入力した場合、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testExecuteNotExistsCode() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(EXECUTE_PATH)
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "1")
                        .param("organizationId", "1")
                        .param("projectName", "あ")
                        .param("projectType", "44")
                        .param("projectClass", "44")
                        .param("clientId", "1")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectStartDate", "2022-06-07")
                        .param("projectEndDate", "2022-06-07")
                        .with(csrf())
                        // 確認画面から完了画面へ遷移する際は二重送信チェックが行われるが、
                        // そこでエラーにならないために以下のコードが必要となる。
                        .with(transactionToken(TOKEN_NAME)))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("不正な値が指定されました。", getErrorMessage(bindingResult, "projectType"));
        assertEquals("不正な値が指定されました。", getErrorMessage(bindingResult, "projectClass"));
    }

    /**
     * 確認画面から完了画面へ遷移する際、無効な日付を入力した場合、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testExecuteInvalidDate1() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(EXECUTE_PATH)
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "1")
                        .param("organizationId", "1")
                        .param("projectName", "あ")
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("clientId", "1")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectStartDate", "xxxxxx")
                        .param("projectEndDate", "xxxxxx")
                        .with(csrf())
                        // 確認画面から完了画面へ遷移する際は二重送信チェックが行われるが、
                        // そこでエラーにならないために以下のコードが必要となる。
                        .with(transactionToken(TOKEN_NAME)))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectStartDate"));
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectEndDate"));
    }

    /**
     * 確認画面から完了画面へ遷移する際、無効な日付を入力した場合、エラーになる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testExecuteInvalidDate2() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(EXECUTE_PATH)
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "1")
                        .param("organizationId", "1")
                        .param("projectName", "あ")
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("clientId", "1")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectStartDate", "2022-06-32")
                        .param("projectEndDate", "2022-06-32")
                        .with(csrf())
                        // 確認画面から完了画面へ遷移する際は二重送信チェックが行われるが、
                        // そこでエラーにならないために以下のコードが必要となる。
                        .with(transactionToken(TOKEN_NAME)))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectStartDate"));
        assertEquals("日付の形式が正しくありません。", getErrorMessage(bindingResult, "projectEndDate"));
    }

    /**
     * 確認画面から完了画面へ遷移する際、開始日付が終了日付よりも後の場合、エラーとなる。
     * 
     * @throws Exception MockMvcでエラーが発生した場合にスローされる
     */
    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void testExecuteProjectStartDateIsAfterProjectEndDate() throws Exception {
        MvcResult mvcResult = mvc
                .perform(post(EXECUTE_PATH)
                        .param("projectId", "1")
                        .param("versionNo", "1")
                        .param("divisionId", "1")
                        .param("organizationId", "1")
                        .param("projectName", "あ")
                        .param("projectType", "01")
                        .param("projectClass", "01")
                        .param("clientId", "1")
                        .param("projectManager", "あ")
                        .param("projectLeader", "あ")
                        .param("projectStartDate", "2022-06-08")
                        .param("projectEndDate", "2022-06-07")
                        .with(csrf())
                        // 確認画面から完了画面へ遷移する際は二重送信チェックが行われるが、
                        // そこでエラーにならないために以下のコードが必要となる。
                        .with(transactionToken(TOKEN_NAME)))
                .andReturn();
        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("終了日には、開始日より後の日付を入力して下さい。", getErrorMessage(bindingResult, "startDateBeforeEndDate"));
    }
}
