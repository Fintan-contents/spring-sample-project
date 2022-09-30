package com.example.batch.project.mapper;

import com.example.common.generated.model.Organization;
import com.example.common.generated.model.Project;
import com.example.common.generated.model.ProjectWork;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * プロジェクト一括登録バッチ/本テーブル登録のMapper。
 */
@Mapper
public interface ImportProjectsMapper {

    /**
     * プロジェクトを排他ロックをかけてIDで検索する。
     * @param projectId プロジェクトID
     * @return 検索結果
     */
    Project selectProjectByIdForUpdate(int projectId);

    /**
     * 部門をIDで検索する。
     * @param organizationId 部門ID
     * @return 検索結果
     */
    Organization selectOrganizationById(int organizationId);

    /**
     * ワークテーブルから登録対象となるレコードを検索する。
     * @param businessDate 業務日付
     * @return 登録対象のレコード一覧
     */
    List<ProjectWork> selectProjectWorksInPeriod(LocalDate businessDate);

    /**
     * プロジェクトを更新する。
     * <p>
     * このメソッドは、バージョンNoのインクリメントを行う。
     * </p>
     * @param project 更新対象のプロジェクト
     * @return 更新件数
     */
    int updateProject(Project project);

    /**
     * プロジェクトを登録する。
     * @param project プロジェクト
     * @return 更新件数
     */
    int insertProject(Project project);
}
