package com.example.batch.project.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.common.generated.model.ProjectWork;

/**
 * プロジェクト一括登録バッチ/ワークテーブル登録のMapper。
 */
@Mapper
public interface ImportProjectsToWorkMapper {

    /**
     * プロジェクトワークを登録する。
     * @param projectWork プロジェクトワーク
     * @return 更新件数
     */
    int insertProjectWork(ProjectWork projectWork);
}
