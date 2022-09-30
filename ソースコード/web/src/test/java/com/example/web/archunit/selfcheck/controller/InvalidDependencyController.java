package com.example.web.archunit.selfcheck.controller;

import org.springframework.stereotype.Controller;

import com.example.web.archunit.selfcheck.helper.DummyViewHelper;
import com.example.web.archunit.selfcheck.mapper.DummyMapper;

/**
 * 不正な依存をもつControllerクラス。
 *
 * @see com.example.web.archunit.LayeredArchitectureTest
 */
@Controller
public class InvalidDependencyController {

    /**
     * ControllerがMapperに依存している（レイヤーをまたいだ依存）。
     */
    private DummyMapper invalidMapperUsage;

    /**
     * ControllerがViewHelperに依存している（使用方法、責務配置の誤り）。
     */
    private DummyViewHelper invalidViewHelperUsage;

}
