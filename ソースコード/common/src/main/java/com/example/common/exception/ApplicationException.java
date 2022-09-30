package com.example.common.exception;

/**
 * 業務例外。
 * 
 * @author sample
 * 
 */
public class ApplicationException extends RuntimeException {

    /**
     * この業務例外を関連付けるフィールド
     */
    private final String field;

    /**
     * メッセージコード
     */
    private final String code;

    /**
     * コンストラクタ。
     * 
     * @param field この業務例外を関連付けるフィールド
     * @param code メッセージコード
     * @param t 原因例外
     */
    public ApplicationException(String field, String code, Throwable t) {
        super(t);
        this.field = field;
        this.code = code;
    }

    /**
     * 業務例外を構築する。
     * 
     * @param field この業務例外を関連付けるフィールド
     * @param code メッセージコード
     * @return 業務例外
     */
    public static ApplicationException fieldError(String field, String code) {
        return new ApplicationException(field, code, null);
    }

    /**
     * 業務例外を構築する。
     * 
     * @param field この業務例外を関連付けるフィールド
     * @param code メッセージコード
     * @param t 原因例外
     * @return 業務例外
     */
    public static ApplicationException fieldError(String field, String code, Throwable t) {
        return new ApplicationException(field, code, t);
    }

    /**
     * 業務例外を構築する。
     * 
     * @param code メッセージコード
     * @return 業務例外
     */
    public static ApplicationException globalError(String code) {
        return new ApplicationException(null, code, null);
    }

    /**
     * 業務例外を構築する。
     * 
     * @param code メッセージコード
     * @param t 原因例外
     * @return 業務例外
     */
    public static ApplicationException globalError(String code, Throwable t) {
        return new ApplicationException(null, code, t);
    }

    /**
     * この業務例外を関連付けるフィールドを返す。
     * 
     * @return この業務例外を関連付けるフィールド
     */
    public String getField() {
        return field;
    }

    /**
     * メッセージコードを返す。
     * 
     * @return メッセージコード
     */
    public String getCode() {
        return code;
    }
}
