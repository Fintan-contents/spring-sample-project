package com.example.api.common.error;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link ExceptionHandler}でもハンドリングされなかった例外をハンドリングする{@link ErrorController}実装クラス。
 * 
 * Spring Bootのデフォルトでは{@link BasicErrorController}が使用される。
 * 
 * @author sample
 *
 */
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class DefaultErrorController extends AbstractErrorController {

    /**
     * コンストラクタ。
     * 
     * @param errorAttributes ErrorAttributes
     */
    public DefaultErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    /**
     * エラーハンドリングを行ってレスポンスを返す。
     * 
     * @param request サーブレットリクエスト
     * @return レスポンス
     */
    @RequestMapping
    public ResponseEntity<Object> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        return ResponseEntity.status(status).build();
    }
}
