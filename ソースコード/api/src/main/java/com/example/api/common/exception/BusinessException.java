package com.example.api.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 業務エラー
 */
public class BusinessException extends RuntimeException {
    /** 障害コード */
    private final String faultCode;
    /** HTTPステータス */
    private final HttpStatus status;
    /** メッセージID */
    private final String messageId;
    /** メッセージに含める追加情報 */
    private final Object[] args;

    /**
     * コンストラクタ
     * @param faultCode 障害コード
     * @param status HTTPステータス
     * @param messageId メッセージID
     * @param args メッセージに含める追加情報
     */
    public BusinessException(String faultCode, HttpStatus status, String messageId, Object... args) {
        this.faultCode = faultCode;
        this.status = status;
        this.messageId = messageId;
        this.args = args;
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
     * HTTPステータス を取得する。
     *
     * @return HTTPステータス
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * メッセージID を取得する。
     *
     * @return メッセージID
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * メッセージに含める追加情報 を取得する。
     *
     * @return メッセージに含める追加情報
     */
    public Object[] getArgs() {
        return args;
    }
}
