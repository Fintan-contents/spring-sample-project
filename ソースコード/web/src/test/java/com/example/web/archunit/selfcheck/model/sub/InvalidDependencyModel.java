package com.example.web.archunit.selfcheck.model.sub;

import com.example.web.archunit.selfcheck.dto.sub.DummyDto;

/**
 * 不正な依存をもつModelクラス。
 *
 * @see com.example.web.archunit.LayeredArchitectureTest
 */
public class InvalidDependencyModel {

    /**
     * Modelが他のレイヤーに依存してはいけない。
     */
    public DummyDto toDummyDto() {
        return new DummyDto();
    }
}
