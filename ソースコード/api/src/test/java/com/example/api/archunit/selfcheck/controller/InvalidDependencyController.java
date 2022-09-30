package com.example.api.archunit.selfcheck.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.api.archunit.selfcheck.mapper.DummyMapper;

/**
 * 不正な依存をもつControllerクラス。
 *
 * @see com.example.api.archunit.LayeredArchitectureTest
 */
@RestController
public class InvalidDependencyController {

    /**
     * ControllerがMapperに依存している（レイヤーをまたいだ依存）。
     */
    private DummyMapper invalidMapperUsage;

}
