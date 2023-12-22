package com.example.api.common.error;

import java.util.List;

/**
 * 業務エラー時のレスポンス
 */
public class ApiError {
    /** 障害コード */
    private final String faultCode;
    /** メッセージリスト */
    private final List<String> messages;

    /**
     * コンストラクタ
     * @param faultCode 障害コード
     * @param messages エラー内容のメッセージ
     */
    public ApiError(String faultCode, List<String> messages) {
        this.faultCode = faultCode;
        this.messages = messages;
    }

    /**
     * コンストラクタ
     * @param faultCode 障害コード
     * @param message エラー内容のメッセージ
     */
    public ApiError(String faultCode, String message) {
        this.faultCode = faultCode;
        this.messages = List.of(message);
    }

    /**
     * 障害コード を取得する。
     *
     * @return 障害コード
     */
    public String getFaultCode() {
        return faultCode;
    }

    /**
     * メッセージリスト を取得する。
     *
     * @return メッセージリスト
     */
    public List<String> getMessages() {
        return messages;
    }
}
