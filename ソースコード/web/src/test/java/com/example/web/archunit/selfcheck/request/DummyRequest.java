package com.example.web.archunit.selfcheck.request;

import com.example.web.archunit.selfcheck.dto.sub.DummyDto;

public class DummyRequest {

    public DummyDto toDto() {
        return new DummyDto();
    }
}
