package com.example.batch;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
// Spring Batchを有効化する
@EnableBatchProcessing
public class App {

    /**
     * メインメソッド。
     * 
     * @param args 起動引数
     */
    public static void main(String[] args) {
        int exitCode;

        try {
            exitCode = SpringApplication.exit(SpringApplication.run(App.class, args));
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(App.class);
            logger.error("エラーが発生しました", e);
            exitCode = 1;
        }

        System.exit(exitCode);
    }
}
