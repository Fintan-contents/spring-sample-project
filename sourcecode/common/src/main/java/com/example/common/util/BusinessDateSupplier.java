package com.example.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import nablarch.core.date.BusinessDateProvider;

/**
 * 業務日付の取得機能を提供するクラス。
 * 
 * @author sample
 * 
 */
@Component
@ConfigurationProperties("business-date")
public class BusinessDateSupplier {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("uuuuMMdd");

    @Autowired
    private BusinessDateProvider businessDateProvider;

    /**
     * 業務日付の上書き値(yyyyMMdd)。
     */
    private String fixedDate;

    /**
     * デフォルトのセグメントの業務日付を取得する。
     * <p>
     * プロパティ {@code businessDate.fixedDate} が設定されている場合、そちらの日付が優先される。<br>
     * 例: {@code -DbusinessDate.fixedDate=20210102}
     * </p>
     * @return 業務日付
     */
    public LocalDate getDate() {
        /*
         * ■Nablarch が提供する業務日付を上書きする機能を利用しない理由
         * Nablarch が提供している業務日付をシステムプロパティで上書きする仕組みは、
         * DiContainer クラスを使用したときに働くようになっている。
         * 本システムでは、システムリポジトリを DiContainer を使わずに構築しているため、
         * この機能は利用できない。
         *
         * また、業務日付の上書きはシステムプロパティではなく java コマンドの引数で
         * できた方が他の設定値の上書きと同列に扱えて都合がいい。
         * このため、Springのプロパティの仕組みで上書きするようにしている。
         */
        String stringDate = fixedDate != null ? fixedDate : businessDateProvider.getDate();
        return LocalDate.parse(stringDate, FORMATTER);
    }

    /**
     * 業務日付の上書き値を設定する。
     * @param fixedDate 業務日付の上書き値(yyyyMMdd)
     */
    public void setFixedDate(String fixedDate) {
        this.fixedDate = fixedDate;
    }
}
