package com.example.web.project.configuration;

/**
 * ユーザ別従事プロジェクトファイルダウンロードのProperties。
 * 
 * @author sample
 */
public class ProjectDownloadProperties {

    /**
     * 未処理を表すステータス
     */
    public static final String STATUS_UNPROCESSED = "01";

    /**
     * 処理中を表すステータス
     */
    public static final String STATUS_PROCESSING = "02";

    /**
     * ファイルが格納されているディレクトリ
     */
    private String dir;

    /**
     * ファイルが格納されているディレクトリを取得する。
     * 
     * @return ファイルが格納されているディレクトリ
     */
    public String getDir() {
        return dir;
    }

    /**
     * ファイルが格納されているディレクトリを設定する。
     * 
     * @param dir ファイルが格納されているディレクトリ
     */
    public void setDir(String dir) {
        this.dir = dir;
    }
}
