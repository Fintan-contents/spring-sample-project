package com.example.batch;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.common.EnableCommon;

/**
 * メインクラス。
 * 
 * @author sample
 *
 */
@SpringBootApplication
@EnableCommon
// Mapperアノテーションが付いたインターフェースのみをスキャン対象とする
@MapperScan(annotationClass = Mapper.class)
public class App {

    /**
     * メインメソッド。
     * 
     * @param args 起動引数
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);

        // 常駐バッチなら以降の処理は行わない
        boolean residentBatchEnabled = context.getEnvironment().getProperty("resident-batch.enabled", boolean.class, false);
        if (residentBatchEnabled) {
            return;
        }

        int exitCode;

        try {
            exitCode = SpringApplication.exit(context);
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(App.class);
            logger.error("エラーが発生しました", e);
            exitCode = 1;
        }

        System.exit(exitCode);
    }
}
