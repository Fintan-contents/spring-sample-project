package com.example.batch.project.processor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.batch.project.item.ExportProjectsInPeriodItem;
import com.example.common.generated.model.Project;

/**
 * 期間内プロジェクト一覧出力バッチのItemProcessor。
 */
@Component
@StepScope
public class ExportProjectsInPeriodItemProcessor implements ItemProcessor<Project, ExportProjectsInPeriodItem> {

    @Override
    public ExportProjectsInPeriodItem process(Project project) {
        return ExportProjectsInPeriodItem.from(project);
    }
}
