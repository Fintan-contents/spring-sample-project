package com.example.web.project.form;

import static com.example.web.common.token.transaction.TransactionTokenRequestPostProcessor.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
 * プロジェクトアップロード画面の入力値精査のテスト。
 *
 */
@SpringBootTest
@Import(ValidationTestBase.SessionRepositoryConfiguration.class)
@WebTest
@AutoConfigureMockMvc
class ProjectUploadFormTest extends ValidationTestBase {

    private static final String EXECUTE_PATH = "/project/upload/execute?execute";
    private static final String FORM_NAME = "projectUploadForm";
    private static final String TOKEN_NAME = "project/upload";

    // 登録処理のバリデーション

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
        assertEquals("入力してください。", getErrorMessage(bindingResult, "tempFileId"));
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
                        .param("tempFileId", "一時ファイルID")
                        .with(csrf())
                        // 確認画面から完了画面へ遷移する際は二重送信チェックが行われるが、
                        // そこでエラーにならないために以下のコードが必要となる。
                        .with(transactionToken(TOKEN_NAME)))
                .andReturn();

        BindingResult bindingResult = getBindingResult(mvcResult, FORM_NAME);
        assertEquals("不正な文字種の値が指定されました。", getErrorMessage(bindingResult, "tempFileId"));
    }
}
