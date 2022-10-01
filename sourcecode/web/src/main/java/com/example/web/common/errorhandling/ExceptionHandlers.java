package com.example.web.common.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.common.exception.DataNotFoundException;

import jp.fintan.keel.spring.web.token.transaction.InvalidTransactionTokenException;

/**
 * 例外ハンドラーを集約したクラス。
 * 
 * @author sample
 *
 */
@ControllerAdvice(basePackages = "com.example.web")
public class ExceptionHandlers {

    /**
     * {@link DataNotFoundException}をハンドリングする。
     * 
     * @return 画面テンプレートのパス
     */
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String handleDataNotFoundException() {
        return "error/404";
    }

    /**
     * 二重サブミットエラーをハンドリングする。
     * 
     * @return 画面テンプレートのパス
     */
    @ExceptionHandler(InvalidTransactionTokenException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleInvalidTransactionTokenException() {
        return "error/doubleSubmissionError";
    }
}
