package com.example.batch.project;

import ch.qos.logback.classic.Level;
import com.example.batch.project.configuration.UpdateBusinessDateConfig;
import com.example.batch.project.configuration.UpdateBusinessDateProperties;
import com.example.batch.test.BatchTest;
import com.example.batch.test.BatchTestBase;
import com.example.batch.test.SystemDateTextReplacer;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 業務日付更新バッチのテスト。
 *
 * @author sample
 */
@BatchTest
@SpringBootTest
@DBRider
@DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
public class UpdateBusinessDateTest extends BatchTestBase {
    private static final String BASE_PATH = "com/example/batch/project/UpdateBusinessDateTest";

    @Autowired
    UpdateBusinessDateConfig config;
    @Autowired
    UpdateBusinessDateProperties properties;

    JobParameters jobParameters = jobParametersBuilder().toJobParameters();

    /**
     * 業務日付を指定せずに正常終了した場合のテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testNormal/testNormal.xlsx"
    )
    @ExpectedDataSet(BASE_PATH + "/testNormal/expected-testNormal.xlsx")
    void testNormal() throws Exception {
        properties.setSegmentId("00");
        jobLauncher.run(config.updateBusinessDateJob(), jobParameters);
    }

    /**
     * 業務日付を指定して正常終了した場合のテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testSpecifyBusinessDate/testSpecifyBusinessDate.xlsx"
    )
    @ExpectedDataSet(BASE_PATH + "/testSpecifyBusinessDate/expected-testSpecifyBusinessDate.xlsx")
    void testSpecifyBusinessDate() throws Exception {
        properties.setSegmentId("01");
        JobParameters jobParameters = jobParametersBuilder()
                .addString("businessDate", "20221231")
                .toJobParameters();

        int exitCode = getExitCode(jobLauncher.run(config.updateBusinessDateJob(), jobParameters));
        assertThat(exitCode).isEqualTo(0);
    }

    /**
     * 業務日付を指定して正常終了した場合のテスト。（ISO 8601形式）
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testSpecifyBusinessDate/testSpecifyBusinessDate.xlsx"
    )
    @ExpectedDataSet(BASE_PATH + "/testSpecifyBusinessDate/expected-testSpecifyBusinessDate.xlsx")
    void testSpecifyBusinessDate_ISO8601Format() throws Exception {
        properties.setSegmentId("01");
        JobParameters jobParameters = jobParametersBuilder()
                .addString("businessDate", "2022-12-31")
                .toJobParameters();

        int exitCode = getExitCode(jobLauncher.run(config.updateBusinessDateJob(), jobParameters));
        assertThat(exitCode).isEqualTo(0);
    }

    /**
     * 業務日付に最大長より大きい文字列を指定した場合のエラーのテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testOverMaxLengthBusinessDate() throws Exception {
        properties.setSegmentId("00");
        JobParameters jobParameters = jobParametersBuilder()
                .addString("businessDate", "202212319")
                .toJobParameters();

        int exitCode = getExitCode(jobLauncher.run(config.updateBusinessDateJob(), jobParameters));
        assertThat(exitCode).isEqualTo(1);
    }

    /**
     * 業務日付に不正な文字種別を指定した場合のエラーのテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testInvalidCharacterBusinessDate() throws Exception {
        properties.setSegmentId("00");
        JobParameters jobParameters = jobParametersBuilder()
                .addString("businessDate", "abcdefgh")
                .toJobParameters();

        int exitCode = getExitCode(jobLauncher.run(config.updateBusinessDateJob(), jobParameters));
        assertThat(exitCode).isEqualTo(1);
        assertLogContainsInException(Level.ERROR, "Text 'abcdefgh' could not be parsed at index 0");
    }

    /**
     * 業務日付に不正な書式文字列を指定した場合のエラーのテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testInvalidFormatBusinessDate() throws Exception {
        properties.setSegmentId("00");
        JobParameters jobParameters = jobParametersBuilder()
                .addString("businessDate", "2022.12.31")
                .toJobParameters();

        int exitCode = getExitCode(jobLauncher.run(config.updateBusinessDateJob(), jobParameters));
        assertThat(exitCode).isEqualTo(1);
    }

    /**
     * 業務日付に存在しない日付を指定した場合のエラーのテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testNoExistBusinessDate() throws Exception {
        properties.setSegmentId("00");
        JobParameters jobParameters = jobParametersBuilder()
                .addString("businessDate", "20221232")
                .toJobParameters();

        int exitCode = getExitCode(jobLauncher.run(config.updateBusinessDateJob(), jobParameters));
        assertThat(exitCode).isEqualTo(1);
    }

    /**
     * 存在しない区分を指定した場合のエラーメッセージと終了ステータスのテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testErrorMessageAndStatusWhenNoExistSegmentId/testErrorMessageAndStatusWhenNoExistSegmentId.xlsx"
    )
    void testErrorMessageAndStatusWhenNoExistSegmentId() throws Exception {
        properties.setSegmentId("99");

        int exitCode = getExitCode(jobLauncher.run(config.updateBusinessDateJob(), jobParameters));

        assertThat(exitCode).isEqualTo(1);
        assertLogContainsInException("業務日付が存在しません(segment_id=99)");
    }

    /**
     * 存在しない区分を指定した場合に、DBが更新されていないことのテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testDBDoesNotBeUpdatedWhenNoExistSegmentId/testDBDoesNotBeUpdatedWhenNoExistSegmentId.xlsx"
    )
    @ExpectedDataSet(BASE_PATH + "/testDBDoesNotBeUpdatedWhenNoExistSegmentId/expected-testDBDoesNotBeUpdatedWhenNoExistSegmentId.xlsx")
    void testDBDoesNotBeUpdatedWhenNoExistSegmentId() throws Exception {
        properties.setSegmentId("99");

        jobLauncher.run(config.updateBusinessDateJob(), jobParameters);
    }

    /**
     * リランのテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testRerun/testRerun.xlsx"
    )
    @ExpectedDataSet(BASE_PATH + "/testRerun/expected-testRerun.xlsx")
    void testRerun() throws Exception {
        properties.setSegmentId("00");

        JobParameters jobParameters1 = jobParametersBuilder()
                .addString("businessDate", "20221231")
                .toJobParameters();

        // 1回目
        jobLauncher.run(config.updateBusinessDateJob(), jobParameters1);

        JobParameters jobParameters2 = jobParametersBuilder().toJobParameters();

        // リラン
        jobLauncher.run(config.updateBusinessDateJob(), jobParameters2);
    }
}
