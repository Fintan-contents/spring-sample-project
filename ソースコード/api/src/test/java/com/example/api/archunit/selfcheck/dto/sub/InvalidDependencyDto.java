package com.example.api.archunit.selfcheck.dto.sub;

import com.example.api.archunit.selfcheck.configuration.DummyProperties;
import com.example.api.archunit.selfcheck.service.DummyService;

/**
 * 不正な依存をもつDTOクラス。
 *
 * @see com.example.api.archunit.LayeredArchitectureTest
 */
public class InvalidDependencyDto {

    /**
     * DtoクラスはServiceに依存してはいけない。
     */
    public void doSomething(DummyService invalid) {
        invalid.doSomething(new DummyDto());
    }

    /**
     * DtoクラスはPropertiesに依存してはいけない。
     */
    public void doSomething(DummyProperties invalid) {
    }
}
