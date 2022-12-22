package com.example.batch.project.tasklet;

import com.example.batch.common.exception.BatchSystemExceptionCreator;
import com.example.batch.project.configuration.UpdateBusinessDateProperties;
import com.example.batch.project.mapper.UpdateBusinessDateMapper;
import com.example.common.generated.model.BusinessDate;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 業務日付更新バッチのTasklet。
 */
@Component
@StepScope
public class UpdateBusinessDateTasklet implements Tasklet {
    @Autowired
    private UpdateBusinessDateMapper updateBusinessDateMapper;
    @Autowired
    private BatchSystemExceptionCreator batchSystemExceptionCreator;
    @Autowired
    private UpdateBusinessDateProperties properties;
    @Value("#{jobParameters['businessDate']}")
    @DateTimeFormat(pattern = "uuuuMMdd")
    private LocalDate jobParameterBusinessDate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        BusinessDate businessDate = updateBusinessDateMapper.selectBusinessDateByPrimaryKeyForUpdate(properties.getSegmentId());

        if (businessDate == null) {
            throw batchSystemExceptionCreator
                    .create("errors.business-date-not-found", properties.getSegmentId());
        }

        LocalDate bizDate = jobParameterBusinessDate != null ? jobParameterBusinessDate : LocalDate.now();
        businessDate.setBizDate(bizDate.format(DateTimeFormatter.ofPattern("uuuuMMdd")));
        updateBusinessDateMapper.updateBusinessDateByPrimaryKey(businessDate);

        return RepeatStatus.FINISHED;
    }
}
