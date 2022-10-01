package com.example.api.archunit.selfcheck.request;

import com.example.api.archunit.selfcheck.dto.sub.DummyDto;

public class DummyRequest {

    public DummyDto toDto() {
        return new DummyDto();
    }
}
