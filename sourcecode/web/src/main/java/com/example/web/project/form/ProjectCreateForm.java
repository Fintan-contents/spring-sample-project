package com.example.web.project.form;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.web.project.dto.create.ProjectDto;

import nablarch.core.validation.ee.Domain;
import nablarch.core.validation.ee.Required;

/**
 * プロジェクト登録機能のForm。
 *
 * @author sample
 */
public class ProjectCreateForm {

    /**
     * プロジェクト名
     */
    @Required
    @Domain("projectName")
    private String projectName;

    /**
     * プロジェクト種別
     */
    @Required
    @Domain("projectType")
    private String projectType;

    /**
     * プロジェクト分類
     */
    @Required
    @Domain("projectClass")
    private String projectClass;

    /**
     * プロジェクト開始日付
     */
    @Required
    @DateTimeFormat(pattern = "uuuu-MM-dd")
    private LocalDate projectStartDate;

    /**
     * プロジェクト終了日付
     */
    @Required
    @DateTimeFormat(pattern = "uuuu-MM-dd")
    private LocalDate projectEndDate;

    /**
     * 事業部ID
     */
    @Required
    @Domain("organizationId")
    private Integer divisionId;

    /**
     * 部門ID
     */
    @Required
    @Domain("organizationId")
    private Integer organizationId;

    /**
     * 顧客ID
     */
    @Required
    @Domain("clientId")
    private Integer clientId;

    /**
     * プロジェクトマネージャー名
     */
    @Required
    @Domain("userName")
    private String projectManager;

    /**
     * プロジェクトリーダー名
     */
    @Required
    @Domain("userName")
    private String projectLeader;

    /**
     * 備考
     */
    @Domain("note")
    private String note;

    /**
     * 売上高
     */
    @Domain("amountOfMoney")
    private Integer sales;

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
     * @param projectName 設定するプロジェクト名
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
     * @param projectType 設定するプロジェクト種別
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
     * @param projectClass 設定するプロジェクト分類
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
     * @param projectStartDate 設定するプロジェクト開始日付
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
     * @param projectEndDate 設定するプロジェクト終了日付
     */
    public void setProjectEndDate(LocalDate projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    /**
     * 事業部IDを取得する。
     *
     * @return 事業部ID
     */
    public Integer getDivisionId() {
        return divisionId;
    }

    /**
     * 事業部IDを設定する。
     *
     * @param divisionId 設定する事業部ID
     */
    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * 部門IDを取得する。
     *
     * @return 部門ID
     */
    public Integer getOrganizationId() {
        return organizationId;
    }

    /**
     * 部門IDを設定する。
     *
     * @param organizationId 設定する部門ID
     */
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
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
     * @param clientId 設定する顧客ID
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * プロジェクトマネージャー名を取得する。
     *
     * @return プロジェクトマネージャー名
     */
    public String getProjectManager() {
        return projectManager;
    }

    /**
     * プロジェクトマネージャー名を設定する。
     *
     * @param projectManager 設定するプロジェクトマネージャー名
     */
    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * プロジェクトリーダー名を取得する。
     *
     * @return プロジェクトリーダー名
     */
    public String getProjectLeader() {
        return projectLeader;
    }

    /**
     * プロジェクトリーダー名を設定する。
     *
     * @param projectLeader 設定するプロジェクトリーダー名
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
     * @param note 設定する備考
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
     * @param sales 設定する売上高
     */
    public void setSales(Integer sales) {
        this.sales = sales;
    }

    /**
     * このProjectFormをProjectDtoに変換する。<br>
     *
     * @return このProjectFormの値を保持するProjectDto
     */
    public ProjectDto toProjectDto() {
        ProjectDto projectDto = new ProjectDto();
        BeanUtils.copyProperties(this, projectDto);
        return projectDto;
    }

    /**
     * 開始日、終了日の相関チェックの結果を返す。
     * @return 開始日、終了日の相関チェック結果
     */
    @AssertTrue(message = "{validator.periodConsistencyCheck.message.ProjectCreateForm}")
    public boolean isStartDateBeforeEndDate() {
        if (projectStartDate == null || projectEndDate == null) {
            return true;
        }
        return !projectStartDate.isAfter(projectEndDate);
    }
}
