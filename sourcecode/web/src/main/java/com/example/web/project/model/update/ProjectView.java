package com.example.web.project.model.update;

import com.example.common.generated.model.Project;

/**
 * クエリの結果を投入するModel。
 * 
 * @author sample
 */
public class ProjectView extends Project {

    /** 上位組織ID */
    private Integer divisionId;

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
}