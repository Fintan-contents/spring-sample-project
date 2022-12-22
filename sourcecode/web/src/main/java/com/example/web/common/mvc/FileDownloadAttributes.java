package com.example.web.common.mvc;

/**
 * ファイルダウンロードの属性。
 * 
 * @author sample
 *
 */
public class FileDownloadAttributes {

    /**
     * 対象ファイルのパス
     */
    private final String targetFilePath;

    /**
     * ダウンロード時のファイル名
     */
    private final String downloadFileName;

    /**
     * コンストラクタ。
     * 
     * @param targetFilePath 対象ファイルのパス
     * @param downloadFileName ダウンロード時のファイル名
     */
    public FileDownloadAttributes(String targetFilePath, String downloadFileName) {
        this.targetFilePath = targetFilePath;
        this.downloadFileName = downloadFileName;
    }

    /**
     * 対象ファイルのパスを取得する。
     * 
     * @return 対象ファイルのパス
     */
    public String getTargetFilePath() {
        return targetFilePath;
    }

    /**
     * ダウンロード時のファイル名を取得する。
     * 
     * @return ダウンロード時のファイル名
     */
    public String getDownloadFileName() {
        return downloadFileName;
    }
}