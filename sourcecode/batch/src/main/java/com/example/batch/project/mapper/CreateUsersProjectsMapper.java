package com.example.batch.project.mapper;

import com.example.common.generated.model.Project;
import com.example.common.generated.model.ProjectsByUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ユーザ別従事プロジェクト抽出バッチのMapper。
 */
@Mapper
public interface CreateUsersProjectsMapper {

    /**
     * 指定された要求IDに紐づくユーザが従事するプロジェクトを取得する。
     * @param requestId 要求ID
     * @return 指定された要求IDに紐づくユーザが従事するプロジェクトの一覧
     */
    List<Project> selectProjectsByRequestId(long requestId);

    /**
     * ユーザ別従事プロジェクトに出力したファイルの情報などを書き出す。
     * @param projectsByUser 更新情報を保持したユーザ別従事プロジェクト
     * @return 更新件数
     */
    int updateProjectsByUserSetOutputFileInformationByRequestId(ProjectsByUser projectsByUser);
}
