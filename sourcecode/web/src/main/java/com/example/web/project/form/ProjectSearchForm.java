package com.example.web.project.form;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.web.common.validation.ArrayDomain;
import com.example.web.project.model.search.ProjectSearchCriteria;

import nablarch.core.validation.ee.Domain;

/**
 * プロジェクト検索機能のForm。
 * 
 * @author sample
 */
public class ProjectSearchForm implements Serializable {

    /** 事業部 */
    @Domain("organizationId")
    private Integer divisionId;

    /** 部門 */
    @Domain("organizationId")
    private Integer organizationId;

    /** プロジェクト種別 */
    @ArrayDomain("projectType")
    private String[] projectTypes = {};

    /** プロジェクト分類 */
    @ArrayDomain("projectClass")
    private String[] projectClasses = {};

    /** 売上高_実績_FROM */
    @Domain("amountOfMoney")
    private Integer salesFrom;

    /** 売上高_実績_TO */
    @Domain("amountOfMoney")
    private Integer salesTo;

    /** 開始日_FROM */
    @DateTimeFormat(pattern = "uuuu-MM-dd")
    private LocalDate projectStartDateFrom;

    /** 開始日_TO */
    @DateTimeFormat(pattern = "uuuu-MM-dd")
    private LocalDate projectStartDateTo;

    /** 終了日_FROM */
    @DateTimeFormat(pattern = "uuuu-MM-dd")
    private LocalDate projectEndDateFrom;

    /** 終了日_TO */
    @DateTimeFormat(pattern = "uuuu-MM-dd")
    private LocalDate projectEndDateTo;

    /** プロジェクト名 */
    @Domain("projectName")
    private String projectName;

    /** ページNo */
    @Domain("pageNumber")
    private Integer pageNumber;

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
     * プロジェクト種別 を設定する。
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
     * プロジェクト分類 を設定する。
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
     * ページ番号を取得する。
     * @return ページ番号
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * ページ番号を設定する
     * @param pageNumber ページ番号
     */
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * 売上高_実績_FROM、売上高_実績_TOの相関チェック結果を返す。
     * @return 売上高_実績_FROM、売上高_実績_TOの相関チェック結果
     */
    @AssertTrue(message = "{validator.priceRange.message}")
    public boolean isSalesFromLessThanSalesTo() {
        if (salesFrom == null || salesTo == null) {
            return true;
        }
        return salesFrom <= salesTo;
    }

    /**
     * 開始日_FROM、開始日_TOの相関チェックの結果を返す。
     * @return 日付の範囲。開始日_FROM、開始日_TOの相関チェック結果
     */
    @AssertTrue(message = "{validator.dateRange.message}")
    public boolean isStartDateFromBeforeToDate() {
        if (projectStartDateFrom == null || projectStartDateTo == null) {
            return true;
        }
        return !projectStartDateFrom.isAfter(projectStartDateTo);
    }

    /**
     * 終了日_FROM、終了日_TOの相関チェックの結果を返す
     * @return 日付の範囲。終了日_FROM、終了日_TOの相関チェック結果
     */
    @AssertTrue(message = "{validator.dateRange.message}")
    public boolean isEndDateFromBeforeToDate() {
        if (projectEndDateFrom == null || projectEndDateTo == null) {
            return true;
        }
        return !projectEndDateFrom.isAfter(projectEndDateTo);
    }

    /**
     * このProjectSearchFormをProjectSearchCriteriaに変換する
     * @param size 1ページに表示するプロジェクトの件数
     * @return このProjectSearchFormの値を保持するProjectSearchCriteria
     */
    public ProjectSearchCriteria toProjectSearchCriteria(int size) {
        ProjectSearchCriteria criteria = new ProjectSearchCriteria();
        BeanUtils.copyProperties(this, criteria);

        Pageable pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, size);
        criteria.setPageable(pageable);

        return criteria;
    }

    /**
     * このフォームの値すべて消去する
     */
    public void clear() {
        ProjectSearchForm newForm = new ProjectSearchForm();
        BeanUtils.copyProperties(newForm, this);
    }
}
