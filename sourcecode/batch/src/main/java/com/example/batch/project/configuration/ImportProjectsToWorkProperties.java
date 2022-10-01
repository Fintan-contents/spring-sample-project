package com.example.batch.project.configuration;

import java.nio.file.Path;

/**
 * プロジェクト一括登録バッチ/ワークテーブル登録のプロパティを定義したクラス。
 */
public class ImportProjectsToWorkProperties {
    /**
     * 入力ファイルのパス。
     */
    private Path inputFilePath;

    /**
     * チャンクサイズ。
     */
    private int chunkSize;

    /**
     * 入力ファイルのパスを設定する。
     * @param inputFilePath 入力ファイルのパス
     */
    public void setInputFilePath(Path inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    /**
     * 入力ファイルのパスを取得する。
     * @return 入力ファイルのパス
     */
    public Path getInputFilePath() {
        return inputFilePath;
    }

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
