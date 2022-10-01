package com.example.web.top.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * トップページのController。
 * 
 * @author sample
 *
 */
@Controller
@RequestMapping("/")
public class TopController {

    /**
     * トップページを表示する。
     * 
     * @return 画面テンプレート
     */
    @GetMapping
    public String index() {
        return "top/index";
    }
}
