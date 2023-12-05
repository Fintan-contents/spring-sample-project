package com.example.batch.project;

import static org.assertj.core.api.Assertions.*;

import com.example.batch.test.SystemDateTextReplacer;
import com.github.database.rider.core.api.configuration.DBUnit;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.batch.project.configuration.ImportProjectsConfig;
import com.example.batch.project.configuration.ImportProjectsToWorkConfig;
import com.example.batch.test.BatchTest;
import com.example.batch.test.BatchTestBase;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

/**
 * プロジェクト一括登録バッチのテスト。
 * 
 * @author sample
 *
 */
@BatchTest
@SpringBootTest
@DBRider
//@DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
public class ImportProjectsIntegrationTest extends BatchTestBase {
    private static final String BASE_PATH = "com/example/batch/project/ImportProjectsIntegrationTest";
    private static final String INPUT_FILE = "work/BA1060201/input/N21AA001.csv";

    @Autowired
    ImportProjectsToWorkConfig importProjectsToWorkConfig;
    @Autowired
    ImportProjectsConfig importProjectsConfig;

    @BeforeEach
    void setUp() throws Exception {
        Files.deleteIfExists(Path.of(INPUT_FILE));
    }

    /**
     * 取引単体テスト。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testIntegration/testIntegration.xlsx",
            executeScriptsBefore = BASE_PATH + "/testIntegration/reset_sequence_val_project.sql")
    @ExpectedDataSet(BASE_PATH + "/testIntegration/expected-testIntegration.xlsx")
    void testIntegration() throws Exception {
        copy("testIntegration/input.csv", INPUT_FILE);

        JobParameters jobParametersForToWork = jobParametersBuilder().toJobParameters();
        jobLauncher.run(importProjectsToWorkConfig.importProjectsToWorkJob(), jobParametersForToWork);

        JobParameters jobParametersForWorkToProject = jobParametersBuilder().toJobParameters();
        jobLauncher.run(importProjectsConfig.importProjectsJob(), jobParametersForWorkToProject);
    }

    /**
     * 取引単体テスト(リラン)。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testIntegrationRerun/testIntegrationRerun.xlsx",
            executeScriptsBefore = BASE_PATH + "/testIntegrationRerun/reset_sequence_val_project.sql")
    @ExpectedDataSet(BASE_PATH + "/testIntegrationRerun/expected-testIntegrationRerun.xlsx")
    void testIntegrationRerun() throws Exception {
        copy("testIntegrationRerun/input.csv", INPUT_FILE);

        JobParameters jobParametersForToWork = jobParametersBuilder().toJobParameters();
        jobLauncher.run(importProjectsToWorkConfig.importProjectsToWorkJob(), jobParametersForToWork);

        JobParameters jobParametersForWorkToProject = jobParametersBuilder().toJobParameters();
        JobExecution jobExecution = jobLauncher.run(importProjectsConfig.importProjectsJob(), jobParametersForWorkToProject);
        assertThat(getExitCode(jobExecution)).isEqualTo(2);

        // リランのため修正したCSVを準備する
        copy("testIntegrationRerun/input-rerun.csv", INPUT_FILE);

        jobParametersForToWork = jobParametersBuilder().toJobParameters();
        jobLauncher.run(importProjectsToWorkConfig.importProjectsToWorkJob(), jobParametersForToWork);

        jobParametersForWorkToProject = jobParametersBuilder().toJobParameters();
        jobExecution = jobLauncher.run(importProjectsConfig.importProjectsJob(), jobParametersForWorkToProject);
        assertThat(getExitCode(jobExecution)).isEqualTo(0);
    }
}
