package com.example.web.project.configuration;

/**
 * プロジェクト検索機能のProperties。
 */
public class ProjectSearchProperties {

    /**
     * 1ページあたりの表示件数
     */
    private int recordsPerPage;

    /**
     * 1ページあたりの表示件数を取得する。
     * 
     * @return 1ページあたりの表示件数
     */
    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    /**
     * 1ページあたりの表示件数を設定する。
     * 
     * @param recordsPerPage 1ページあたりの表示件数
     */
    public void setRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }
}
