package com.example.web.project.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.common.generated.model.ProjectsByUser;
import com.example.common.generated.model.ProjectsByUserRequest;
import com.example.web.project.model.download.ProjectsByUserView;

/**
 * ユーザ別従事プロジェクトファイルダウンロードのMapper。
 * 
 * @author sample
 */
@Mapper
public interface ProjectDownloadMapper {

    /**
     * ログインIDを条件にユーザ別従事プロジェクト情報を検索する。
     * 
     * @param loginId ログインID
     * @return 検索結果
     */
    ProjectsByUserView selectProjectsByUserByLoginId(String loginId);

    /**
     * ユーザ別従事プロジェクト作成要求を登録する。
     * 
     * @param model ユーザ別従事プロジェクト作成要求
     * @return 登録件数
     */
    int insertProjectsByUserRequest(ProjectsByUserRequest model);

    /**
     * ユーザ別従事プロジェクト情報を更新する。
     * 
     * @param model ユーザ別従事プロジェクト情報
     * @return 更新件数
     */
    int updateProjectsByUser(ProjectsByUser model);
}
