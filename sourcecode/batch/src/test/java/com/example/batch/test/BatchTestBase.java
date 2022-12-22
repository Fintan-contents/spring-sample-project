package com.example.batch.test;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.Appender;
import com.example.batch.common.configuration.BatchExitCodeGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * バッチのテストクラスの基底クラス。
 *
 */
public abstract class BatchTestBase {

    /**
     * ジョブを起動するためのクラス
     */
    @Autowired
    protected JobLauncher jobLauncher;
    @Autowired
    private BatchExitCodeGenerator batchExitCodeGenerator;

    @Captor
    protected ArgumentCaptor<ILoggingEvent> logCaptor;
    private Logger root;
    private Appender<ILoggingEvent> mockAppender;
    private boolean logCaptured = false;

    @BeforeEach
    void setUpMockLogger() throws Exception {
        root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        mockAppender = mock(Appender.class);
        root.addAppender(mockAppender);
    }

    @AfterEach
    void tearDownMockLogger() {
        root.detachAppender(mockAppender);
    }

    /**
     * クラスパス上のファイルを読み込んで文字列で返す。
     * 主に出力ファイルの期待値となるCSVファイルを読み込むために使用されることを想定している。
     * 
     * @param path ファイルのパス。テストクラスの完全修飾名に対応するディレクトリからの相対パスとなる
     * @return ファイルの内容。UTF-8でデコードされる
     */
    protected String read(String path) {
        Resource resource = buildResource(path);
        try (InputStream in = resource.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * クラスパス上のファイルを、指定した場所にコピーする。
     * 主に入力ファイルのCSVファイルをバッチ処理が取り込む場所に配置するために仕様されることを想定している。
     * <p>
     * コピー先のディレクトリがない場合はディレクトリが作成される。
     * コピー先にファイルが存在する場合は、上書きされる。
     *
     * @param from ファイルのパス。テストクラスの完全修飾名に対応するディレクトリからの相対パスとなる
     * @param to コピー先のファイルのパス。テスト実行時のワーキングディレクトリからの相対パスとなる
     */
    protected void copy(String from, String to) {
        try {
            Path toFile = Path.of(to);
            Path toDir = toFile.getParent();
            if (Files.notExists(toDir) || !Files.isDirectory(toDir)) {
                Files.createDirectories(toDir);
            }

            Resource resource = buildResource(from);
            Files.copy(resource.getFile().toPath(), toFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Resource buildResource(String path) {
        String prefix = getClass().getName().replace('.', '/');
        return new ClassPathResource(prefix + "/" + path);
    }

    /**
     * {@link JobParameters}が同じだと二回目以降実行できなくなるので、
     * 同じ{@link JobParameters}にならないようにランダムな値をあらかじめ設定した
     * {@link JobParametersBuilder}を生成する。
     *
     * @return ランダムな値が設定された {@link JobParametersBuilder}
     */
    protected JobParametersBuilder jobParametersBuilder() {
        return new JobParametersBuilder()
                // 毎回異なる値がJobParametersに設定されるようにするため、
                // UUIDをセットしている。
                .addString("test_id", UUID.randomUUID().toString());
    }

    /**
     * 指定した文字列がログに出力されているか検証する。
     * @param message ログに出力されていることを期待する部分文字列
     */
    protected void assertLogContains(String message) {
        captureLog();

        assertThat(logCaptor.getAllValues())
                .extracting(ILoggingEvent::getFormattedMessage)
                .anyMatch(log -> log.contains(message));
    }

    /**
     * 指定した文字列がログに出力されていないか検証する。
     * @param message ログに出力されていないことを期待する部分文字列
     */
    protected void assertLogNoContains(String message) {
        captureLog();

        assertThat(logCaptor.getAllValues())
                .extracting(ILoggingEvent::getFormattedMessage)
                .allMatch(log -> !log.contains(message));
    }

    /**
     * 指定した文字列が、指定されたログレベルで出力されているか検証する。
     * @param level 期待するログレベル
     * @param message ログに出力されていることを期待する部分文字列
     */
    protected void assertLogContains(Level level, String message) {
        captureLog();

        assertThat(logCaptor.getAllValues())
                .anyMatch(event -> event.getLevel() == level && event.getFormattedMessage().contains(message));
    }

    /**
     * 指定した文字列が、ログに書き出された例外のメッセージに含まれるかを検証する。
     * @param message ログに書き出された例外のメッセージに含まれることを期待する文字列
     */
    protected void assertLogContainsInException(String message) {
        captureLog();

        assertThat(logCaptor.getAllValues())
                .anyMatch(event -> containsMessage(event.getThrowableProxy(), message));
    }

    /**
     * 指定した文字列が、指定されたログレベルでログに書き出された例外のメッセージに含まれるかを検証する。
     * @param level 期待するログレベル
     * @param message ログに書き出された例外のメッセージに含まれることを期待する文字列
     */
    protected void assertLogContainsInException(Level level, String message) {
        captureLog();

        assertThat(logCaptor.getAllValues())
                .anyMatch(event -> event.getLevel() == level && containsMessage(event.getThrowableProxy(), message));
    }

    /**
     * 指定した文字列を含む原因例外が存在するか再帰的に検査する。
     * @param throwableProxy 例外プロキシ
     * @param message 検査する文字列
     * @return 文字列を含む例外が存在した場合はtrue
     */
    private boolean containsMessage(IThrowableProxy throwableProxy, String message) {
        if (throwableProxy == null || throwableProxy.getMessage() == null) {
            return false;
        }
        if (throwableProxy.getMessage().contains(message)) {
            return true;
        }
        return containsMessage(throwableProxy.getCause(), message);
    }

    /**
     * 指定されたレベルのログが出力されていないことを検証する。
     * @param level 出力されていないことを期待するログレベル
     */
    protected void assertNotExistsLogLevel(Level level) {
        captureLog();

        assertThat(logCaptor.getAllValues())
                .extracting(ILoggingEvent::getLevel)
                .doesNotContainSequence(level);
    }

    /**
     * モックのロガーに出力されたログをキャプチャーする。
     * <p>
     * 既にキャプチャー済みの場合は処理を行わない。
     * </p>
     */
    protected void captureLog() {
        if (!logCaptured) {
            verify(mockAppender, atLeastOnce()).doAppend(logCaptor.capture());
            logCaptured = true;
        }
    }

    protected int getExitCode(JobExecution jobExecution) {
        batchExitCodeGenerator.onApplicationEvent(new JobExecutionEvent(jobExecution));
        int exitCode = batchExitCodeGenerator.getExitCode();
        batchExitCodeGenerator.clearJobExecutions();
        return exitCode;
    }
}