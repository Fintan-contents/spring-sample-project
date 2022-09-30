package com.example.web.archunit.selfcheck.helper;

import org.springframework.transaction.annotation.Transactional;

/**
 * {@link Transactional#readOnly()}が指定されていないViewHelperクラス。
 *
 * @see com.example.web.archunit.TransactionBoundaryTest
 */
@Transactional
public class NoReadOnlyAttributeViewHelper {

    @Transactional
    public String doSomething() {
        return "";
    }

}
