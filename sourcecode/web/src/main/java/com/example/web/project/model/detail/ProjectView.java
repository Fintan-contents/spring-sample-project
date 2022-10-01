package com.example.web.project.model.detail;

import com.example.common.generated.model.Project;

/**
 * クエリの結果を投入するModel。
 * 
 * @author sample
 */
public class ProjectView extends Project {

    /** 組織名 */
    private String organizationName;

    /** 上位組織名 */
    private String divisionName;

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
}