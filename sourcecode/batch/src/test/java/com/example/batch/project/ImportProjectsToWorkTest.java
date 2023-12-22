package com.example.batch.project;

import static org.assertj.core.api.Assertions.*;

import com.example.batch.test.SystemDateTextReplacer;
import com.github.database.rider.core.api.configuration.DBUnit;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.batch.project.configuration.ImportProjectsToWorkConfig;
import com.example.batch.test.BatchTest;
import com.example.batch.test.BatchTestBase;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * プロジェクト一括登録バッチ/ワークテーブル登録のテスト。
 * 
 * @author sample
 *
 */
@BatchTest
@SpringBootTest
@DBRider
@DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
public class ImportProjectsToWorkTest extends BatchTestBase {
    private static final String BASE_PATH = "com/example/batch/project/ImportProjectsToWorkTest";
    private static final String INPUT_FILE = "work/BA1060201/input/N21AA001.csv";

    @Autowired
    ImportProjectsToWorkConfig config;

    JobParameters jobParameters = jobParametersBuilder().toJobParameters();

    @BeforeEach
    void setUp() throws Exception {
        Files.deleteIfExists(Path.of(INPUT_FILE));
    }

    /**
     * 必須項目が無い場合、精査エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @ExpectedDataSet(BASE_PATH + "/testRequired/expected-testRequired.xlsx")
    void testRequired() throws Exception {
        copy("testRequired/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains("プロジェクト名を入力してください。");
        assertLogContains("プロジェクト種別を入力してください。");
        assertLogContains("プロジェクト分類を入力してください。");
        assertLogContains("プロジェクト開始日付を入力してください。");
        assertLogContains("プロジェクト終了日付を入力してください。");
        assertLogContains("組織IDを入力してください。");
        assertLogContains("顧客IDを入力してください。");
        assertLogContains("プロジェクトマネージャーを入力してください。");
        assertLogContains("プロジェクトリーダーを入力してください。");
    }

    /**
     * 必須ではない項目が無い場合、正常に処理される。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project_work.sql")
    @ExpectedDataSet(BASE_PATH + "/testOptional/expected-testOptional.xlsx")
    void testOptional() throws Exception {
        copy("testOptional/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);
    }

    /**
     * 文字数が上限と同じ場合、正常に処理される。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project_work.sql")
    @ExpectedDataSet(BASE_PATH + "/testMaxLength/expected-testMaxLength.xlsx")
    void testMaxLength() throws Exception {
        copy("testMaxLength/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);
    }

    /**
     * 文字数が上限を超えている場合、精査エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @ExpectedDataSet(BASE_PATH + "/testMaxLengthError/expected-testMaxLengthError.xlsx")
    void testMaxLengthError() throws Exception {
        copy("testMaxLengthError/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains("プロジェクト名は128文字以下の全角文字で入力してください。");
        assertLogContains("プロジェクトマネージャーは128文字以下の全角文字で入力してください。");
        assertLogContains("プロジェクトリーダーは128文字以下の全角文字で入力してください。");
        assertLogContains("備考は512文字以下のシステム許容文字で入力してください。");
    }

    /**
     * 値が最大値の場合、正常に処理される。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project_work.sql")
    @ExpectedDataSet(BASE_PATH + "/testMaxValue/expected-testMaxValue.xlsx")
    void testMaxValue() throws Exception {
        copy("testMaxValue/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);
    }

    /**
     * 値が最大値を超えている場合、精査エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @ExpectedDataSet(BASE_PATH + "/testMaxValueError/expected-testMaxValueError.xlsx")
    void testMaxValueError() throws Exception {
        copy("testMaxValueError/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains("プロジェクトIDは数値を入力してください。");
        assertLogContains("組織IDは数値を入力してください。");
        assertLogContains("顧客IDは数値を入力してください。");
        assertLogContains("売上高は0円から999999999円の範囲で入力してください。");
    }

    /**
     * 値が最小値の場合、正常に処理される。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project_work.sql")
    @ExpectedDataSet(BASE_PATH + "/testMinValue/expected-testMinValue.xlsx")
    void testMinValue() throws Exception {
        copy("testMinValue/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);
    }

    /**
     * 値が最小値を下回っている場合、精査エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testMinValueError() throws Exception {
        copy("testMinValueError/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains("売上高は0円から999999999円の範囲で入力してください。");
    }

    /**
     * 数値が不正な場合、精査エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testInvalidCharacterForNumeric() throws Exception {
        copy("testInvalidCharacterForNumeric/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains("プロジェクトIDは数値を入力してください。");
        assertLogContains("組織IDは数値を入力してください。");
        assertLogContains("顧客IDは数値を入力してください。");
        assertLogContains("売上高は数値を入力してください。");
    }

    /**
     * 文字が不正な場合、精査エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testInvalidCharacterForText() throws Exception {
        copy("testInvalidCharacterForText/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains("プロジェクト名は128文字以下の全角文字で入力してください。");
        assertLogContains("プロジェクトマネージャーは128文字以下の全角文字で入力してください。");
        assertLogContains("プロジェクトリーダーは128文字以下の全角文字で入力してください。");
        assertLogContains("備考は512文字以下のシステム許容文字で入力してください。");
    }

    /**
     * 正常に処理される。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(
            value = BASE_PATH + "/testNormal/testNormal.xlsx",
            executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project_work.sql")
    @ExpectedDataSet(BASE_PATH + "/testNormal/expected-testNormal.xlsx")
    void testNormal() throws Exception {
        copy("testNormal/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);
    }

    /**
     * コード値が不正な場合、精査エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testCodeError() throws Exception {
        copy("testCodeError/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains("プロジェクト種別に不正な値が指定されました。");
        assertLogContains("プロジェクト分類に不正な値が指定されました。");
    }

    /**
     * 日付フォーマットが不正な場合、精査エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testInvalidDateFormat() throws Exception {
        copy("testInvalidDateFormat/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains("プロジェクト開始日付は日付の形式が正しくありません。");
        assertLogContains("プロジェクト終了日付は日付の形式が正しくありません。");
    }

    /**
     * 存在しない日付の場合、精査エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testNotExistsDate() throws Exception {
        copy("testNotExistsDate/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains("プロジェクト開始日付は日付の形式が正しくありません。");
        assertLogContains("プロジェクト終了日付は日付の形式が正しくありません。");
    }

    /**
     * 開始日が終了日よりも未来の場合、項目間精査エラー。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testInvalidProjectPeriod() throws Exception {
        copy("testInvalidProjectPeriod/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains("開始日付は終了日付より前の日付を入力してください。");
    }

    /**
     * 開始日と終了日が同じ日付の場合、項目間精査エラーにならない。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testValidProjectPeriodSameDate() throws Exception {
        copy("testValidProjectPeriodSameDate/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogNoContains("開始日付は終了日付より前の日付を入力してください。");
    }

    /**
     * 開始日がnullの場合、項目間精査エラーにならない。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testValidProjectPeriodStartDateIsNull() throws Exception {
        copy("testValidProjectPeriodStartDateIsNull/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogNoContains("開始日付は終了日付より前の日付を入力してください。");
    }

    /**
     * 終了日がnullの場合、項目間精査エラーにならない。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testValidProjectPeriodEndDateIsNull() throws Exception {
        copy("testValidProjectPeriodEndDateIsNull/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogNoContains("開始日付は終了日付より前の日付を入力してください。");
    }

    /**
     * 複数レコードを処理する。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project_work.sql")
    @ExpectedDataSet(BASE_PATH + "/testMultiRecord/expected-testMultiRecord.xlsx")
    void testMultiRecord() throws Exception {
        copy("testMultiRecord/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);
    }

    /**
     * スキップするレコードを含む場合。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project_work.sql")
    @ExpectedDataSet(BASE_PATH + "/testSkip/expected-testSkip.xlsx")
    void testSkip() throws Exception {
        copy("testSkip/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);
    }

    /**
     * 空のファイルでも正常に処理される。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @ExpectedDataSet(BASE_PATH + "/testEmptyFile/expected-testEmptyFile.xlsx")
    void testEmptyFile() throws Exception {
        copy("testEmptyFile/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertThat(logCaptor.getAllValues())
                .extracting(ILoggingEvent::getLevel)
                .doesNotContainSequence(Level.ERROR);
    }

    /**
     * 不正なCSVは登録されない。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @ExpectedDataSet(BASE_PATH + "/testInvalidCsv/expected-testInvalidCsv.xlsx")
    void testInvalidCsv() throws Exception {
        copy("testInvalidCsv/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);
    }

    /**
     * 正常に処理された場合のログの確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testLog() throws Exception {
        copy("testLog/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains(Level.INFO, "入力件数=10, スキップ件数=0");
    }

    /**
     * 精査エラー時のログの確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testLogInValidationError() throws Exception {
        copy("testLogInValidationError/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains(Level.INFO, "入力件数=10, スキップ件数=1");
        assertLogContains(Level.WARN, "スキップしました 行番号=2, エラー内容=プロジェクト名は128文字以下の全角文字で入力してください。");
    }

    /**
     * リラン。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    @DataSet(executeScriptsBefore = BASE_PATH + "/reset_sequence_val_project_work.sql")
    @ExpectedDataSet(BASE_PATH + "/testRerun/expected-testRerun.xlsx")
    void testRerun() throws Exception {
        copy("testRerun/input.csv", INPUT_FILE);

        // 1回目
        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        // rerun
        JobParameters jobParametersForRerun = new JobParametersBuilder()
                .addString("test_id", UUID.randomUUID().toString())
                .toJobParameters();
        jobLauncher.run(config.importProjectsToWorkJob(), jobParametersForRerun);
    }

    /**
     * 1レコードに対して複数の精査エラーが発生した場合。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testCollectAllValidationErrors() throws Exception {
        copy("testCollectAllValidationErrors/input.csv", INPUT_FILE);

        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertLogContains("プロジェクトIDは数値を入力してください。");
        assertLogContains("プロジェクト名は128文字以下の全角文字で入力してください。");
    }

    /**
     * ファイルが存在しない場合でもエラーにならない。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testFileNotExists() throws Exception {
        jobLauncher.run(config.importProjectsToWorkJob(), jobParameters);

        assertNotExistsLogLevel(Level.ERROR);
    }

    /**
     * 正常終了時の終了コードを確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testExitCodeNormal() throws Exception {
        copy("testExitCodeNormal/input.csv", INPUT_FILE);

        int exitCode = getExitCode(jobLauncher.run(config.importProjectsToWorkJob(), jobParameters));
        assertThat(exitCode).isEqualTo(0);
    }

    /**
     * 警告終了時の終了コードを確認。
     * 
     * @throws Exception Spring Batchでエラーが発生した場合にスローされる
     */
    @Test
    void testExitCodeWarning() throws Exception {
        copy("testExitCodeWarning/input.csv", INPUT_FILE);

        int exitCode = getExitCode(jobLauncher.run(config.importProjectsToWorkJob(), jobParameters));
        assertThat(exitCode).isEqualTo(2);
    }
}
