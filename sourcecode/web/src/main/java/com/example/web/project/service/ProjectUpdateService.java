package com.example.web.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.exception.OptimisticLockException;
import com.example.common.generated.model.Project;
import com.example.web.project.dto.update.OrganizationDto;
import com.example.web.project.dto.update.ProjectDto;
import com.example.web.project.mapper.ProjectUpdateMapper;
import com.example.web.project.model.update.ProjectView;

/**
 * プロジェクト更新機能のService。
 *
 * @author sample
 */
@Service
@Transactional
public class ProjectUpdateService {

    /**
     * プロジェクト更新機能のMapper
     */
    @Autowired
    private ProjectUpdateMapper mapper;

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

    /**
     * プロジェクトを更新する。
     *
     * @param projectDto 更新対象のプロジェクト
     */
    public void updateProject(ProjectDto projectDto) {
        Project project = projectDto.toProject();
        int updateCount = mapper.updateProjectByPrimaryKey(project);
        if (updateCount == 0) {
            throw new OptimisticLockException();
        }
    }

    /**
     * すべての事業部を取得する。
     *
     * @return すべての事業部
     */
    @Transactional(readOnly = true)
    public List<OrganizationDto> selectAllDivision() {
        return mapper
                .selectAllDivision().stream()
                .map(organization -> OrganizationDto.fromOrganization(organization))
                .collect(Collectors.toList());
    }

    /**
     * すべての部門を取得する。
     *
     * @return すべての部門
     */
    @Transactional(readOnly = true)
    public List<OrganizationDto> selectAllDepartment() {
        return mapper
                .selectAllDepartment().stream()
                .map(organization -> OrganizationDto.fromOrganization(organization))
                .collect(Collectors.toList());
    }
}
