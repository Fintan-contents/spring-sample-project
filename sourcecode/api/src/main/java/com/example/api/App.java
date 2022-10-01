package com.example.api;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
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
public class App {

    /**
     * メインメソッド。
     * 
     * @param args 起動引数
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
