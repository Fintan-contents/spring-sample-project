package com.example.web.archunit.selfcheck.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.web.archunit.selfcheck.controller.DummyController;
import com.example.web.archunit.selfcheck.form.DummyForm;
import com.example.web.archunit.selfcheck.request.DummyRequest;
import com.example.web.archunit.selfcheck.response.DummyResponse;

/**
 * 不正な依存をもつServiceクラス。
 *
 * @see com.example.web.archunit.LayeredArchitectureTest
 */
public class InvalidDependencyService {

    /**
     * ServiceクラスはControllerに依存してはいけない（逆方向の依存）。
     */
    @Autowired
    private DummyController invalidController;

    /**
     * ServiceクラスはController(AJAX)に依存してはいけない（逆方向の依存）。
     */
    @Autowired
    private com.example.web.archunit.selfcheck.ajax.DummyController invalidAjaxController;

    /**
     * ServiceクラスはFormに依存してはいけない。
     */
    public void invalidDependencyForForm(DummyForm form) {
    }

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
