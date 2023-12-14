package com.example.batch.project;

import static org.assertj.core.api.Assertions.*;

import com.example.batch.test.SystemDateTextReplacer;
import com.github.database.rider.core.api.configuration.DBUnit;
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
import com.github.database.rider.junit5.api.DBRider;

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
@DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
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
            value = BASE_PATH + "/testNormalInsert/testNormalInsert.xlsx",
            executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project.sql")
    @ExpectedDataSet(BASE_PATH + "/testNormalInsert/expected-testNormalInsert.xlsx")
    void testNormalInsert() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 更新の確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testNormalUpdate/testNormalUpdate.xlsx")
    @ExpectedDataSet(BASE_PATH + "/testNormalUpdate/expected-testNormalUpdate.xlsx")
    void testNormalUpdate() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 指定されたプロジェクトが存在しない場合、業務エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testProjectNotFound/testProjectNotFound.xlsx")
    @ExpectedDataSet(BASE_PATH + "/testProjectNotFound/expected-testProjectNotFound.xlsx")
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
    @DataSet(BASE_PATH + "/testOrganizationNotFound/testOrganizationNotFound.xlsx")
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
    @DataSet(BASE_PATH + "/testOrganizationIdIsDivision/testOrganizationIdIsDivision.xlsx")
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
            value = BASE_PATH + "/testMaxLengthInsert/testMaxLengthInsert.xlsx",
            executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project.sql")
    @ExpectedDataSet(BASE_PATH + "/testMaxLengthInsert/expected-testMaxLengthInsert.xlsx")
    void testMaxLengthInsert() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 最大文字数の更新を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testMaxLengthUpdate/testMaxLengthUpdate.xlsx")
    @ExpectedDataSet(BASE_PATH + "/testMaxLengthUpdate/expected-testMaxLengthUpdate.xlsx")
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
            value = BASE_PATH + "/testSort/testSort.xlsx",
            executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project.sql")
    @ExpectedDataSet(value = BASE_PATH + "/testSort/expected-testSort.xlsx")
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
            value = BASE_PATH + "/testMultiInsert/testMultiInsert.xlsx",
            executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project.sql")
    @ExpectedDataSet(value = BASE_PATH + "/testMultiInsert/expected-testMultiInsert.xlsx")
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
            value = BASE_PATH + "/testMultiInsertWithValidationError/testMultiInsertWithValidationError.xlsx",
            executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project.sql")
    @ExpectedDataSet(value = BASE_PATH + "/testMultiInsertWithValidationError/expected-testMultiInsertWithValidationError.xlsx")
    void testMultiInsertWithValidationError() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 複数レコードの更新を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testMultiUpdate/testMultiUpdate.xlsx")
    @ExpectedDataSet(value = BASE_PATH + "/testMultiUpdate/expected-testMultiUpdate.xlsx")
    void testMultiUpdate() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 精査エラーを伴う複数レコードの更新を確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testMultiUpdateWithValidationError/testMultiUpdateWithValidationError.xlsx")
    @ExpectedDataSet(value = BASE_PATH + "/testMultiUpdateWithValidationError/expected-testMultiUpdateWithValidationError.xlsx")
    void testMultiUpdateWithValidationError() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 正常に処理された場合のログの確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testLog/testLog.xlsx")
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
    @DataSet(BASE_PATH + "/testLogInValidationError/testLogInValidationError.xlsx")
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
    @DataSet(BASE_PATH + "/testEmptyWorkTable/testEmptyWorkTable.xlsx")
    @ExpectedDataSet(value = BASE_PATH + "/testEmptyWorkTable/expected-testEmptyWorkTable.xlsx")
    void testEmptyWorkTable() throws Exception {
        jobLauncher.run(config.importProjectsJob(), jobParameters);
    }

    /**
     * 正常終了時の終了コードを確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(BASE_PATH + "/testExitCodeNormal/testExitCodeNormal.xlsx")
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
    @DataSet(BASE_PATH + "/testExitCodeWarning/testExitCodeWarning.xlsx")
    void testExitCodeWarning() throws Exception {
        int exitCode = getExitCode(jobLauncher.run(config.importProjectsJob(), jobParameters));
        assertThat(exitCode).isEqualTo(2);
    }
}
