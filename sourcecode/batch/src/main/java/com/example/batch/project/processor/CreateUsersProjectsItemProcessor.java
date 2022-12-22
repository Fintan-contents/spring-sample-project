package com.example.batch.project.processor;


import com.example.batch.project.item.CreateUsersProjectsItem;
import com.example.common.generated.model.Project;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * ユーザ別従事プロジェクト抽出バッチのItemProcessor。
 */
@Component
@StepScope
public class CreateUsersProjectsItemProcessor implements ItemProcessor<Project, CreateUsersProjectsItem> {
    @Override
    public CreateUsersProjectsItem process(Project project) throws Exception {
        return CreateUsersProjectsItem.from(project);
    }
}
