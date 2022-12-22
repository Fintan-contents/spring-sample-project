package com.example.web.project.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.exception.ApplicationException;
import com.example.common.generated.model.ProjectsByUser;
import com.example.common.generated.model.ProjectsByUserRequest;
import com.example.web.project.configuration.ProjectDownloadProperties;
import com.example.web.project.mapper.ProjectDownloadMapper;
import com.example.web.project.model.download.ProjectsByUserView;

/**
 * ユーザ別従事プロジェクトファイルダウンロードのService。
 *
 * @author sample
 */
@Service
@Transactional
public class ProjectDownloadService {

    @Autowired
    private ProjectDownloadMapper mapper;

    /**
     * ログインIDを条件にユーザ別従事プロジェクト情報を検索する。
     * 
     * @param loginId ログインID
     * @return 検索結果
     */
    @Transactional(readOnly = true)
    public ProjectsByUserView selectProjectsByUserByLoginId(String loginId) {
        return mapper.selectProjectsByUserByLoginId(loginId);
    }

    /**
     * ユーザ別従事プロジェクトの作成を要求できるかチェックする。
     * 
     * @param loginId ログインID
     */
    public void validate(String loginId) {
        ProjectsByUserView model = mapper.selectProjectsByUserByLoginId(loginId);
        if (model.isNotBeAbleToCreateFile()) {
            throw ApplicationException.globalError("errors.projectDownload.illegalState");
        }
    }

    /**
     * ユーザ別従事プロジェクトの作成を要求する。
     * 
     * @param loginId ログインID
     */
    public void request(String loginId) {
        ProjectsByUserView model = mapper.selectProjectsByUserByLoginId(loginId);
        if (model.isNotBeAbleToCreateFile()) {
            throw ApplicationException.globalError("errors.projectDownload.illegalState");
        }

        ProjectsByUserRequest request = new ProjectsByUserRequest();
        request.setRequestDatetime(LocalDateTime.now());
        request.setStatus(ProjectDownloadProperties.STATUS_UNPROCESSED);
        request.setUserId(model.getUserId());
        mapper.insertProjectsByUserRequest(request);

        ProjectsByUser projectsByUser = new ProjectsByUser();
        projectsByUser.setUserId(model.getUserId());
        projectsByUser.setRequestId(request.getId());
        mapper.updateProjectsByUser(projectsByUser);
    }
}
