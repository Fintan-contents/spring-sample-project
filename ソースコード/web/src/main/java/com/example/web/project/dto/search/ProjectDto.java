package com.example.web.project.dto.search;

import java.time.LocalDate;

import org.springframework.beans.BeanUtils;

import com.example.web.project.model.search.ProjectView;

/**
 * プロジェクト
 *
 */
public class ProjectDto {

    /** プロジェクトID */
    private Integer projectId;

    /** プロジェクト名 */
    private String projectName;

    /** 組織名 */
    private String organizationName;

    /** 上位組織名 */
    private String divisionName;

    /** プロジェクト種別 */
    private String projectType;

    /** プロジェクト分類 */
    private String projectClass;

    /** プロジェクトマネージャー */
    private String projectManager;

    /** 売上高 */
    private Integer sales;

    /** プロジェクト開始日付 */
    private LocalDate projectStartDate;

    /** プロジェクト終了日付 */
    private LocalDate projectEndDate;

    /**
     * プロジェクトIDを取得する。
     *
     * @return プロジェクトID
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * プロジェクトIDを設定する。
     *
     * @param projectId プロジェクトID
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * プロジェクト名を取得する。
     *
     * @return プロジェクト名
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * プロジェクト名を設定する。
     *
     * @param projectName プロジェクト名
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * 組織名を取得する。
     *
     * @return 組織名
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * 組織名を設定する。
     *
     * @param organizationName 組織名
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * 上位組織名を取得する。
     *
     * @return 上位組織名ID
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * 上位組織名を設定する。
     *
     * @param divisionName 上位組織名
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * プロジェクト種別を取得する。
     *
     * @return プロジェクト種別
     */
    public String getProjectType() {
        return projectType;
    }

    /**
     * プロジェクト種別を設定する。
     *
     * @param projectType プロジェクト種別
     */
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    /**
     * プロジェクト分類を取得する。
     *
     * @return プロジェクト分類
     */
    public String getProjectClass() {
        return projectClass;
    }

    /**
     * プロジェクト分類を設定する。
     *
     * @param projectClass プロジェクト分類
     */
    public void setProjectClass(String projectClass) {
        this.projectClass = projectClass;
    }

    /**
     * プロジェクトマネージャーを取得する。
     *
     * @return プロジェクトマネージャー
     */
    public String getProjectManager() {
        return projectManager;
    }

    /**
     * プロジェクトマネージャーを設定する。
     *
     * @param projectManager プロジェクトマネージャー
     */
    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * 売上高を取得する。
     *
     * @return 売上高
     */
    public Integer getSales() {
        return sales;
    }

    /**
     * 売上高を設定する。
     *
     * @param sales 売上高
     */
    public void setSales(Integer sales) {
        this.sales = sales;
    }

    /**
     * プロジェクト開始日付を取得する。
     *
     * @return プロジェクト開始日付
     */
    public LocalDate getProjectStartDate() {
        return projectStartDate;
    }

    /**
     * プロジェクト開始日付を設定する。
     *
     * @param projectStartDate プロジェクト開始日付
     */
    public void setProjectStartDate(LocalDate projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    /**
     * プロジェクト終了日付を取得する。
     *
     * @return プロジェクト終了日付
     */
    public LocalDate getProjectEndDate() {
        return projectEndDate;
    }

    /**
     * プロジェクト終了日付を設定する。
     *
     * @param projectEndDate プロジェクト終了日付
     */
    public void setProjectEndDate(LocalDate projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    /**
     * Modelからインスタンスを生成する。
     * 
     * @param projectView 変換元のModel
     * @return インスタンス
     */
    public static ProjectDto fromProject(ProjectView projectView) {
        ProjectDto projectDto = new ProjectDto();
        BeanUtils.copyProperties(projectView, projectDto);
        return projectDto;
    }
}
