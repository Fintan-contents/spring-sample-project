package com.example.api.test;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * テスト補助用のRestController。
 * 
 * @author sample
 *
 */
@RestController
@RequestMapping("/tests")
public class TestController {

    /**
     * {@link ExceptionHandler}でハンドリングされない例外をスローする。
     * 
     * @return 必ず例外をスローするため戻り値は使われない
     */
    @GetMapping("unhandle-exception")
    public Object throwUnhandleException() {
        throw new RuntimeException();
    }
}
