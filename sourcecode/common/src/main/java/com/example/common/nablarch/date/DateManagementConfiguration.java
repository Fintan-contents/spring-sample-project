package com.example.common.nablarch.date;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Nablarchの業務日付管理機能とシステム日時管理機能の設定をインポートする設定。
 * 
 * @author sample
 *
 */
@Configuration(proxyBeanMethods = false)
@Import({
        SystemTimeConfiguration.class,
        BusinessDateConfiguration.class
})
public class DateManagementConfiguration {
}