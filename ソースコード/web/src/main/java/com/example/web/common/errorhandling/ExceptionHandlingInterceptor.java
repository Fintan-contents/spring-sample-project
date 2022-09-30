package com.example.web.common.errorhandling;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

import com.example.common.exception.ApplicationException;

/**
 * Controllerにおける例外ハンドリングのボイラープレートコードを肩代わりするインターセプター。
 * 
 * @author sample
 *
 */
@Component
@Qualifier(ExceptionHandlingInterceptor.QUALIFIER)
public class ExceptionHandlingInterceptor implements MethodInterceptor {

    public static final String QUALIFIER = "ExceptionHandlingInterceptor";

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        OnRejectError onError = AnnotationUtils.findAnnotation(invocation.getMethod(), OnRejectError.class);
        validate(invocation.getMethod());
        Errors errors = findErrors(invocation.getArguments());
        if (onError.handlingValidationError() && errors.hasErrors()) {
            return onError.path();
        }
        try {
            return invocation.proceed();
        } catch (Throwable t) {
            for (Class<? extends Exception> exceptionClass : onError.types()) {
                if (exceptionClass.isInstance(t)) {
                    reject(errors, t);
                    return onError.path();
                }
            }
            throw t;
        }
    }

    /**
     * インターセプト対象のメソッドをバリデーションする。
     * 
     * @param method インターセプト対象のメソッド
     */
    private static void validate(Method method) {
        Assert
                .state(method.getReturnType() == String.class,
                        () -> String
                                .format("@%s を付けるコントローラーのメソッドは戻り値が %s でなければいけません",
                                        OnRejectError.class.getSimpleName(),
                                        String.class.getSimpleName()));
    }

    /**
     * メソッド引数からErrorsを取り出す。
     * 
     * @param arguments メソッド引数
     * @return 取り出されたErrors
     */
    private static Errors findErrors(Object[] arguments) {
        return Arrays
                .stream(arguments)
                .filter(Errors.class::isInstance)
                .map(Errors.class::cast)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        String
                                .format("@%s を付けるコントローラーのメソッドは %s 実装クラスのインスタンスを引数に持たなければいけません",
                                        OnRejectError.class.getSimpleName(),
                                        Errors.class.getName())));
    }

    /**
     * エラーを報告する。
     * 
     * @param errors Errors
     * @param t 例外
     */
    private static void reject(Errors errors, Throwable t) {
        if (ApplicationException.class.isInstance(t)) {
            ApplicationException ae = ApplicationException.class.cast(t);
            if (ae.getField() != null) {
                errors.rejectValue(ae.getField(), ae.getCode());
            } else {
                errors.reject(ae.getCode());
            }
        } else {
            errors.reject(t.getClass().getName());
        }
    }

}