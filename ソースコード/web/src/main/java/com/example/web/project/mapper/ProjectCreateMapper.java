package com.example.web.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.common.generated.model.Organization;
import com.example.common.generated.model.Project;

/**
 * プロジェクト登録機能のMapper。
 *
 * @author sample
 */
@Mapper
public interface ProjectCreateMapper {

    /**
     * プロジェクトを登録する。
     *
     * @param project 登録対象のプロジェクト
     * @return 登録件数
     */
    int insertProject(Project project);

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