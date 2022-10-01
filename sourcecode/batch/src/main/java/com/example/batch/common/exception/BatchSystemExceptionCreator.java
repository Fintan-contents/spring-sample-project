package com.example.batch.common.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * {@link BatchSystemException}の生成を補助するクラス。
 * <p>
 * メッセージIDからメッセージを構築し、構築したメッセージを{@link BatchSystemException}に設定して返却する
 * 機能を提供する。
 * </p>
 */
@Component
public class BatchSystemExceptionCreator {
    @Autowired
    private MessageSource messageSource;

    /**
     * 指定したメッセージIDから構築したメッセージを設定した{@link BatchSystemException}を生成する。
     * @param messageId メッセージID
     * @param args メッセージに埋め込む文字列
     * @return メッセージIDから構築したメッセージを設定した{@link {@link BatchSystemException}}
     */
    public BatchSystemException create(String messageId, Object... args) {
        String message = messageSource.getMessage(messageId, args, Locale.getDefault());
        return new BatchSystemException(message);
    }
}
