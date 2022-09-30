package com.example.batch.project.configuration;

/**
 * プロジェクト一括登録バッチ/本テーブル登録のプロパティを定義したクラス。
 */
public class ImportProjectsProperties {
    /**
     * チャンクサイズ。
     */
    private int chunkSize;

    /**
     * チャンクサイズを取得する。
     * @return チャンクサイズ
     */
    public int getChunkSize() {
        return chunkSize;
    }

    /**
     * チャンクサイズを設定する。
     * @param chunkSize チャンクサイズ
     */
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
}
