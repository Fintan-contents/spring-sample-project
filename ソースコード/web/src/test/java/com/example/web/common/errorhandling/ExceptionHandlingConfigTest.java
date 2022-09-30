package com.example.web.common.errorhandling;

import static org.junit.jupiter.api.Assertions.*;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

public class ExceptionHandlingConfigTest {

    AnnotationConfigApplicationContext applicationContext;

    @BeforeEach
    void init() {
        applicationContext = new AnnotationConfigApplicationContext(ExceptionHandlingConfig.class,
                TestMethodInterceptor.class,
                TestConfig.class,
                TestController.class,
                NoOnErrorAnnotationController.class);
    }

    @AfterEach
    void close() {
        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    @Test
    void アノテーションが付いたメソッドはインターセプトの対象() {
        var controller = applicationContext.getBean(TestController.class);
        assertTrue(AopUtils.isAopProxy(controller));
        assertEquals("/intercepted", controller.method1(null));
    }

    @Test
    void アノテーションが付いていないメソッドはインターセプトの対象外() {
        var controller = applicationContext.getBean(TestController.class);
        assertTrue(AopUtils.isAopProxy(controller));
        assertEquals("/ok", controller.method2(null));
    }

    @Test
    void アノテーションが付いていないメソッドだけからなるコントローラーはproxyではない() {
        var controller = applicationContext.getBean(NoOnErrorAnnotationController.class);
        assertFalse(AopUtils.isAopProxy(controller));
        assertEquals("/ok", controller.method3(null));
    }

    @Component
    @Qualifier(ExceptionHandlingInterceptor.QUALIFIER)
    public static class TestMethodInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return "/intercepted";
        }
    }

    @Configuration
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    public static class TestConfig {
    }

    public static class TestController {

        @OnRejectError(path = "/error")
        public String method1(BindingResult bindingResult) {
            return "/ok";
        }

        public String method2(BindingResult bindingResult) {
            return "/ok";
        }
    }

    public static class NoOnErrorAnnotationController {

        public String method3(BindingResult bindingResult) {
            return "/ok";
        }
    }
}