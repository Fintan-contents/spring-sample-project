package com.example.web.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.generated.model.Project;
import com.example.web.project.dto.create.OrganizationDto;
import com.example.web.project.dto.create.ProjectDto;
import com.example.web.project.mapper.ProjectCreateMapper;

/**
 * プロジェクト登録機能のService。
 *
 * @author sample
 */
@Service
@Transactional
public class ProjectCreateService {

    /**
     * プロジェクト登録機能のMapper
     */
    @Autowired
    private ProjectCreateMapper mapper;

    /**
     * プロジェクトを登録する。
     *
     * @param projectDto 登録対象のプロジェクト
     */
    public void insertProject(ProjectDto projectDto) {
        Project project = projectDto.toProject();
        project.setVersionNo(1L);
        mapper.insertProject(project);
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
