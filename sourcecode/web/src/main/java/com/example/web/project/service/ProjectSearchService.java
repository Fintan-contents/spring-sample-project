package com.example.web.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.web.project.dto.search.OrganizationDto;
import com.example.web.project.dto.search.ProjectDto;
import com.example.web.project.mapper.ProjectSearchMapper;
import com.example.web.project.model.search.ProjectSearchCriteria;

/**
 * プロジェクト検索機能のService。
 *
 * @author sample
 */
@Service
@Transactional
public class ProjectSearchService {

    /**
     * プロジェクト検索機能のMapper
     */
    @Autowired
    private ProjectSearchMapper mapper;

    /**
     * プロジェクトを検索する。
     * 
     * @param criteria 検索条件
     * @return 検索結果
     */
    @Transactional(readOnly = true)
    public Page<ProjectDto> selectProjectes(ProjectSearchCriteria criteria) {
        int recordSize = mapper.countProjectByCriteria(criteria);
        List<ProjectDto> content = mapper
                .selectProjectByCriteria(criteria).stream()
                .map(project -> ProjectDto.fromProject(project))
                .collect(Collectors.toList());
        return new PageImpl<>(content, criteria.getPageable(), recordSize);

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
