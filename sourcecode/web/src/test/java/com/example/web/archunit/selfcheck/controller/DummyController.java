package com.example.web.archunit.selfcheck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.web.archunit.selfcheck.configuration.DummyProperties;
import com.example.web.archunit.selfcheck.dto.sub.DummyDto;
import com.example.web.archunit.selfcheck.form.DummyForm;
import com.example.web.archunit.selfcheck.service.DummyService;

@Controller
public class DummyController {

    @Autowired
    private DummyProperties properties;

    @Autowired
    private DummyService service;

    @GetMapping
    public String doSomething(DummyForm form) {
        DummyDto dummyDto = form.toDummyDto();
        service.doSomething(dummyDto);
        return "foo.html";
    }
}
