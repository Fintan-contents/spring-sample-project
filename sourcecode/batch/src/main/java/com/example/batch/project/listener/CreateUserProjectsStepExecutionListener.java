package com.example.batch.project.listener;

import com.example.batch.project.configuration.CreateUsersProjectsProperties;
import com.example.batch.project.mapper.CreateUsersProjectsMapper;
import com.example.common.generated.model.ProjectsByUser;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * ユーザ別従事プロジェクト抽出バッチのStepExecutionListener。
 */
@Component
@StepScope
public class CreateUserProjectsStepExecutionListener implements StepExecutionListener {
    @Value("#{jobParameters['request.id']}")
    private long requestId;
    @Autowired
    private CreateUsersProjectsProperties properties;
    @Autowired
    private CreateUsersProjectsMapper createUsersProjectsMapper;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String fileName = properties.getOutputFileNamePrefix() + requestId + ".csv";
        LocalDateTime createDatetime = LocalDateTime.now();
        LocalDateTime expireDatetime = createDatetime.plusWeeks(1);

        ProjectsByUser projectsByUser = new ProjectsByUser();
        projectsByUser.setRequestId(requestId);
        projectsByUser.setFileName(fileName);
        projectsByUser.setCreateDatetime(createDatetime);
        projectsByUser.setExpireDatetime(expireDatetime);

        createUsersProjectsMapper.updateProjectsByUserSetOutputFileInformationByRequestId(projectsByUser);

        return null;
    }
}
