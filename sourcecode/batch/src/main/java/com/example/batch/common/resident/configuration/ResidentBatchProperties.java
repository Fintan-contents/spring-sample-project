package com.example.batch.common.resident.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 常駐バッチのProperties。
 *
 */
public class ResidentBatchProperties {

    /**
     * 常駐バッチを有効化するフラグ
     */
    private boolean enabled;

    /**
     * ジョブID
     */
    private String jobId;

    /**
     * Spring Batchで扱うJobの名前
     */
    private String springBatchJobName;

    /**
     * 要求テーブルの設定
     */
    private Map<String, RequestProperties> requests = new HashMap<>();

    /**
     * リトライの設定
     */
    private RetryProperties retry = new RetryProperties();

    /**
     * ジョブIDに紐づいた要求テーブルの設定を取得する。
     * 
     * @return ジョブIDに紐づいた要求テーブルの設定
     */
    public RequestProperties getRequest() {
        return requests.get(jobId);
    }

    /**
     * 常駐バッチを有効化するフラグを取得する。
     * 
     * @return 常駐バッチを有効化するフラグ
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 常駐バッチを有効化するフラグを設定する。
     * 
     * @param enabled 常駐バッチを有効化するフラグ
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * ジョブIDを取得する。
     * 
     * @return ジョブID
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * ジョブIDを設定する。
     * 
     * @param jobId ジョブID
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * Spring Batchで扱うJobの名前を取得する。
     * 
     * @return Spring Batchで扱うJobの名前
     */
    public String getSpringBatchJobName() {
        return springBatchJobName;
    }

    /**
     * Spring Batchで扱うJobの名前を設定する。
     * 
     * @param springBatchJobName Spring Batchで扱うJobの名前
     */
    public void setSpringBatchJobName(String springBatchJobName) {
        this.springBatchJobName = springBatchJobName;
    }

    /**
     * 要求テーブルの設定を取得する。
     * 
     * @return 要求テーブルの設定
     */
    public Map<String, RequestProperties> getRequests() {
        return requests;
    }

    /**
     * 要求テーブルの設定を設定する。
     * 
     * @param requests 要求テーブルの設定
     */
    public void setRequests(Map<String, RequestProperties> requests) {
        this.requests = requests;
    }

    /**
     * リトライの設定を取得する。
     * 
     * @return リトライの設定
     */
    public RetryProperties getRetry() {
        return retry;
    }

    /**
     * リトライの設定を設定する。
     * 
     * @param retry リトライの設定
     */
    public void setRetry(RetryProperties retry) {
        this.retry = retry;
    }

    /**
     * 要求テーブルの設定。
     *
     */
    public static class RequestProperties {

        /**
         * テーブル名
         */
        private String tableName;

        /**
         * 主キーの名前
         */
        private String primaryKeyName;

        /**
         * ステータスのカラム名
         */
        private String statusColumnName;

        /**
         * 要求日時のカラム名
         */
        private String requestedAtColumnName;

        /**
         * 一度に取得する最大件数
         */
        private int limit;

        /**
         * 主キーの型が文字列型かどうかを表すフラグ
         */
        private boolean stringPrimaryKey;

        /**
         * ステータス「未処理」を表す値
         */
        private String unprocessedStatus = "01";

        /**
         * ステータス「処理中」を表す値
         */
        private String processingStatus = "02";

        /**
         * ステータス「処理成功」を表す値
         */
        private String successStatus = "03";

        /**
         * ステータス「処理失敗」を表す値
         */
        private String failureStatus = "04";

        /**
         * テーブル名を取得する。
         * 
         * @return テーブル名
         */
        public String getTableName() {
            return tableName;
        }

        /**
         * テーブル名を設定する。
         * 
         * @param tableName テーブル名
         */
        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        /**
         * 主キーの名前を取得する。
         * 
         * @return 主キーの名前
         */
        public String getPrimaryKeyName() {
            return primaryKeyName;
        }

        /**
         * 主キーの名前を設定する。
         * 
         * @param primaryKeyName 主キーの名前
         */
        public void setPrimaryKeyName(String primaryKeyName) {
            this.primaryKeyName = primaryKeyName;
        }

        /**
         * ステータスのカラム名を取得する。
         * 
         * @return ステータスのカラム名
         */
        public String getStatusColumnName() {
            return statusColumnName;
        }

        /**
         * ステータスのカラム名を設定する。
         * 
         * @param statusColumnName ステータスのカラム名
         */
        public void setStatusColumnName(String statusColumnName) {
            this.statusColumnName = statusColumnName;
        }

        /**
         * 要求日時のカラム名を取得する。
         * 
         * @return 要求日時のカラム名
         */
        public String getRequestedAtColumnName() {
            return requestedAtColumnName;
        }

        /**
         * 要求日時のカラム名を設定する。
         * 
         * @param requestedAtColumnName 要求日時のカラム名
         */
        public void setRequestedAtColumnName(String requestedAtColumnName) {
            this.requestedAtColumnName = requestedAtColumnName;
        }

        /**
         * 一度に取得する最大件数を取得する。
         * 
         * @return 一度に取得する最大件数
         */
        public int getLimit() {
            return limit;
        }

        /**
         * 一度に取得する最大件数を設定する。
         * 
         * @param limit 一度に取得する最大件数
         */
        public void setLimit(int limit) {
            this.limit = limit;
        }

        /**
         * 主キーの型が文字列型かどうかを表すフラグを取得する。
         * 
         * @return 主キーの型が文字列型かどうかを表すフラグ
         */
        public boolean isStringPrimaryKey() {
            return stringPrimaryKey;
        }

        /**
         * 主キーの型が文字列型かどうかを表すフラグを設定する。
         * 
         * @param stringPrimaryKey 主キーの型が文字列型かどうかを表すフラグ
         */
        public void setStringPrimaryKey(boolean stringPrimaryKey) {
            this.stringPrimaryKey = stringPrimaryKey;
        }

        /**
         * ステータス「未処理」を表す値を取得する。
         * 
         * @return ステータス「未処理」を表す値
         */
        public String getUnprocessedStatus() {
            return unprocessedStatus;
        }

        /**
         * ステータス「未処理」を表す値を設定する。
         * 
         * @param unprocessedStatus ステータス「未処理」を表す値
         */
        public void setUnprocessedStatus(String unprocessedStatus) {
            this.unprocessedStatus = unprocessedStatus;
        }

        /**
         * ステータス「処理中」を表す値を取得する。
         * 
         * @return ステータス「処理中」を表す値
         */
        public String getProcessingStatus() {
            return processingStatus;
        }

        /**
         * ステータス「処理中」を表す値を設定する。
         * 
         * @param processingStatus ステータス「処理中」を表す値
         */
        public void setProcessingStatus(String processingStatus) {
            this.processingStatus = processingStatus;
        }

        /**
         * ステータス「処理成功」を表す値を取得する。
         * 
         * @return ステータス「処理成功」を表す値
         */
        public String getSuccessStatus() {
            return successStatus;
        }

        /**
         * ステータス「処理成功」を表す値を設定する。
         * 
         * @param successStatus ステータス「処理成功」を表す値
         */
        public void setSuccessStatus(String successStatus) {
            this.successStatus = successStatus;
        }

        /**
         * ステータス「処理失敗」を表す値を取得する。
         * 
         * @return ステータス「処理失敗」を表す値
         */
        public String getFailureStatus() {
            return failureStatus;
        }

        /**
         * ステータス「処理失敗」を表す値を設定する。
         * 
         * @param failureStatus ステータス「処理失敗」を表す値
         */
        public void setFailureStatus(String failureStatus) {
            this.failureStatus = failureStatus;
        }
    }

    /**
     * リトライの設定。
     *
     */
    public static class RetryProperties {

        /**
         * リトライ対象としない例外クラス
         */
        private List<Class<? extends Throwable>> notRetryOn = List.of();

        /**
         * 最大試行回数回数
         */
        private int maxAttempts = 5;

        /**
         * 指数バックオフの設定
         */
        private BackoffProperties backoff = new BackoffProperties();

        /**
         * リトライ対象としない例外クラスを取得する。
         * 
         * @return リトライ対象としない例外クラス
         */
        public List<Class<? extends Throwable>> getNotRetryOn() {
            return notRetryOn;
        }

        /**
         * リトライ対象としない例外クラスを設定する。
         * 
         * @param notRetryOn リトライ対象としない例外クラス
         */
        public void setNotRetryOn(List<Class<? extends Throwable>> notRetryOn) {
            this.notRetryOn = notRetryOn;
        }

        /**
         * 最大試行回数を取得する。
         * 
         * @return 最大試行回数
         */
        public int getMaxAttempts() {
            return maxAttempts;
        }

        /**
         * 最大試行回数を設定する。
         * 
         * @param maxAttempts 最大試行回数
         */
        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        /**
         * 指数バックオフの設定を取得する。
         * 
         * @return 指数バックオフの設定
         */
        public BackoffProperties getBackoff() {
            return backoff;
        }

        /**
         * 指数バックオフの設定を設定する。
         * 
         * @param backoff 指数バックオフの設定
         */
        public void setBackoff(BackoffProperties backoff) {
            this.backoff = backoff;
        }
    }

    /**
     * 指数バックオフの設定。
     *
     */
    public static class BackoffProperties {

        /**
         * 初期インターバル(ミリ秒)
         */
        private long initialInterval = TimeUnit.SECONDS.toMillis(1L);

        /**
         * 乗数
         */
        private double multiplier = 2;

        /**
         * 最大インターバル(ミリ秒)
         */
        private long maxInterval = TimeUnit.SECONDS.toMillis(10L);

        /**
         * 乱数要否
         */
        private boolean withRandom = true;

        /**
         * 初期インターバル(ミリ秒)を取得する。
         * 
         * @return 初期インターバル(ミリ秒)
         */
        public long getInitialInterval() {
            return initialInterval;
        }

        /**
         * 初期インターバル(ミリ秒)を設定する。
         * 
         * @param initialInterval 初期インターバル(ミリ秒)
         */
        public void setInitialInterval(long initialInterval) {
            this.initialInterval = initialInterval;
        }

        /**
         * 乗数を取得する。
         * 
         * @return 乗数
         */
        public double getMultiplier() {
            return multiplier;
        }

        /**
         * 乗数を設定する。
         * 
         * @param multiplier 乗数
         */
        public void setMultiplier(double multiplier) {
            this.multiplier = multiplier;
        }

        /**
         * 最大インターバル(ミリ秒)を取得する。
         * 
         * @return 最大インターバル(ミリ秒)
         */
        public long getMaxInterval() {
            return maxInterval;
        }

        /**
         * 最大インターバル(ミリ秒)を設定する。
         * 
         * @param maxInterval 最大インターバル(ミリ秒)
         */
        public void setMaxInterval(long maxInterval) {
            this.maxInterval = maxInterval;
        }

        /**
         * 乱数要否を取得する。
         * 
         * @return 乱数要否
         */
        public boolean isWithRandom() {
            return withRandom;
        }

        /**
         * 乱数要否を設定する。
         * 
         * @param withRandom 乱数要否
         */
        public void setWithRandom(boolean withRandom) {
            this.withRandom = withRandom;
        }
    }
}
