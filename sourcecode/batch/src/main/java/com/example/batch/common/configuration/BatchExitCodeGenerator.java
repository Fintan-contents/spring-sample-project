package com.example.batch.common.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;
import org.springframework.context.ApplicationListener;

/**
 * バッチの終了コードを決定するクラス。
 * <p>
 * このクラスは、以下の順序で終了コードを決定する。
 * <ul>
 *   <li>障害が発生した場合: 1</li>
 *   <li>警告終了の条件を満たす場合: 2</li>
 *   <li>上記以外の場合: 0</li>
 * </ul>
 * </p>
 */
public class BatchExitCodeGenerator implements ExitCodeGenerator, ApplicationListener<JobExecutionEvent> {

    private final List<JobExecution> executions = new ArrayList<>();

    @Override
    public void onApplicationEvent(JobExecutionEvent event) {
        executions.add(event.getJobExecution());
    }

    @Override
    public int getExitCode() {
        if (existsFailure()) {
            return 1;
        }
        if (existsWarning()) {
            return 2;
        }
        return 0;
    }

    /**
     * 内部に保持しているJobExecutionをクリアする。
     * 
     */
    public void clearJobExecutions() {
        executions.clear();
    }

    /**
     * 警告終了の条件を満たすかチェックする。
     * 
     * @return 警告終了の条件を満たしている場合はtrueを返す
     */
    private boolean existsWarning() {
        for (JobExecution execution : executions) {
            for (StepExecution stepExecution : execution.getStepExecutions()) {
                if (0 < stepExecution.getSkipCount()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 異常終了の条件を満たすかチェックする。
     * 
     * @return 異常終了の条件を満たしている場合はtrueを返す
     */
    private boolean existsFailure() {
        for (JobExecution execution : executions) {
            for (StepExecution stepExecution : execution.getStepExecutions()) {
                if (!stepExecution.getFailureExceptions().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
}
