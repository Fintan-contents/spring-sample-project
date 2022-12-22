package com.example.batch.common.resident;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.stereotype.Component;

import com.example.batch.common.resident.configuration.ResidentBatchProperties;
import com.example.batch.common.resident.service.ResidentBatchService;

/**
 * 常駐バッチの制御ロジック実装クラス。
 *
 */
@Component
public class ResidentBatchProcessorImpl implements ResidentBatchProcessor {

    private final Logger logger = LoggerFactory.getLogger(ResidentBatchProcessorImpl.class);

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private ResidentBatchProperties residentBatchProperties;
    @Autowired
    private Collection<Job> jobs;
    @Autowired(required = false)
    private JobParametersConverter converter = new DefaultJobParametersConverter();
    @Autowired
    private ResidentBatchService service;

    private Job job;
    private RetryTemplate retryTemplate;

    private final AtomicBoolean initialized = new AtomicBoolean();

    @Override
    public boolean process() {
        if (initialized.get() == false) {
            service.initialize(residentBatchProperties.getJobId());
            initialized.set(true);
        }

        if (service.isRunning(residentBatchProperties.getJobId()) == false) {
            return false;
        }

        ResidentBatchProperties.RequestProperties requestProperties = residentBatchProperties.getRequest();

        List<?> primaryKeys = service
                .selectRequestPrimaryKeyByStatus(requestProperties.getUnprocessedStatus());

        for (Object primaryKey : primaryKeys) {
            service.updateRequestStatusByPrimaryKey(requestProperties.getProcessingStatus(), primaryKey);
            try {
                retryTemplate.execute(retryContext -> {

                    Properties properties = new Properties();
                    JobParameters jobParameters = converter.getJobParameters(properties);
                    JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(jobParameters);
                    if (primaryKey instanceof Long) {
                        jobParametersBuilder = jobParametersBuilder.addLong("request.id", (Long) primaryKey);
                    } else {
                        jobParametersBuilder = jobParametersBuilder.addString("request.id", String.valueOf(primaryKey));
                    }
                    jobParameters = jobParametersBuilder.toJobParameters();
                    JobExecution jobExecution = jobLauncher.run(job, jobParameters);

                    Iterator<Throwable> exceptions = jobExecution
                            .getAllFailureExceptions()
                            .iterator();
                    if (exceptions.hasNext()) {
                        throw exceptions.next();
                    }

                    return null;
                });
                service.updateRequestStatusByPrimaryKey(requestProperties.getSuccessStatus(), primaryKey);

            } catch (Throwable t) {
                logger
                        .error("要求テーブル {} の主キーが {} のデータを処理中にエラーが発生しました",
                                residentBatchProperties.getRequest().getTableName(),
                                primaryKey,
                                t);
                service.updateRequestStatusByPrimaryKey(requestProperties.getFailureStatus(), primaryKey);
            }
        }
        return true;
    }

    @Override
    public void initialize() {

        // 指定されたジョブIDに紐づく要求テーブルの設定が取得できることを確認
        Objects.requireNonNull(residentBatchProperties.getRequest(), "ジョブIDに紐づく要求テーブルの設定を取得できませんでした");

        this.job = Objects
                .requireNonNull(jobs
                        .stream()
                        .filter(a -> a
                                .getName()
                                .equals(residentBatchProperties.getSpringBatchJobName()))
                        .findAny().orElse(null), "Jobを特定できませんでした");

        // リトライの設定を行う
        ResidentBatchProperties.RetryProperties retry = residentBatchProperties.getRetry();
        ResidentBatchProperties.BackoffProperties backoff = retry.getBackoff();
        this.retryTemplate = new RetryTemplateBuilder()
                .exponentialBackoff(
                        backoff.getInitialInterval(),
                        backoff.getMultiplier(),
                        backoff.getMaxInterval(),
                        backoff.isWithRandom())
                .maxAttempts(retry.getMaxAttempts())
                .notRetryOn(retry.getNotRetryOn())
                .build();

        service.validate();
    }
}
