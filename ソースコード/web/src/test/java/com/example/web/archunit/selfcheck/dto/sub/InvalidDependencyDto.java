package com.example.web.archunit.selfcheck.dto.sub;

import com.example.web.archunit.selfcheck.configuration.DummyProperties;
import com.example.web.archunit.selfcheck.service.DummyService;

/**
 * 不正な依存をもつDTOクラス。
 *
 * @see com.example.web.archunit.LayeredArchitectureTest
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
