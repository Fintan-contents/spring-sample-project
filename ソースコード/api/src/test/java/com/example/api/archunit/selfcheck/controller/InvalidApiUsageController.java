package com.example.api.archunit.selfcheck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;

/**
 * 適切でないAPI使用が行われているControllerクラス。
 *
 * @see com.example.api.archunit.ApiUsageTest
 */
@RestController
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
