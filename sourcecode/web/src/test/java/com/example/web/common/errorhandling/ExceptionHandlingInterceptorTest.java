package com.example.web.common.errorhandling;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.util.SimpleMethodInvocation;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import com.example.common.exception.ApplicationException;
import com.example.common.exception.OptimisticLockException;

public class ExceptionHandlingInterceptorTest {

    ExceptionHandlingInterceptor sut;
    TestController targetObject;
    MapBindingResult errors;

    @BeforeEach
    void init() {
        sut = new ExceptionHandlingInterceptor();
        targetObject = new TestController();
        errors = new MapBindingResult(Map.of(), "testForm");
    }

    /**
     * 正常オペレーション。
     * 
     * @throws Throwable Interceptorがスローする場合
     */
    @Test
    void testNormalOperation() throws Throwable {
        MethodInvocation invocation = new SimpleMethodInvocationImpl(targetObject, "method1",
                errors);
        Object actual = sut.invoke(invocation);
        assertEquals("/ok", actual);
    }

    /**
     * バリデーションエラーがある場合はインターセプターがエラー時のパスを返す。
     * 
     * @throws Throwable Interceptorがスローする場合
     */
    @Test
    void testHandledIfValidationError() throws Throwable {
        // バリデーションエラーのエミュレート
        errors.rejectValue("errorfield", "errorcode");

        MethodInvocation invocation = new SimpleMethodInvocationImpl(targetObject, "method1",
                errors);
        Object actual = sut.invoke(invocation);
        assertEquals("/error", actual);
    }

    /**
     * バリデーションエラーがあってもインターセプターでハンドリングしない設定。
     * 
     * @throws Throwable Interceptorがスローする場合
     */
    @Test
    void testNotHandledValidationError() throws Throwable {
        // バリデーションエラーのエミュレート
        errors.rejectValue("errorfield", "errorcode");

        MethodInvocation invocation = new SimpleMethodInvocationImpl(targetObject, "method2",
                errors);
        Object actual = sut.invoke(invocation);
        assertEquals("/ok", actual);
    }

    /**
     * ApplicationExceptionがスローされた場合はインターセプターでハンドリングする1。
     * 
     * @throws Throwable Interceptorがスローする場合
     */
    @Test
    void testHandledIfApplicationExceptionThrown1() throws Throwable {
        MethodInvocation invocation = new SimpleMethodInvocationImpl(targetObject, "method3",
                errors);
        Object actual = sut.invoke(invocation);
        assertEquals("/error", actual);
        ObjectError objectError = errors.getGlobalError();
        assertEquals("errorcode", objectError.getCode());
    }

    /**
     * ApplicationExceptionがスローされた場合はインターセプターでハンドリングする2。
     * 
     * @throws Throwable Interceptorがスローする場合
     */
    @Test
    void testHandledIfApplicationExceptionThrown2() throws Throwable {
        MethodInvocation invocation = new SimpleMethodInvocationImpl(targetObject, "method4",
                errors);
        Object actual = sut.invoke(invocation);
        assertEquals("/error", actual);
        FieldError fieldError = errors.getFieldError("errorfield");
        assertEquals("errorcode", fieldError.getCode());
    }

    /**
     * OptimisticLockExceptionがスローされた場合はインターセプターでハンドリングする。
     * 
     * @throws Throwable Interceptorがスローする場合
     */
    @Test
    void testHandledIfOptimisticLockExceptionThrown() throws Throwable {
        MethodInvocation invocation = new SimpleMethodInvocationImpl(targetObject, "method5",
                errors);
        Object actual = sut.invoke(invocation);
        assertEquals("/error", actual);
        ObjectError objectError = errors.getGlobalError();
        assertEquals(OptimisticLockException.class.getName(), objectError.getCode());
    }

    /**
     * 設定されていない例外はハンドリングしない。
     * 
     * @throws Throwable Interceptorがスローする場合
     */
    @Test
    void testExceptionNoConfiguredIsNotHandled() throws Throwable {
        MethodInvocation invocation = new SimpleMethodInvocationImpl(targetObject, "method6",
                errors);
        assertThrows(RuntimeException.class, () -> sut.invoke(invocation));
    }

    /**
     * OnErrorで注釈するメソッドは引数にBindingResultを取らないといけない。
     * 
     * @throws Throwable Interceptorがスローする場合
     */
    @Test
    void testArgumentsShouldBeContainBindingResult() throws Throwable {
        MethodInvocation invocation = new SimpleMethodInvocationImpl(targetObject, "method7");
        assertThrows(IllegalArgumentException.class, () -> sut.invoke(invocation));
    }

    /**
     * OnErrorで注釈するメソッドは戻り値がStringでなければいけない。
     * 
     * @throws Throwable Interceptorがスローする場合
     */
    @Test
    void testReturnTypeShouldBeString() throws Throwable {
        MethodInvocation invocation = new SimpleMethodInvocationImpl(targetObject, "method8",
                errors);
        assertThrows(IllegalStateException.class, () -> sut.invoke(invocation));
    }

    /**
     * 設定された例外をハンドリングする。
     * 
     * @throws Throwable Interceptorがスローする場合
     */
    @Test
    void testHandledAnyException() throws Throwable {
        MethodInvocation invocation = new SimpleMethodInvocationImpl(targetObject, "method9",
                errors);
        Object actual = sut.invoke(invocation);
        assertEquals("/error", actual);
        ObjectError globalError = errors.getGlobalError();
        assertEquals(RuntimeException.class.getName(), globalError.getCode());
    }

    public static class TestController {

        @OnRejectError(path = "/error")
        public String method1(BindingResult bindingResult) {
            return "/ok";
        }

        @OnRejectError(path = "/error", handlingValidationError = false)
        public String method2(BindingResult bindingResult) {
            return "/ok";
        }

        @OnRejectError(path = "/error")
        public String method3(BindingResult bindingResult) {
            throw ApplicationException.globalError("errorcode");
        }

        @OnRejectError(path = "/error")
        public String method4(BindingResult bindingResult) {
            throw ApplicationException.fieldError("errorfield", "errorcode");
        }

        @OnRejectError(path = "/error")
        public String method5(BindingResult bindingResult) {
            throw new OptimisticLockException();
        }

        @OnRejectError(path = "/error")
        public String method6(BindingResult bindingResult) {
            throw new RuntimeException();
        }

        @OnRejectError(path = "/error")
        public String method7() {
            return fail();
        }

        @OnRejectError(path = "/error")
        public ModelAndView method8(BindingResult bindingResult) {
            return fail();
        }

        @OnRejectError(path = "/error", types = RuntimeException.class)
        public String method9(BindingResult bindingResult) {
            throw new RuntimeException();
        }

        public void method10() {
        }
    }

    private static class SimpleMethodInvocationImpl extends SimpleMethodInvocation {

        public SimpleMethodInvocationImpl(Object targetObject, String method, Object... arguments) {
            super(targetObject,
                    ReflectionUtils.findMethod(targetObject.getClass(), method, (Class<?>[]) null),
                    arguments);
        }

        @Override
        public Object proceed() {
            return ReflectionUtils.invokeMethod(getMethod(), getThis(), getArguments());
        }
    }
}