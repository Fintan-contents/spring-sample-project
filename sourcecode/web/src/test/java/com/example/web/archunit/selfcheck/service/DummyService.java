package com.example.web.archunit.selfcheck.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.web.archunit.selfcheck.configuration.DummyProperties;
import com.example.web.archunit.selfcheck.dto.sub.DummyDto;
import com.example.web.archunit.selfcheck.mapper.DummyMapper;

public class DummyService {

    @Autowired
    private DummyProperties properties;

    @Autowired
    private DummyMapper dummyMapper;

    public void doSomething(DummyDto dummyDto) {

    }
}
