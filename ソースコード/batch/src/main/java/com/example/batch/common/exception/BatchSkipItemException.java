package com.example.batch.common.exception;

/**
 * アイテムの処理をスキップするときにスローする例外。
 */
public class BatchSkipItemException extends RuntimeException {

    private final String code;
    private final Object[] args;

    /**
     * コンストラクタ。
     * @param code メッセージのコード
     * @param args メッセージに埋め込むパラメータ
     */
    public BatchSkipItemException(String code, Object... args) {
        this.code = code;
        this.args = args;
    }

    /**
     * メッセージのコードを取得する。
     * @return メッセージのコード
     */
    public String getCode() {
        return code;
    }

    /**
     * メッセージに埋め込むパラメータを取得する。
     * @return メッセージに埋め込むパラメータ
     */
    public Object[] getArgs() {
        return args;
    }
}
