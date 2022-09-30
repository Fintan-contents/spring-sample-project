package com.example.web.project.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.web.project.model.detail.ProjectView;

/**
 * プロジェクト詳細機能のMapper。
 *
 * @author sample
 */
@Mapper
public interface ProjectDetailMapper {

    /**
     * 指定されたプロジェクトを取得する。
     * 
     * @param projectId プロジェクトID
     * @return 指定されたプロジェクト。見つからない場合はnullを返す。
     */
    ProjectView selectProjectByPrimaryKey(Integer projectId);
}