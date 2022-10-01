package com.example.batch.archunit.selfcheck.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 適切でないAPI使用が行われているクラス。
 *
 * @see com.example.batch.archunit.ApiUsageTest
 */
@Component
@StepScope
public class InvalidApiUsageReader implements ItemReader<Object> {

    /**
     * {@link ProcessBuilder}を使用している。
     */
    public void invalidApiUsageProcessBuilder() {
        ProcessBuilder processBuilder = new ProcessBuilder("ls");
    }

    /**
     * {@link Runtime}を使用している。
     */
    public void invalidApiUsageRuntime() {
        Runtime.getRuntime();
    }

    /**
     * リフレクションAPIを使用している。
     */
    public void invalidApiUsageReflection() {
        getClass().getMethods()[0].getDeclaringClass();
    }

    /**
     * {@link Thread}を使用している。
     */
    public void invalidApiUsageThread() {
        Thread thread = new Thread(() -> System.out.println("hello."));
        thread.start();
    }

    /**
     * {@link ExecutorService}、{@link Executors}を使用している。
     */
    public void invalidApiUsageExecutor() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> System.out.println("hello."));
        executorService.shutdown();
    }

    /**
     * {@link TaskExecutor}を使用している。
     */
    @Autowired
    private TaskExecutor taskExecutor;

    /**
     * レガシーAPIを使用している。
     */
    public void invalidApiUsageLegacyApi() {
        StringBuffer sb = new StringBuffer();
        Dictionary<String, Object> dictionary = new Hashtable<>();
        dictionary.put("foo", "bar");
        Enumeration<Object> enumeration = dictionary.elements();
        while (enumeration.hasMoreElements()) {
            Object element = enumeration.nextElement();
            sb.append(element);
        }
        Stack<String> stack = new Stack<>();

        StringTokenizer stringTokenizer = new StringTokenizer("123-456-789", "-");
        while (stringTokenizer.hasMoreTokens()) {
            stack.push(stringTokenizer.nextToken());
        }
        Vector<String> vector = new Vector<>();
    }

    @Override
    public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }
}
