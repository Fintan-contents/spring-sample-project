package com.example.web.project.configuration;

/**
 * プロジェクトアップロードのProperties。
 * 
 * @author sample
 */
public class ProjectUploadProperties {

    /**
     * ファイルを格納するディレクトリ
     */
    private String dir;

    /**
     * ファイルを格納するディレクトリを取得する。
     * 
     * @return ファイルを格納するディレクトリ
     */
    public String getDir() {
        return dir;
    }

    /**
     * ファイルを格納するディレクトリを設定する。
     * 
     * @param dir ファイルを格納するディレクトリ
     */
    public void setDir(String dir) {
        this.dir = dir;
    }
}
