package com.example.api.archunit.selfcheck.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.api.archunit.LayeredArchitectureTest;
import com.example.api.archunit.selfcheck.controller.DummyController;
import com.example.api.archunit.selfcheck.request.DummyRequest;
import com.example.api.archunit.selfcheck.response.DummyResponse;

/**
 * 不正な依存をもつServiceクラス。
 *
 * @see LayeredArchitectureTest
 */
public class InvalidDependencyService {

    /**
     * ServiceクラスはControllerに依存してはいけない（逆方向の依存）。
     */
    @Autowired
    private DummyController invalid;

    /**
     * ServiceクラスはRequestに依存してはいけない。
     */
    public void invalidDependencyForRequest(DummyRequest request) {
    }

    /**
     * ServiceクラスはResponseに依存してはいけない。
     */
    public DummyResponse invalidDependencyForResponse() {
        return null;
    }
}
