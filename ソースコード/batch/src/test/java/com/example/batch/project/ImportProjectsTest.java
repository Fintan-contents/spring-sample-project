package com.example.batch.project;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.batch.project.configuration.ImportProjectsConfig;
import com.example.batch.test.BatchTest;
import com.example.batch.test.BatchTestBase;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;

import ch.qos.logback.classic.Level;

/**
 * プロジェクト一括登録バッチ/本テーブル登録のテスト。
 * 
 * @author sample
 *
 */
@BatchTest
@SpringBootTest
@DBRider
@ActiveProfiles("ImportProjectsTest")
public class ImportProjectsTest extends BatchTestBase {
    private static final String BASE_PATH = "com/example/batch/project/ImportProjectsTest";

    @Autowired
    ImportProjectsConfig config;

    JobParameters jobParameters = jobParametersBuilder().toJobParameters();

    /**
     * 登録の確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testNormalInsert/testNormalInsert.xls",
            executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project.sql")
    @ExpectedDataSet(BASE_PATH + "/testNormalInsert/expected-testNormalInsert.xls")
    void testNormalInsert() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 更新の確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testNormalUpdate/testNormalUpdate.xls")
    @ExpectedDataSet(BASE_PATH + "/testNormalUpdate/expected-testNormalUpdate.xls")
    void testNormalUpdate() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 指定されたプロジェクトが存在しない場合、業務エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testProjectNotFound/testProjectNotFound.xls")
    @ExpectedDataSet(BASE_PATH + "/testProjectNotFound/expected-testProjectNotFound.xls")
    void testProjectNotFound() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);

        assertLogContains("プロジェクトが存在しません(project_work_id=1, project_id=2)");
    }

    /**
     * 指定された部門が存在しない場合、業務エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testOrganizationNotFound/testOrganizationNotFound.xls")
    void testOrganizationNotFound() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);

        assertLogContains("部門が存在しません(project_work_id=1, organization_id=999)");
    }

    /**
     * 事業部が指定された場合、業務エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testOrganizationIdIsDivision/testOrganizationIdIsDivision.xls")
    void testOrganizationIdIsDivision() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);

        assertLogContains("事業部のIDは設定できません(project_work_id=2, organization_id=1)");
    }

    /**
     * 最大文字数の登録を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testMaxLengthInsert/testMaxLengthInsert.xls",
            executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project.sql")
    @ExpectedDataSet(BASE_PATH + "/testMaxLengthInsert/expected-testMaxLengthInsert.xls")
    void testMaxLengthInsert() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 最大文字数の更新を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testMaxLengthUpdate/testMaxLengthUpdate.xls")
    @ExpectedDataSet(BASE_PATH + "/testMaxLengthUpdate/expected-testMaxLengthUpdate.xls")
    void testMaxLengthUpdate() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * ソート順を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testSort/testSort.xls",
            executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project.sql")
    @ExpectedDataSet(value = BASE_PATH + "/testSort/expected-testSort.xls")
    void testSort() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 複数レコードの登録を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testMultiInsert/testMultiInsert.xls",
            executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project.sql")
    @ExpectedDataSet(value = BASE_PATH + "/testMultiInsert/expected-testMultiInsert.xls")
    void testMultiInsert() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 精査エラーを伴う複数レコードの登録を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testMultiInsertWithValidationError/testMultiInsertWithValidationError.xls",
            executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project.sql")
    @ExpectedDataSet(value = BASE_PATH + "/testMultiInsertWithValidationError/expected-testMultiInsertWithValidationError.xls")
    void testMultiInsertWithValidationError() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 複数レコードの更新を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testMultiUpdate/testMultiUpdate.xls")
    @ExpectedDataSet(value = BASE_PATH + "/testMultiUpdate/expected-testMultiUpdate.xls")
    void testMultiUpdate() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 精査エラーを伴う複数レコードの更新を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testMultiUpdateWithValidationError/testMultiUpdateWithValidationError.xls")
    @ExpectedDataSet(value = BASE_PATH + "/testMultiUpdateWithValidationError/expected-testMultiUpdateWithValidationError.xls")
    void testMultiUpdateWithValidationError() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 正常に処理された場合のログの確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testLog/testLog.xls")
    void testLog() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);

        assertLogContains(Level.INFO, "入力件数=10, スキップ件数=0");
    }

    /**
     * 精査エラー時のログの確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testLogInValidationError/testLogInValidationError.xls")
    void testLogInValidationError() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);

        assertLogContains(Level.INFO, "入力件数=10, スキップ件数=1");
        assertLogContains(Level.WARN, "スキップしました エラー内容=部門が存在しません(project_work_id=2, organization_id=99)");
    }

    /**
     * ワークテーブルが空の場合の確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testEmptyWorkTable/testEmptyWorkTable.xls")
    @ExpectedDataSet(value = BASE_PATH + "/testEmptyWorkTable/expected-testEmptyWorkTable.xls")
    void testEmptyWorkTable() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 正常終了時の終了コードを確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testExitCodeNormal/testExitCodeNormal.xls")
    void testExitCodeNormal() throws Exception {
        int exitCode = getExitCode(jobLauncher.run(config.importProjectsJob(), jobParameters));
        assertThat(exitCode).isEqualTo(0);
    }

    /**
     * 警告終了時の終了コードを確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testExitCodeWarning/testExitCodeWarning.xls")
    void testExitCodeWarning() throws Exception {
        int exitCode = getExitCode(jobLauncher.run(config.importProjectsJob(), jobParameters));
        assertThat(exitCode).isEqualTo(2);
    }
}
