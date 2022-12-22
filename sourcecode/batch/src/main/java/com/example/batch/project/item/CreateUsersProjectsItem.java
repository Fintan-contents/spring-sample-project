package com.example.batch.project.item;

import com.example.common.generated.model.Project;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

/**
 * ユーザ別従事プロジェクト抽出バッチで出力するCSVファイルとマッピングするItem。
 */
public class CreateUsersProjectsItem {

    /**
     * {@link Project}を元に{@link CreateUsersProjectsItem}を生成する。
     * @param project 元となる{@link Project}
     * @return {@link Project}の値をコピーした{@link CreateUsersProjectsItem}
     */
    public static CreateUsersProjectsItem from(Project project) {
        CreateUsersProjectsItem item = new CreateUsersProjectsItem();
        BeanUtils.copyProperties(project, item);
        return item;
    }

    /**
     * プロジェクトID
     */
    private Integer projectId;

    /**
     * プロジェクト名
     */
    private String projectName;

    /**
     * プロジェクト種別
     */
    private String projectType;

    /**
     * プロジェクト分類
     */
    private String projectClass;

    /**
     * プロジェクト開始日付
     */
    private LocalDate projectStartDate;

    /**
     * プロジェクト終了日付
     */
    private LocalDate projectEndDate;

    /**
     * 部門ID
     */
    private Integer organizationId;

    /**
     * 顧客ID
     */
    private Integer clientId;

    /**
     * プロジェクトマネージャー名
     */
    private String projectManager;

    /**
     * プロジェクトリーダー名
     */
    private String projectLeader;

    /**
     * 備考
     */
    private String note;

    /**
     * 売上高
     */
    private Integer sales;

    /**
     * バージョン番号
     */
    private Long versionNo;

    /**
     * プロジェクトIDを取得する。
     * @return プロジェクトID
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * プロジェクトIDを設定する。
     * @param projectId プロジェクトID
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * プロジェクト名を取得する。
     * @return プロジェクト名
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * プロジェクト名を設定する。
     * @param projectName プロジェクト名
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * プロジェクト種別を取得する。
     * @return プロジェクト種別
     */
    public String getProjectType() {
        return projectType;
    }

    /**
     * プロジェクト種別を設定する。
     * @param projectType プロジェクト種別
     */
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    /**
     * プロジェクト分類を取得する。
     * @return プロジェクト分類
     */
    public String getProjectClass() {
        return projectClass;
    }

    /**
     * プロジェクト分類を設定する。
     * @param projectClass プロジェクト分類
     */
    public void setProjectClass(String projectClass) {
        this.projectClass = projectClass;
    }

    /**
     * プロジェクト開始日付を取得する。
     * @return プロジェクト開始日付
     */
    public LocalDate getProjectStartDate() {
        return projectStartDate;
    }

    /**
     * プロジェクト開始日付を設定する。
     * @param projectStartDate プロジェクト開始日付
     */
    public void setProjectStartDate(LocalDate projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    /**
     * プロジェクト終了日付を取得する。
     * @return プロジェクト終了日付
     */
    public LocalDate getProjectEndDate() {
        return projectEndDate;
    }

    /**
     * プロジェクト終了日付を設定する。
     * @param projectEndDate プロジェクト終了日付
     */
    public void setProjectEndDate(LocalDate projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    /**
     * 部門IDを取得する。
     * @return 部門ID
     */
    public Integer getOrganizationId() {
        return organizationId;
    }

    /**
     * 部門IDを設定する。
     * @param organizationId 部門ID
     */
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * 顧客IDを取得する。
     * @return 顧客ID
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * 顧客IDを設定する。
     * @param clientId 顧客ID
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * プロジェクトマネージャー名を取得する。
     * @return プロジェクトマネージャー名
     */
    public String getProjectManager() {
        return projectManager;
    }

    /**
     * プロジェクトマネージャー名を設定する。
     * @param projectManager プロジェクトマネージャー名
     */
    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * プロジェクトリーダー名を取得する。
     * @return プロジェクトリーダー名
     */
    public String getProjectLeader() {
        return projectLeader;
    }

    /**
     * プロジェクトリーダー名を設定する。
     * @param projectLeader プロジェクトリーダー名
     */
    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    /**
     * 備考を取得する。
     * @return 備考
     */
    public String getNote() {
        return note;
    }

    /**
     * 備考を設定する。
     * @param note 備考
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 売上高を取得する。
     * @return 売上高
     */
    public Integer getSales() {
        return sales;
    }

    /**
     * 売上高を設定する。
     * @param sales 売上高
     */
    public void setSales(Integer sales) {
        this.sales = sales;
    }

    /**
     * バージョン番号を取得する。
     * @return バージョン番号
     */
    public Long getVersionNo() {
        return versionNo;
    }

    /**
     * バージョン番号を設定する。
     * @param versionNo バージョン番号
     */
    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }
}
