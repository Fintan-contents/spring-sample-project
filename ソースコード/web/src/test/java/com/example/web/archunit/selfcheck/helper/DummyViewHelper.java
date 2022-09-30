package com.example.web.archunit.selfcheck.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.example.web.archunit.selfcheck.dto.sub.DummyDto;
import com.example.web.archunit.selfcheck.mapper.DummyMapper;

@Transactional(readOnly = true)
public class DummyViewHelper {

    @Autowired
    private DummyMapper mapper;

    String format(DummyDto dto) {
        return "";
    }

    @Transactional(readOnly = true)
    private String doSomething() {
        return "";
    }
}
