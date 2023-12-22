package com.example.web.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.session.MapSessionRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Formのバリデーションをテストするためのテスト基底クラス。
 *
 */
public abstract class ValidationTestBase {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    private MessageSource messageSource;

    /**
     * バリデーション結果を取得する。
     * 
     * @param mvcResult MockMvcの実行結果
     * @param name Form名（Formのクラス単純名の先頭1文字を小文字にしたもの）
     * @return バリデーション結果
     */
    protected BindingResult getBindingResult(MvcResult mvcResult, String name) {
        return (BindingResult) mvcResult.getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + name);
    }

    /**
     * エラーメッセージを取得する。
     * 
     * @param bindingResult バリデーション結果
     * @param field フィールド名
     * @return エラーメッセージ
     */
    protected String getErrorMessage(BindingResult bindingResult, String field) {
        return messageSource.getMessage(bindingResult.getFieldError(field), null);
    }

    /**
     * WORKAROUND: Spring Session JDBCがMockMVCのsessionを無視してしまうIssueの対応。
     *   https://github.com/spring-projects/spring-session/issues/2037
     */
    @TestConfiguration
    public static class SessionRepositoryConfiguration {
        @Bean
        public MapSessionRepository mapSessionRepository() {
            return new MapSessionRepository(new ConcurrentHashMap<>());
        }
    }
}
