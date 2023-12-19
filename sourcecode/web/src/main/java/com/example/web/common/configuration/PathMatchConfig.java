package com.example.web.common.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * URLのTrailing Slashの有無を区別しないようにするための設定。
 *
 * <p>
 * Spring Framework 6.0からtrailing slashのデフォルト値がfalseになった。
 * trueへ設定することは非推奨だが、次回に対応するまでの延命措置として一時的に採用する。
 * </p>
 *
 * @see <a href="https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#spring-mvc-and-webflux-url-matching-changes">
 *      Spring Boot 3.0 Migration Guide - Spring MVC and WebFlux URL Matching Changes
 *      </a>
 */
@Configuration
public class PathMatchConfig  implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(true);
    }
}
