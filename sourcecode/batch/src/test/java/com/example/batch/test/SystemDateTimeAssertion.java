package com.example.batch.test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * TIMESTAMP項目に設定されたシステム日時のアサートを補助するヘルパークラス。
 * <p>
 * このクラスは、検査対象の日時が期待する日時と誤差の範囲で一致するかどうかを検証する。<br>
 * 具体的には、検査対象の日時を{@code actual}、期待する日時を{@code expected}、誤差を{@code e}とした場合、
 * 以下の条件を満たすことを検証する。<br>
 * {@code expected - e <= actual && actual <= expected}
 * </p>
 * <p>
 * このクラスは、Database Riderのscript assertionsで使用されることを想定している。
 * </p>
 * <p>
 * 記述例<br>
 * {@code groovy:(com.example.batch.test.SystemDateTimeAssertion.assertNow(value))}
 * </p>
 */
public class SystemDateTimeAssertion {
    /**
     * 誤差(秒)。
     */
    private static final int ERROR_SECONDS = 2;

    /**
     * 期待する日時をシステム日時とし、Timestampの値が誤差の範囲内で一致することを検証する。
     * @param value 検証対象の日時
     * @return 検証対象の日時がシステム日時と誤差の範囲内で一致する場合はtrue
     */
    public static boolean assertNow(Timestamp value) {
        return assertEqualsInError(LocalDateTime.now(), value.toLocalDateTime());
    }

    /**
     * 期待する日時をシステム日時+{@code weeks}週間後とし、Timestampの値が誤差の範囲内で一致することを検証する。
     * @param value 検証対象の日時
     * @param weeks 週
     * @return 検査対象の日時がシステム日時+{@code weeks}週間後の日時と誤差の範囲内で一致する場合はtrue
     */
    public static boolean assertAfterWeeks(Timestamp value, int weeks) {
        return assertEqualsInError(LocalDateTime.now().plusWeeks(weeks), value.toLocalDateTime());
    }

    /**
     * 検証対象の日時が期待する日時と誤差の範囲内で一致することを検証する。
     * @param expected 期待する日時
     * @param actual 検証対象の日時
     * @return 誤差の範囲内で一致する場合はtrue
     */
    private static boolean assertEqualsInError(LocalDateTime expected, LocalDateTime actual) {
        final LocalDateTime lower = expected.minusSeconds(ERROR_SECONDS);
        return (actual.equals(lower) || actual.isAfter(lower))
                && (actual.equals(expected) || actual.isBefore(expected));
    }
}
