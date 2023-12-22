package com.example.web.archunit.selfcheck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.ServletRequest;

/**
 * 適切でないAPI使用が行われているControllerクラス。
 *
 * @see com.example.web.archunit.ApiUsageTest
 */
@Controller
public class InvalidApiUsageController {

    /**
     * Servlet APIに依存している。
     */
    @GetMapping("/")
    public String index(ServletRequest servletRequest) {
        String[] param = servletRequest.getParameterValues("foo");
        return "index";
    }
}
