package com.example.web.archunit;

import static com.example.web.archunit.ArchUnitConfiguration.*;
import static com.example.web.archunit.ArchUnitRuleUtil.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.task.TaskExecutor;

import com.example.web.common.errorhandling.ErrorViewResolverImpl;
import com.example.web.common.errorhandling.ExceptionHandlingInterceptor;
import com.example.web.common.mvc.CsvFileDownloadView;
import com.example.web.common.upload.UploadSupport;

/**
 * API使用が適切であることをチェックするテストクラス。
 */
public class ApiUsageTest {

    /**
     * 外部プロセス起動を行うAPIを使用していないこと。
     */
    @Test
    void testNoClassesUseExternalProcess() {
        noClasses()
                .should().accessClassesThat().haveFullyQualifiedName("java.lang.Runtime")
                .orShould().accessClassesThat().haveFullyQualifiedName("java.lang.ProcessBuilder")
                .because("業務アプリケーションから外部プロセスを起動してはいけません。")
                .check(appJavaClasses);
    }

    /**
     * リフレクションAPIを使用していないこと。
     *
     * 一部の基板部品（{@link ExceptionHandlingInterceptor}）は例外として除外している。
     */
    @Test
    void testNoReflection() {
        noClasses()
                .that(notType(ExceptionHandlingInterceptor.class))
                .should().accessClassesThat().resideInAPackage("java.lang.reflect")
                .because("業務アプリケーションからリフレクションを使用してはいけません。")
                .check(appJavaClasses);
    }

    /**
     * Servlet APIを使用していないこと。
     */
    @Test
    void testNoServletAPI() {
        noClasses()
                .that(notType(ErrorViewResolverImpl.class, CsvFileDownloadView.class, UploadSupport.class))
                .should().accessClassesThat().resideInAnyPackage("jakarta.servlet..")
                .because("Servlet APIではなくSpring Web MVCのAPIを使用してください。")
                .check(appJavaClasses);
    }

    /**
     * スレッドに関するAPIを使用していないこと。
     *
     * {@link Runnable}はチェック対象外とする。
     * {@link Thread}や{@link  ExecutorService}と組み合わせて使用するので、そちらでチェックすることとする。
     *
     * また{@link java.util.concurrent} パッケージ配下には{@link java.util.concurrent.ConcurrentHashMap}や
     * {@link java.util.concurrent.atomic}パッケージ等、場合によっては使用するAPIがあるため、
     * チェック対象は必要最低限に絞っている。
     */
    @Test
    void testNoAsyncProcessing() {
        noClasses()
                .should().dependOnClassesThat().belongToAnyOf(
                        Executors.class,
                        ExecutorService.class,
                        Thread.class,
                        TaskExecutor.class)
                .because("業務アプリケーションでスレッドに関するAPIを使用してはいけません。")
                .check(appJavaClasses);
    }

    /**
     * レガシーAPIを使用していないこと。
     *
     * @param legacyApi チェック対象となるレガシーAPI
     * @param alternative 代替手段
     * @see #legacyApiSource()
     */
    @ParameterizedTest
    @MethodSource("legacyApiSource")
    void testNoLegacyApi(Class<?> legacyApi, Object alternative) {
        noClasses()
                .should().accessClassesThat().belongToAnyOf(legacyApi)
                .because("代わりに新しいAPI(" + alternative + ")を使用してください。")
                .check(appJavaClasses);
    }

    /**
     * レガシーAPIを返却する。
     *
     * @return チェック対象となるレガシーAPIとその代替手段の組み合わせ
     */
    static Stream<Arguments> legacyApiSource() {
        return Stream
                .of(
                        arguments(StringBuffer.class, StringBuilder.class),
                        arguments(Dictionary.class, Map.class),
                        arguments(Enumeration.class, Iterator.class),
                        arguments(Hashtable.class, HashMap.class),
                        arguments(Stack.class, Deque.class),
                        arguments(StringTokenizer.class, "Stringのsplitメソッドまたはjava.util.regexパッケージのAPI"),
                        arguments(Vector.class, ArrayList.class));
    }

}
