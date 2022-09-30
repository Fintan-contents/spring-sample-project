package com.example.batch.project.processor;

import com.example.common.generated.model.Organization;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.batch.common.exception.BatchSkipItemException;
import com.example.batch.project.mapper.ImportProjectsMapper;
import com.example.common.generated.model.Project;
import com.example.common.generated.model.ProjectWork;

/**
 * プロジェクト一括登録バッチ/本テーブル登録のItemProcessor。
 */
@Component
@StepScope
public class ImportProjectsItemProcessor implements ItemProcessor<ProjectWork, Project> {
    @Autowired
    private ImportProjectsMapper importProjectsMapper;

    @Override
    public Project process(ProjectWork work) {
        if (work.getProjectId() != null && importProjectsMapper.selectProjectByIdForUpdate(work.getProjectId()) == null) {
            throw new BatchSkipItemException("errors.project-not-found", work.getProjectWorkId(), work.getProjectId());
        }

        Organization organization = importProjectsMapper.selectOrganizationById(work.getOrganizationId());
        if (organization == null) {
            throw new BatchSkipItemException("errors.organization-not-found", work.getProjectWorkId(), work.getOrganizationId());
        }
        if (organization.getUpperOrganization() == null) {
            throw new BatchSkipItemException("errors.organization-is-division", work.getProjectWorkId(), work.getOrganizationId());
        }

        Project project = new Project();
        BeanUtils.copyProperties(work, project);

        if (project.getProjectId() == null) {
            project.setVersionNo(1L);
        }

        return project;
    }
}
