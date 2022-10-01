package com.example.batch.project.processor;

import com.example.batch.project.item.ImportProjectsToWorkItem;
import com.example.common.generated.model.ProjectWork;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * プロジェクト一括登録バッチ/ワークテーブル登録のItemProcessor。
 */
@Component
@StepScope
public class ImportProjectsToWorkItemProcessor implements ItemProcessor<ImportProjectsToWorkItem, ProjectWork> {
    @Override
    public ProjectWork process(ImportProjectsToWorkItem item) {
        return item.toProjectWork();
    }
}
