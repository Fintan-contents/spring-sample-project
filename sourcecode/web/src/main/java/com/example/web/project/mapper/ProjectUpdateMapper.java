package com.example.web.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.common.generated.model.Organization;
import com.example.common.generated.model.Project;
import com.example.web.project.model.update.ProjectView;

/**
 * プロジェクト更新機能のMapper。
 *
 * @author sample
 */
@Mapper
public interface ProjectUpdateMapper {

    /**
     * 指定されたプロジェクトを取得する。
     * 
     * @param projectId プロジェクトID
     * @return 指定されたプロジェクト。見つからない場合はnullを返す。
     */
    ProjectView selectProjectByPrimaryKey(Integer projectId);

    /**
     * プロジェクトを更新する。
     *
     * @param project 更新対象のプロジェクト
     * @return 更新件数
     */
    int updateProjectByPrimaryKey(Project project);

    /**
     * 指定された組織の名前を取得する。
     *
     * @param organizationId 組織ID
     * @return 指定された組織の名前。見つからない場合はnullを返す。
     */
    String selectOrganizationNameByPrimaryKey(Integer organizationId);

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