package com.example.web.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ログインのController。
 * 認証処理の大半はSpring Securityに移譲するため、ログイン画面を初期表示するためのメソッドのみを定義している。
 * 
 * @author sample
 *
 */
@Controller
@RequestMapping("/login")
public class SecurityLoginController {

    /**
     * ログイン画面を表示する。
     * 
     * @return 画面テンプレートのパス
     */
    @GetMapping
    public String index() {
        return "security/login/index";
    }
}
