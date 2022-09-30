package com.example.web.project.model.search;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

/**
 * プロジェクト検索の検索条件を表すModel。
 * 
 * @author sample
 */
public class ProjectSearchCriteria {

    /** 事業部 */
    private Integer divisionId;

    /** 部門 */
    private Integer organizationId;

    /** プロジェクト種別 */
    private String[] projectTypes;

    /** プロジェクト分類 */
    private String[] projectClasses;

    /** 売上高_実績_FROM */
    private Integer salesFrom;

    /** 売上高_実績_TO */
    private Integer salesTo;

    /** 開始日_FROM */
    private LocalDate projectStartDateFrom;

    /** 開始日_TO */
    private LocalDate projectStartDateTo;

    /** 終了日_FROM */
    private LocalDate projectEndDateFrom;

    /** 終了日_TO */
    private LocalDate projectEndDateTo;

    /** プロジェクト名 */
    private String projectName;

    /** ページングメタ情報 */
    private Pageable pageable;

    /**
     * 事業部 を取得する。
     *
     * @return 事業部
     */
    public Integer getDivisionId() {
        return divisionId;
    }

    /**
     * 事業部 を設定する
     *
     * @param divisionId 事業部
     */
    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * 部門 を取得する。
     *
     * @return 部門
     */
    public Integer getOrganizationId() {
        return organizationId;
    }

    /**
     * 部門 を設定する
     *
     * @param organizationId 部門
     */
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * プロジェクト種別 を取得する。
     *
     * @return プロジェクト種別
     */
    public String[] getProjectTypes() {
        return projectTypes;
    }

    /**
     * プロジェクト種別 を設定する
     *
     * @param projectTypes プロジェクト種別
     */
    public void setProjectTypes(String[] projectTypes) {
        this.projectTypes = projectTypes;
    }

    /**
     * プロジェクト分類 を取得する。
     *
     * @return プロジェクト分類
     */
    public String[] getProjectClasses() {
        return projectClasses;
    }

    /**
     * プロジェクト分類 を設定する
     *
     * @param projectClasses プロジェクト分類
     */
    public void setProjectClasses(String[] projectClasses) {
        this.projectClasses = projectClasses;
    }

    /**
     * 売上高_実績_FROM を取得する。
     *
     * @return 売上高_実績_FROM
     */
    public Integer getSalesFrom() {
        return salesFrom;
    }

    /**
     * 売上高_実績_FROM を設定する
     *
     * @param salesFrom 売上高_実績_FROM
     */
    public void setSalesFrom(Integer salesFrom) {
        this.salesFrom = salesFrom;
    }

    /**
     * 売上高_実績_TO を取得する。
     *
     * @return 売上高_実績_TO
     */
    public Integer getSalesTo() {
        return salesTo;
    }

    /**
     * 売上高_実績_TO を設定する
     *
     * @param salesTo 売上高_実績_TO
     */
    public void setSalesTo(Integer salesTo) {
        this.salesTo = salesTo;
    }

    /**
     * 開始日_FROM を取得する。
     *
     * @return 開始日_FROM
     */
    public LocalDate getProjectStartDateFrom() {
        return projectStartDateFrom;
    }

    /**
     * 開始日_FROM を設定する
     *
     * @param projectStartDateFrom 開始日_FROM
     */
    public void setProjectStartDateFrom(LocalDate projectStartDateFrom) {
        this.projectStartDateFrom = projectStartDateFrom;
    }

    /**
     * 開始日_TO を取得する。
     *
     * @return 開始日_TO
     */
    public LocalDate getProjectStartDateTo() {
        return projectStartDateTo;
    }

    /**
     * 開始日_TO を設定する
     *
     * @param projectStartDateTo 開始日_TO
     */
    public void setProjectStartDateTo(LocalDate projectStartDateTo) {
        this.projectStartDateTo = projectStartDateTo;
    }

    /**
     * 終了日_FROM を取得する。
     *
     * @return 終了日_FROM
     */
    public LocalDate getProjectEndDateFrom() {
        return projectEndDateFrom;
    }

    /**
     * 終了日_FROM を設定する
     *
     * @param projectEndDateFrom 終了日_FROM
     */
    public void setProjectEndDateFrom(LocalDate projectEndDateFrom) {
        this.projectEndDateFrom = projectEndDateFrom;
    }

    /**
     * 終了日_TO を取得する。
     *
     * @return 終了日_TO
     */
    public LocalDate getProjectEndDateTo() {
        return projectEndDateTo;
    }

    /**
     * 終了日_TO を設定する
     *
     * @param projectEndDateTo 終了日_TO
     */
    public void setProjectEndDateTo(LocalDate projectEndDateTo) {
        this.projectEndDateTo = projectEndDateTo;
    }

    /**
     * プロジェクト名 を取得する。
     *
     * @return プロジェクト名
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * プロジェクト名 を設定する
     *
     * @param projectName プロジェクト名
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * ページングメタ情報を取得する
     * @return ページングメタ情報
     */
    public Pageable getPageable() {
        return pageable;
    }

    /**
     * ページングメタ情報を設定する
     * @param pageable ページングメタ情報
     */
    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}
