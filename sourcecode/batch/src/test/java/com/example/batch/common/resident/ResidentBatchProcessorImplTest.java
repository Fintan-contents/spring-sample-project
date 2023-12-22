package com.example.batch.common.resident;

import static org.junit.jupiter.api.Assertions.*;

import com.example.batch.test.SystemDateTextReplacer;
import com.github.database.rider.core.api.configuration.DBUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;

import com.example.batch.common.configuration.BatchBaseConfig;
import com.example.batch.common.resident.configuration.ResidentBatchProperties;
import com.example.batch.common.resident.mapper.ResidentBatchMapper;
import com.example.batch.test.BatchTest;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

class ResidentBatchProcessorImplTest {

    private static final String BASE_PATH = "com/example/batch/common/resident/ResidentBatchProcessorImplTest";

    /**
     * 要求テーブルのプロパティが取得できないためバッチ終了。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTestMistakeRequestProperties")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class MistakeRequestPropertiesTest {

        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-mistake.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-mistake.yaml")
        void test() {
            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> sut.initialize());
            assertEquals("ジョブIDに紐づく要求テーブルの設定を取得できませんでした", exception.getMessage());
        }

        @TestConfiguration
        static class TestConfig extends SimpleTestConfig {
        }
    }

    /**
     * 要求テーブルのテーブル名誤りのためバッチ終了。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTestMistakeTableName")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class MistakeTableNameTest {

        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-mistake.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-mistake.yaml")
        void test() {
            assertThrows(DataAccessException.class, () -> sut.initialize());
        }

        @TestConfiguration
        static class TestConfig extends SimpleTestConfig {
        }
    }

    /**
     * 要求テーブルの主キー名誤りのためバッチ終了。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTestMistakePrimaryKeyName")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class MistakePrimaryKeyNameTest {

        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-mistake.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-mistake.yaml")
        void test() {
            assertThrows(DataAccessException.class, () -> sut.initialize());
        }

        @TestConfiguration
        static class TestConfig extends SimpleTestConfig {
        }
    }

    /**
     * 要求テーブルのステータスカラム名誤りのためバッチ終了。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTestMistakeStatusColumnName")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class MistakeStatusColumnNameTest {

        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-mistake.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-mistake.yaml")
        void test() {
            assertThrows(DataAccessException.class, () -> sut.initialize());
        }

        @TestConfiguration
        static class TestConfig extends SimpleTestConfig {
        }
    }

    /**
     * Spring Batchのジョブ名誤りのためバッチ終了。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTestMistakeSpringBatchJobName")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class MistakeSpringBatchJobNameTest {

        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-mistake.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-mistake.yaml")
        void test() {
            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> sut.initialize());
            assertEquals("Jobを特定できませんでした", exception.getMessage());
        }

        @TestConfiguration
        static class TestConfig extends SimpleTestConfig {
        }
    }

    /**
     * 処理成功。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class SuccessTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-success.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-success.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(1, config.tasklet().getCounter());
        }

        @TestConfiguration
        static class TestConfig extends SimpleTestConfig {
        }
    }

    /**
     * リトライ1回で成功。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class SuccessWith1RetryTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-success.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-success.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(2, config.tasklet().getCounter());
        }

        @TestConfiguration
        static class TestConfig extends AbstractTestConfig<TestTasklet> {
            @Override
            @Bean
            public TestTasklet tasklet() {
                return new TestTasklet(1);
            }
        }
    }

    /**
     * リトライ2回で成功。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class SuccessWith2RetryTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-success.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-success.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(3, config.tasklet().getCounter());
        }

        @TestConfiguration
        static class TestConfig extends AbstractTestConfig<TestTasklet> {
            @Override
            @Bean
            public TestTasklet tasklet() {
                return new TestTasklet(2);
            }
        }
    }

    /**
     * リトライ3回で成功。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class SuccessWith3RetryTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-success.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-success.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(4, config.tasklet().getCounter());
        }

        @TestConfiguration
        static class TestConfig extends AbstractTestConfig<TestTasklet> {
            @Override
            @Bean
            public TestTasklet tasklet() {
                return new TestTasklet(3);
            }
        }
    }

    /**
     * リトライ4回で成功。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class SuccessWith4RetryTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-success.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-success.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(5, config.tasklet().getCounter());
        }

        @TestConfiguration
        static class TestConfig extends AbstractTestConfig<TestTasklet> {
            @Override
            @Bean
            public TestTasklet tasklet() {
                return new TestTasklet(4);
            }
        }
    }

    /**
     * リトライ4回で失敗。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class FailureWith4RetryTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-failure.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-failure.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(5, config.tasklet().getCounter());
        }

        @TestConfiguration
        static class TestConfig extends AbstractTestConfig<TestTasklet> {
            @Override
            @Bean
            public TestTasklet tasklet() {
                return new TestTasklet(5);
            }
        }
    }

    /**
     * リトライ対象外の例外で失敗。
     *
     */
    @SpringBootTest(properties = {
            "resident-batch.retry.not-retry-on=com.example.batch.common.resident.ResidentBatchProcessorImplTest.TestException"
    })
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class FailureWithNotRetryOnExceptionTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-failure.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-failure.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(1, config.tasklet().getCounter());
        }

        @TestConfiguration
        static class TestConfig extends AbstractTestConfig<TestTasklet> {
            @Override
            @Bean
            public TestTasklet tasklet() {
                return new TestTasklet(4);
            }
        }
    }

    /**
     * 要求テーブル1件を処理。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class RequestData1RecordTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-1record.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-1record.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(List.of(1L), config.tasklet().getPrimaryKeys());
        }

        @TestConfiguration
        static class TestConfig extends SimpleTestConfig {
        }
    }

    /**
     * 要求テーブル2件を処理。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class RequestData2RecordsTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-2records.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-2records.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(List.of(1L, 2L), config.tasklet().getPrimaryKeys());
        }

        @TestConfiguration
        static class TestConfig extends SimpleTestConfig {
        }
    }

    /**
     * 要求テーブルn + 1件(1件は持ち越し)。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class RequestDataNPlus1RecordsTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-n-plus-1records.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-n-plus-1records.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(List.of(1L, 2L, 3L, 4L, 5L), config.tasklet().getPrimaryKeys());
        }

        @TestConfiguration
        static class TestConfig extends SimpleTestConfig {
        }
    }

    /**
     * ステータスPROCESSING、SUCCESS、FAILUREは処理対象外。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class UnprocessedOnlyTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-unprocessed-only.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-unprocessed-only.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(List.of(1L), config.tasklet().getPrimaryKeys());
        }

        @TestConfiguration
        static class TestConfig extends SimpleTestConfig {
        }
    }

    /**
     * 常駐バッチ状態管理テーブルの起動フラグがfalseになったら停止する。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class StopTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;
        @Autowired
        ResidentBatchProperties residentBatchProperties;
        @Autowired
        ResidentBatchMapper residentBatchMapper;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-stop.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-stop.yaml")
        void test() {
            sut.initialize();

            assertTrue(sut.process());

            // 起動フラグをfalseにする
            residentBatchMapper.updateResidentBatchStateRunningByPrimaryKey(false, residentBatchProperties.getJobId());

            assertFalse(sut.process());
            assertEquals(List.of(1L, 2L, 3L, 4L, 5L), config.tasklet().getPrimaryKeys());
        }

        @TestConfiguration
        static class TestConfig extends SimpleTestConfig {
        }
    }

    /**
     * ソート（要求日時 asc、主キー asc）。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTest")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class SortTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-sort.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-sort.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(List.of(4L, 3L, 1L, 2L), config.tasklet().getPrimaryKeys());
        }

        @TestConfiguration
        static class TestConfig extends SimpleTestConfig {
        }
    }

    /**
     * 文字列型の主キーのテスト。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTestStringPrimaryKey")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class StringPrimaryKeyTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-string-primary-key.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-string-primary-key.yaml", orderBy = "string_requested_at")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals(List.of("foo", "bar", "baz"), config.tasklet().getPrimaryKeys());
        }

        @TestConfiguration
        static class TestConfig extends AbstractTestConfig<StringPrimaryKeyTasklet> {

            @Bean
            @Override
            public StringPrimaryKeyTasklet tasklet() {
                return new StringPrimaryKeyTasklet();
            }
        }
    }

    /**
     * ステータスの値をカスタマイズする。
     *
     */
    @SpringBootTest
    @ActiveProfiles(profiles = "ResidentBatchProcessorImplTestStatus")
    @BatchTest
    @DBRider
    @DBUnit(replacers = {SystemDateTextReplacer.class}, cacheConnection = false, caseSensitiveTableNames = true)
    @Nested
    class StatusTest {

        @Autowired
        TestConfig config;
        @Autowired
        ResidentBatchProcessorImpl sut;

        @Test
        @DataSet(executeScriptsBefore = BASE_PATH + "/request_table.sql",
                value = BASE_PATH + "/dataset-status.yaml")
        @ExpectedDataSet(value = BASE_PATH + "/expected-status.yaml")
        void test() {
            sut.initialize();
            assertTrue(sut.process());
            assertEquals("BB", config.tasklet().getStatus());
        }

        @TestConfiguration
        static class TestConfig extends AbstractTestConfig<StatusTestTasklet> {

            @Bean
            @Override
            public StatusTestTasklet tasklet() {
                return new StatusTestTasklet();
            }
        }
    }

    static class TestException extends Exception {
    }

    static class TestTasklet implements Tasklet {

        private List<Long> primaryKeys = new ArrayList<>();

        private int counter;
        private final int timesToFail;

        public TestTasklet(int timesToFail) {
            this.timesToFail = timesToFail;
        }

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                throws Exception {

            if (counter++ < timesToFail) {
                throw new TestException();
            }

            Long primaryKey = contribution
                    .getStepExecution().getJobParameters()
                    .getLong("request.id");
            primaryKeys.add(primaryKey);

            return RepeatStatus.FINISHED;
        }

        public int getCounter() {
            return counter;
        }

        public List<Long> getPrimaryKeys() {
            return primaryKeys;
        }
    }

    static class StringPrimaryKeyTasklet implements Tasklet {

        private List<String> primaryKeys = new ArrayList<>();

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                throws Exception {

            String primaryKey = contribution
                    .getStepExecution().getJobParameters()
                    .getString("request.id");
            primaryKeys.add(primaryKey);

            return RepeatStatus.FINISHED;
        }

        public List<String> getPrimaryKeys() {
            return primaryKeys;
        }
    }

    static class StatusTestTasklet implements Tasklet {

        @Autowired
        private ResidentBatchProcessorImplTestMapper mapper;
        private String status;

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            long requestId = contribution.getStepExecution().getJobParameters().getLong("request.id");
            if (requestId == 2L) {
                throw new TestException();
            }

            status = mapper.selectTestRequestsStatusByPrimaryKey(requestId);

            return RepeatStatus.FINISHED;
        }

        public String getStatus() {
            return status;
        }
    }

    static class SimpleTestConfig extends AbstractTestConfig<TestTasklet> {
        @Override
        @Bean
        public TestTasklet tasklet() {
            return new TestTasklet(0);
        }
    }

    static abstract class AbstractTestConfig<T extends Tasklet> extends BatchBaseConfig {

        public abstract T tasklet();

        @Bean
        public Step step() {
            return new StepBuilder("TEST", jobRepository)
                    .tasklet(tasklet(), platformTransactionManager)
                    .build();
        }

        @Bean
        public Job job(Step step) {
            return new JobBuilder("TEST", jobRepository)
                    .start(step)
                    .build();
        }
    }

}
