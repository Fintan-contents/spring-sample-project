package com.example.web.project.dto.update;

import java.time.LocalDate;

import org.springframework.beans.BeanUtils;

import com.example.common.generated.model.Project;
import com.example.web.project.model.update.ProjectView;

/**
 * プロジェクト
 *
 */
public class ProjectDto {

    /** プロジェクトID */
    private Integer projectId;

    /** プロジェクト名 */
    private String projectName;

    /** プロジェクト種別 */
    private String projectType;

    /** プロジェクト分類 */
    private String projectClass;

    /** プロジェクト開始日付 */
    private LocalDate projectStartDate;

    /** プロジェクト終了日付 */
    private LocalDate projectEndDate;

    /** 顧客ID */
    private Integer clientId;

    /** プロジェクトマネージャー */
    private String projectManager;

    /** プロジェクトリーダー */
    private String projectLeader;

    /** 備考 */
    private String note;

    /** 売上高 */
    private Integer sales;

    /** バージョン番号 */
    private Long versionNo;

    /** 組織ID */
    private Integer organizationId;

    /** 上位組織ID */
    private Integer divisionId;

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
     * 顧客IDを取得する。
     *
     * @return 顧客ID
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * 顧客IDを設定する。
     *
     * @param clientId 顧客ID
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
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
     * プロジェクトリーダーを取得する。
     *
     * @return プロジェクトリーダー
     */
    public String getProjectLeader() {
        return projectLeader;
    }

    /**
     * プロジェクトリーダーを設定する。
     *
     * @param projectLeader プロジェクトリーダー
     */
    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    /**
     * 備考を取得する。
     *
     * @return 備考
     */
    public String getNote() {
        return note;
    }

    /**
     * 備考を設定する。
     *
     * @param note 備考
     */
    public void setNote(String note) {
        this.note = note;
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
     * バージョン番号を取得する。
     *
     * @return バージョン番号
     */
    public Long getVersionNo() {
        return versionNo;
    }

    /**
     * バージョン番号を設定する。
     *
     * @param versionNo バージョン番号
     */
    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * 組織IDを取得する。
     *
     * @return 組織ID
     */
    public Integer getOrganizationId() {
        return organizationId;
    }

    /**
     * 組織IDを設定する。
     *
     * @param organizationId 組織ID
     */
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * 上位組織IDを取得する。
     *
     * @return 上位組織ID
     */
    public Integer getDivisionId() {
        return divisionId;
    }

    /**
     * 上位組織IDを設定する。
     *
     * @param divisionId 上位組織
     */
    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Modelに変換する。
     * 
     * @return 変換後のModel
     */
    public Project toProject() {
        Project project = new Project();
        BeanUtils.copyProperties(this, project);
        return project;
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
