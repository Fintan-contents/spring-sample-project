package com.example.web.project.form;

/**
 * プロジェクト詳細機能のForm。
 * 
 * @author sample
 */
public class ProjectDetailForm {

    /**
     * プロジェクトID
     */
    private Integer projectId;

    /**
     * プロジェクトIDを取得する。
     *
     * @return プロジェクトID
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * プロジェクトIdを設定する。
     *
     * @param projectId 設定するプロジェクトId
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
