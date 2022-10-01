package com.example.api.archunit.selfcheck.response;

import com.example.api.archunit.selfcheck.dto.sub.DummyDto;

public class DummyResponse {

    public static DummyResponse fromDto(DummyDto dummyDto) {
        return new DummyResponse();
    }

    /**
     * 命名規約に反したResponseクラス。
     *
     * @see com.example.api.archunit.NamingConventionTest
     */
    public static class ResponseInvalidName {
    }
}
