package com.example.batch.project;

import com.example.batch.project.configuration.CreateUsersProjectsConfig;
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

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ユーザ別従事プロジェクト抽出バッチのテスト。
 *
 * @author sample
 *
 */
@BatchTest
@SpringBootTest
@DBRider
@DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
public class CreateUsersProjectsTest extends BatchTestBase {
    private static final String BASE_PATH = "com/example/batch/project/CreateUsersProjectsTest";
    private static final String OUTPUT_FILE_PREFIX = "work/BA1060301/output/N21AA003_";

    @Autowired
    CreateUsersProjectsConfig config;

    /**
     * 指定された要求IDに紐づく担当プロジェクトが出力されていることのテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testSearchCondition/testSearchCondition.xlsx")
    void testSearchCondition() throws Exception {
        JobParameters jobParameters = jobParametersBuilder()
                .addLong("request.id", 1L)
                .toJobParameters();

        jobLauncher.run(config.createUsersProjectsJob(), jobParameters);

        assertThat(Path.of(OUTPUT_FILE_PREFIX + "1.csv"))
                .usingCharset(StandardCharsets.UTF_8)
                .hasContent(read("testSearchCondition/expected.csv"));
    }

    /**
     * 指定された要求IDに紐づく担当プロジェクトが存在しない場合、空ファイルが出力されることをテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testNoTargetProjects/testNoTargetProjects.xlsx")
    void testNoTargetProjects() throws Exception {
        JobParameters jobParameters = jobParametersBuilder()
                .addLong("request.id", 2L)
                .toJobParameters();

        jobLauncher.run(config.createUsersProjectsJob(), jobParameters);

        assertThat(Path.of(OUTPUT_FILE_PREFIX + "2.csv"))
                .usingCharset(StandardCharsets.UTF_8)
                .isEmptyFile();
    }

    /**
     * ソート順序のテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testSort/testSort.xlsx")
    void testSort() throws Exception {
        JobParameters jobParameters = jobParametersBuilder()
                .addLong("request.id", 1L)
                .toJobParameters();

        jobLauncher.run(config.createUsersProjectsJob(), jobParameters);

        assertThat(Path.of(OUTPUT_FILE_PREFIX + "1.csv"))
                .usingCharset(StandardCharsets.UTF_8)
                .hasContent(read("testSort/expected.csv"));
    }

    /**
     * 最大長の出力テスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testMaxLength/testMaxLength.xlsx")
    void testMaxLength() throws Exception {
        JobParameters jobParameters = jobParametersBuilder()
                .addLong("request.id", 1L)
                .toJobParameters();

        jobLauncher.run(config.createUsersProjectsJob(), jobParameters);

        assertThat(Path.of(OUTPUT_FILE_PREFIX + "1.csv"))
                .usingCharset(StandardCharsets.UTF_8)
                .hasContent(read("testMaxLength/expected.csv"));
    }

    /**
     * 最小長の出力テスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testMinLength/testMinLength.xlsx")
    void testMinLength() throws Exception {
        JobParameters jobParameters = jobParametersBuilder()
                .addLong("request.id", 1L)
                .toJobParameters();

        jobLauncher.run(config.createUsersProjectsJob(), jobParameters);

        assertThat(Path.of(OUTPUT_FILE_PREFIX + "1.csv"))
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
        JobParameters jobParameters = jobParametersBuilder()
                .addLong("request.id", 1L)
                .toJobParameters();

        jobLauncher.run(config.createUsersProjectsJob(), jobParameters);

        assertThat(Path.of(OUTPUT_FILE_PREFIX + "1.csv"))
                .usingCharset(StandardCharsets.UTF_8)
                .hasContent(read("testOutputNull/expected.csv"));
    }

    /**
     * ユーザ別従事プロジェクトの更新内容を確認するテスト。
     *
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testUpdateProjectsByUser/testUpdateProjectsByUser.xlsx")
    @ExpectedDataSet(BASE_PATH + "/testUpdateProjectsByUser/expected-testUpdateProjectsByUser.xlsx")
    void testUpdateProjectsByUser() throws Exception {
        JobParameters jobParameters = jobParametersBuilder()
                .addLong("request.id", 1L)
                .toJobParameters();

        jobLauncher.run(config.createUsersProjectsJob(), jobParameters);
    }
}
