package com.example.batch.project.writer;


import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.batch.project.mapper.ImportProjectsMapper;
import com.example.common.generated.model.Project;

/**
 * プロジェクト一括登録バッチ/本テーブル登録のItemWriter。
 */
@Component
@StepScope
public class ImportProjectsItemWriter implements ItemWriter<Project> {
    @Autowired
    private ImportProjectsMapper importProjectsMapper;

    @Override
    public void write(Chunk<? extends Project> items) {
        // バッチ実行を行うために同一のMapperメソッドをまとめて呼び出している
        for (Project project : items) {
            if (project.getProjectId() != null) {
                importProjectsMapper.updateProject(project);
            }
        }
        for (Project project : items) {
            if (project.getProjectId() == null) {
                importProjectsMapper.insertProject(project);
            }
        }
    }
}
