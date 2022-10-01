package com.example.batch.archunit.selfcheck.configuration;

import org.springframework.context.annotation.Configuration;

import com.example.batch.common.configuration.BatchBaseConfig;

/**
 * 命名規約に反したConfigクラス。
 *
 * @see com.example.batch.archunit.NamingConventionTest
 */
@Configuration
public class ConfigInvalidName extends BatchBaseConfig {

}
