package com.example.api.archunit.selfcheck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.archunit.selfcheck.configuration.DummyProperties;
import com.example.api.archunit.selfcheck.dto.sub.DummyDto;
import com.example.api.archunit.selfcheck.request.DummyRequest;
import com.example.api.archunit.selfcheck.response.DummyResponse;
import com.example.api.archunit.selfcheck.service.DummyService;

@RestController
public class DummyController {

    @Autowired
    private DummyProperties properties;

    @Autowired
    private DummyService service;

    @GetMapping
    public DummyResponse doSomething(DummyRequest request) {
        DummyDto dummyDto = request.toDto();
        DummyDto ret = service.doSomething(dummyDto);
        return DummyResponse.fromDto(ret);
    }
}
