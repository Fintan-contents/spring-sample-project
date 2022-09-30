package com.example.batch.project.configuration;

import java.nio.file.Path;

/**
 * 期間内プロジェクト一括出力バッチのプロパティを定義したクラス。
 */
public class ExportProjectsInPeriodProperties {
    /**
     * 出力ファイルパス。
     */
    private Path outputFilePath;

    /**
     * チャンクサイズ。
     */
    private int chunkSize;

    /**
     * 出力ファイルパスを設定する。
     * @param outputFilePath 出力ファイルパス
     */
    public void setOutputFilePath(Path outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    /**
     * 出力ファイルのパスを取得する。
     * @return 出力ファイルのパス
     */
    public Path getOutputFilePath() {
        return outputFilePath;
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
