package com.example.web.archunit.selfcheck.response;

import com.example.web.archunit.selfcheck.dto.sub.DummyDto;

public class DummyResponse {

    public static DummyResponse fromDto(DummyDto dummyDto) {
        return new DummyResponse();
    }
}
