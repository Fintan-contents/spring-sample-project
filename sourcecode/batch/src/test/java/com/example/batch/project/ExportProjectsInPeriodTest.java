package com.example.batch.project;

import static org.assertj.core.api.Assertions.*;

import com.example.batch.test.SystemDateTextReplacer;
import com.github.database.rider.core.api.configuration.DBUnit;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.batch.project.configuration.ExportProjectsInPeriodConfig;
import com.example.batch.test.BatchTest;
import com.example.batch.test.BatchTestBase;
import com.example.common.util.BusinessDateSupplier;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

/**
 * 期間内プロジェクト一括出力バッチのテスト。
 * 
 * @author sample
 *
 */
@BatchTest
@SpringBootTest
@DBRider
@DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
class ExportProjectsInPeriodTest extends BatchTestBase {
    private static final String BASE_PATH = "com/example/batch/project/ExportProjectsInPeriodTest";
    private static final Path OUTPUT_FILE = Path.of("work/BA1060101/output/N21AA002.csv");

    @Autowired
    ExportProjectsInPeriodConfig config;
    @Autowired
    BusinessDateSupplier businessDateSupplier;

    JobParameters jobParameters = jobParametersBuilder().toJobParameters();

    @BeforeEach
    void setUp() throws Exception {
        Files.deleteIfExists(OUTPUT_FILE);
    }

    @AfterEach
    void tearDown() {
        businessDateSupplier.setFixedDate(null);
    }

    /**
     * 検索条件の確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testSearchCondition/testSearchCondition.xlsx")
    void testSearchCondition() throws Exception {
        jobLauncher.run(config.exportProjectsInPeriodJob(), jobParameters);

        assertThat(OUTPUT_FILE)
                .usingCharset(StandardCharsets.UTF_8)
                .hasContent(read("testSearchCondition/expected.csv"));
    }

    /**
     * レコードが0件の場合。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testCreateEmptyFileIfNoOutputRecord/testCreateEmptyFileIfNoOutputRecord.xlsx")
    void testCreateEmptyFileIfNoOutputRecord() throws Exception {
        jobLauncher.run(config.exportProjectsInPeriodJob(), jobParameters);

        assertThat(OUTPUT_FILE).isEmptyFile();
    }

    /**
     * 1件の出力。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testOutputSingleRecord/testOutputSingleRecord.xlsx")
    void testOutputSingleRecord() throws Exception {
        jobLauncher.run(config.exportProjectsInPeriodJob(), jobParameters);

        assertThat(OUTPUT_FILE)
                .usingCharset(StandardCharsets.UTF_8)
                .hasContent(read("testOutputSingleRecord/expected.csv"));
    }

    /**
     * ソート順の確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testSort/testSort.xlsx")
    void testSort() throws Exception {
        jobLauncher.run(config.exportProjectsInPeriodJob(), jobParameters);

        assertThat(OUTPUT_FILE)
                .usingCharset(StandardCharsets.UTF_8)
                .hasContent(read("testSort/expected.csv"));
    }

    /**
     * 最大文字数の出力を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testMaxLength/testMaxLength.xlsx")
    void testMaxLength() throws Exception {
        jobLauncher.run(config.exportProjectsInPeriodJob(), jobParameters);

        assertThat(OUTPUT_FILE)
                .usingCharset(StandardCharsets.UTF_8)
                .hasContent(read("testMaxLength/expected.csv"));
    }

    /**
     * 最小文字数の出力を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testMinLength/testMinLength.xlsx")
    void testMinLength() throws Exception {
        jobLauncher.run(config.exportProjectsInPeriodJob(), jobParameters);

        assertThat(OUTPUT_FILE)
                .usingCharset(StandardCharsets.UTF_8)
                .hasContent(read("testMinLength/expected.csv"));
    }

    /**
     * null値の出力を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testOutputNull/testOutputNull.xlsx")
    void testOutputNull() throws Exception {
        jobLauncher.run(config.exportProjectsInPeriodJob(), jobParameters);

        assertThat(OUTPUT_FILE)
                .usingCharset(StandardCharsets.UTF_8)
                .hasContent(read("testOutputNull/expected.csv"));
    }

    /**
     * パラメータで業務日付を設定する。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testSetBusinessDateByJobParameters/testSetBusinessDateByJobParameters.xlsx")
    void testSetBusinessDateByJobParameters() throws Exception {
        JobParameters jobParameters = jobParametersBuilder()
                .addString("businessDate", "20210101")
                .toJobParameters();

        jobLauncher.run(config.exportProjectsInPeriodJob(), jobParameters);

        assertThat(OUTPUT_FILE)
                .usingCharset(StandardCharsets.UTF_8)
                .hasContent(read("testSetBusinessDateByJobParameters/expected.csv"));
    }

    /**
     * 業務日付パラメータが空白の場合、パラメータは使用されない。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testSetBusinessDateJobParameterIsEmpty/testSetBusinessDateJobParameterIsEmpty.xlsx")
    void testSetBusinessDateJobParameterIsBlank() throws Exception {
        JobParameters jobParameters = jobParametersBuilder()
                .addString("businessDate", "   ")
                .toJobParameters();

        jobLauncher.run(config.exportProjectsInPeriodJob(), jobParameters);

        assertThat(OUTPUT_FILE)
                .usingCharset(StandardCharsets.UTF_8)
                .hasContent(read("testSetBusinessDateJobParameterIsEmpty/expected.csv"));
    }

    /**
     * 正常終了時の終了コードを確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testExitCodeNormal/testExitCodeNormal.xlsx")
    void testExitCodeNormal() throws Exception {
        int exitCode = getExitCode(jobLauncher.run(config.exportProjectsInPeriodJob(), jobParameters));
        assertThat(exitCode).isEqualTo(0);
    }
}
