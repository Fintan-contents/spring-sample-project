package com.example.batch.common.resident.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 常駐バッチのBeanを定義するConfig。
 *
 */
@Configuration
public class ResidentBatchConfig {

	/**
	 * ResidentBatchPropertiesを構築する。
	 * 
	 * @return 構築されたインスタンス
	 */
	@Bean
	@ConfigurationProperties(prefix = "resident-batch")
	public ResidentBatchProperties residentBatchProperties() {
		return new ResidentBatchProperties();
	}
}
