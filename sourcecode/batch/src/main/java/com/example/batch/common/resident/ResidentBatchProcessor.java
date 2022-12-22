package com.example.batch.common.resident;

/**
 * 常駐バッチの制御ロジックのインタフェース。
 *
 */
public interface ResidentBatchProcessor {

    /**
     * 常駐バッチの処理を行う。
     * 
     * @return 定期処理を継続する場合はtrueを返す
     */
    boolean process();

    /**
     * 初期処理を行う。
     * 
     */
    void initialize();
}
