package com.example.batch.common.exception;

/**
 * バッチを異常終了させるときにスローする例外。
 */
public class BatchSystemException extends RuntimeException {

    /**
     * エラーメッセージを設定してインスタンスを生成する。
     * @param message エラーメッセージ
     */
    public BatchSystemException(String message) {
        super(message);
    }
}
