package com.example.api.common;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.api.common.error.ApiError;
import com.example.api.common.exception.BusinessException;
import com.example.common.exception.OptimisticLockException;

/**
 * 例外ハンドラー
 */
@RestControllerAdvice(basePackages = "com.example.api")
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * バリデーションエラーのハンドリング
     * 
     * @param exception バリデーションエラー
     * @param locale ロケール
     * @return 障害レスポンス
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException exception, Locale locale) {
        List<String> messages = exception
                .getAllErrors().stream()
                .map(fieldError -> messageSource.getMessage(fieldError, locale))
                .sorted()
                .collect(Collectors.toList());

        ApiError apiError = new ApiError("FB1999901", messages);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * キャスト失敗時のハンドリング
     * パスパラメータのキャストに失敗した場合などに送出される例外をハンドリングする
     *
     * @param locale ロケール
     * @return 障害レスポンス
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(Locale locale) {
        String message = messageSource.getMessage("errors.invalid.request.parameter", null, locale);

        ApiError apiError = new ApiError("FB1999901", message);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * リクエストボディ変換エラーのハンドリング。
     * 
     * @param locale ロケール
     * @return 障害レスポンス
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(Locale locale) {
        String message = messageSource.getMessage("errors.invalid.request.parameter", null, locale);

        ApiError apiError = new ApiError("FB1999901", message);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 業務例外のハンドリング
     *
     * @param exception 業務例外
     * @param locale ロケール
     * @return 障害レスポンス
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException exception, Locale locale) {
        String message = messageSource.getMessage(exception.getMessageId(), exception.getArgs(), locale);

        ApiError apiError = new ApiError(exception.getFaultCode(), message);
        return new ResponseEntity<>(apiError, new HttpHeaders(), exception.getStatus());
    }

    /**
     * 重複エラーのハンドリング
     *
     * @param locale ロケール
     * @return 障害レスポンス
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(Locale locale) {
        String message = messageSource.getMessage("errors.register.duplicate", null, locale);

        ApiError apiError = new ApiError("FB1999904", message);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    /**
     * 楽観排他エラーのハンドリング
     * 
     * @param locale ロケール
     * @return 障害レスポンス
     */
    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<Object> handleOptimisticLockException(Locale locale) {
        String message = messageSource.getMessage("errors.optimistic.lock.exception", null, locale);

        ApiError apiError = new ApiError("FB1999905", message);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.CONFLICT);
    }
}
