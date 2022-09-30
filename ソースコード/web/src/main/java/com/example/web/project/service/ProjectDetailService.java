package com.example.web.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.web.project.dto.detail.ProjectDto;
import com.example.web.project.mapper.ProjectDetailMapper;
import com.example.web.project.model.detail.ProjectView;

/**
 * プロジェクト詳細機能のService。
 *
 * @author sample
 */
@Service
@Transactional
public class ProjectDetailService {

    /**
     * プロジェクト詳細機能のMapper
     */
    @Autowired
    private ProjectDetailMapper mapper;

    /**
     * 指定されたプロジェクトを取得する。
     * 
     * @param projectId プロジェクトID
     * @return 指定されたプロジェクト。見つからない場合はnullを返す。
     */
    @Transactional(readOnly = true)
    public ProjectDto findProjectById(Integer projectId) {
        ProjectView projectView = mapper.selectProjectByPrimaryKey(projectId);
        if (projectView == null) {
            return null;
        }
        ProjectDto projectDto = ProjectDto.fromProject(projectView);
        return projectDto;
    }
}
