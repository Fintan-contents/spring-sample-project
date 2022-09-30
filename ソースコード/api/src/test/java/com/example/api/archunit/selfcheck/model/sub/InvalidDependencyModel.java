package com.example.api.archunit.selfcheck.model.sub;

import com.example.api.archunit.selfcheck.dto.sub.DummyDto;

/**
 * 不正な依存をもつModelクラス。
 *
 * @see com.example.api.archunit.LayeredArchitectureTest
 */
public class InvalidDependencyModel {

    /**
     * Modelが他のレイヤーに依存してはいけない。
     */
    public DummyDto toDummyDto() {
        return new DummyDto();
    }
}
