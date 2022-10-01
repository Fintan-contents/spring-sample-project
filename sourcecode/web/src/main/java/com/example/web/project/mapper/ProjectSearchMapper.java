package com.example.web.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.common.generated.model.Organization;
import com.example.web.project.model.search.ProjectView;
import com.example.web.project.model.search.ProjectSearchCriteria;

/**
 * プロジェクト検索機能のMapper。
 *
 * @author sample
 */
@Mapper
public interface ProjectSearchMapper {

    /**
     * プロジェクトを検索する。
     * 
     * @param criteria 検索条件
     * @return 検索結果
     */
    List<ProjectView> selectProjectByCriteria(ProjectSearchCriteria criteria);

    /**
     * プロジェクトをカウントする。
     * 
     * @param criteria 検索条件
     * @return 件数
     */
    int countProjectByCriteria(ProjectSearchCriteria criteria);

    /**
     * すべての事業部を取得する。
     *
     * @return すべての事業部
     */
    List<Organization> selectAllDivision();

    /**
     * すべての部門を取得する。
     *
     * @return すべての部門
     */
    List<Organization> selectAllDepartment();
}