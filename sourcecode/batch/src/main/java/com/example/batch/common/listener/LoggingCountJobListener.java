package com.example.batch.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

/**
 * ジョブの入力件数とスキップ件数をログに出力するリスナー。
 */
@Component
public class LoggingCountJobListener implements JobExecutionListener {

    private final Logger logger = LoggerFactory.getLogger(LoggingCountJobListener.class);

    @Override
    public void afterJob(JobExecution jobExecution) {
        int inputCount = 0;
        int skipCount = 0;

        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            inputCount += stepExecution.getReadCount() + stepExecution.getReadSkipCount();
            skipCount += stepExecution.getSkipCount();
        }

        logger.info("入力件数={}, スキップ件数={}", inputCount, skipCount);
    }
}
