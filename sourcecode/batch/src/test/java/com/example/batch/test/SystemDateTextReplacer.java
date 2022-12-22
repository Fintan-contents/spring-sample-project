package com.example.batch.test;

import com.github.database.rider.core.replacers.Replacer;
import org.dbunit.dataset.ReplacementDataSet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * システム日付文字列のReplacer。
 * <p>
 * Database Riderで読み込むデータに{@code [systemDate]}という文字列があった場合に、
 * その部分をシステム日付の文字列(yyyyMMdd)に置換する。
 * </p>
 */
public class SystemDateTextReplacer implements Replacer {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("uuuuMMdd");

    @Override
    public void addReplacements(ReplacementDataSet dataSet) {
        dataSet.addReplacementSubstring("[systemDate]", LocalDate.now().format(FORMATTER));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}
