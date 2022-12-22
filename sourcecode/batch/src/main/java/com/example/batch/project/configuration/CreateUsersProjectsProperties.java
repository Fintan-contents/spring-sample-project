package com.example.batch.project.configuration;

import java.nio.file.Path;

/**
 * ユーザ別従事プロジェクト抽出バッチのプロパティを定義したクラス。
 */
public class CreateUsersProjectsProperties {
    /**
     * 出力先ディレクトリのパス。
     */
    private Path outputDirPath;

    /**
     * 出力ファイル名のプレフィックス。
     */
    private String outputFileNamePrefix;

    /**
     * チャンクサイズ。
     */
    private int chunkSize;

    /**
     * 出力先ディレクトリのパスを取得する。
     * @return 出力先ディレクトリのパス
     */
    public Path getOutputDirPath() {
        return outputDirPath;
    }

    /**
     * 出力先ディレクトリのパスを設定する。
     * @param outputDirPath 出力先ディレクトリのパス
     */
    public void setOutputDirPath(Path outputDirPath) {
        this.outputDirPath = outputDirPath;
    }

    /**
     * 出力ファイル名のプレフィックスを取得する。
     * @return 出力ファイル名のプレフィックス
     */
    public String getOutputFileNamePrefix() {
        return outputFileNamePrefix;
    }

    /**
     * 出力ファイル名のプレフィックスを設定する。
     * @param outputFileNamePrefix 出力ファイル名のプレフィックス
     */
    public void setOutputFileNamePrefix(String outputFileNamePrefix) {
        this.outputFileNamePrefix = outputFileNamePrefix;
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
