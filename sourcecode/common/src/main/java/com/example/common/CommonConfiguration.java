package com.example.common;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * commonプロジェクトのConfiguration。
 * 主にSpring管理下に置きたいコンポーネントとMapperのスキャンを行う。
 * 
 * @author sample
 *
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan
// Mapperアノテーションが付いたインターフェースのみをスキャン対象とする
@MapperScan(annotationClass = Mapper.class)
public class CommonConfiguration {
}
