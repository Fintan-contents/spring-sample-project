package com.example.batch.common.listener;

import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.example.batch.common.exception.BatchSkipItemException;
import com.example.batch.common.item.LineNumberItem;

/**
 * スキップされたアイテムの情報をログに出力するリスナー。
 */
@Component
public class LoggingSkipItemListener implements SkipListener<Object, Object> {
    private final Logger logger = LoggerFactory.getLogger(LoggingSkipItemListener.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public void onSkipInRead(Throwable t) {
        if (t instanceof FlatFileParseException && t.getCause() instanceof BindException) {
            FlatFileParseException f = (FlatFileParseException) t;
            logSkippedItemWithLineNumber(((BindException) t.getCause()), f.getLineNumber());
        } else {
            logger.warn("スキップしました", t);
        }
    }

    @Override
    public void onSkipInProcess(Object item, Throwable t) {
        if (t instanceof BatchSkipItemException) {
            BatchSkipItemException e = (BatchSkipItemException) t;
            String message = messageSource.getMessage(e.getCode(), e.getArgs(), Locale.getDefault());
            logger.warn("スキップしました エラー内容={}", message);
        } else if (t.getCause() instanceof BindException && item instanceof LineNumberItem) {
            logSkippedItemWithLineNumber(((BindException) t.getCause()), ((LineNumberItem) item).getLineNumber());
        } else {
            logger.warn("スキップしました", t);
        }
    }

    /**
     * スキップした内容をログ出力する。
     * 
     * @param bindException エラー内容を保持しているBindException
     * @param lineNumber 行番号
     */
    private void logSkippedItemWithLineNumber(BindException bindException, int lineNumber) {
        String errors = bindException
                .getAllErrors()
                .stream()
                .map(objectError -> messageSource.getMessage(objectError, Locale.getDefault()))
                .collect(Collectors.joining(", "));

        logger.warn("スキップしました 行番号={}, エラー内容={}", lineNumber, errors);
    }
}
