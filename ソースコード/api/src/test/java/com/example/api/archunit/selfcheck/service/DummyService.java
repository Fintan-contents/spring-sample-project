package com.example.api.archunit.selfcheck.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.api.archunit.selfcheck.configuration.DummyProperties;
import com.example.api.archunit.selfcheck.dto.sub.DummyDto;
import com.example.api.archunit.selfcheck.mapper.DummyMapper;

public class DummyService {

    @Autowired
    private DummyProperties properties;

    @Autowired
    private DummyMapper dummyMapper;

    public DummyDto doSomething(DummyDto dummyDto) {
        return new DummyDto();
    }
}
