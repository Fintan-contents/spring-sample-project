package com.example.web.common.errorhandling;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link ExceptionHandlingInterceptor}を設定するためのクラス。
 * 
 * @author sample
 *
 */
@Configuration
public class ExceptionHandlingConfig {

    @Autowired
    @Qualifier(ExceptionHandlingInterceptor.QUALIFIER)
    private MethodInterceptor interceptor;

    /**
     * {@link OnRejectError}アノテーションが付けられたメソッドにインターセプターを適用する。
     * 
     * @return PointcutAdvisor
     */
    @Bean
    public DefaultPointcutAdvisor exceptionHandlingPointcutAdvisor() {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        Pointcut pointcut = AnnotationMatchingPointcut.forMethodAnnotation(OnRejectError.class);
        advisor.setPointcut(pointcut);
        advisor.setAdvice(interceptor);
        return advisor;
    }
}