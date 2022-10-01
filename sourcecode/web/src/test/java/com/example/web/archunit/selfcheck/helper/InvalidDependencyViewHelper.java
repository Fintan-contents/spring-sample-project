package com.example.web.archunit.selfcheck.helper;

import com.example.web.archunit.selfcheck.service.DummyService;

/**
 * 不正な依存をもつViewHelperクラス。
 *
 * @see com.example.web.archunit.LayeredArchitectureTest
 */
public class InvalidDependencyViewHelper {

    private DummyService invalidUsageService;
}
