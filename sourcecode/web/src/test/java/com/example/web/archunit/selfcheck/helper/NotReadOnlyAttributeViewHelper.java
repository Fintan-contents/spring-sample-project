package com.example.web.archunit.selfcheck.helper;

import org.springframework.transaction.annotation.Transactional;

/**
 * {@link Transactional#readOnly()}にtrueが指定されていないViewHelperクラス。
 *
 * @see import com.example.web.archunit.TransactionBoundaryTest
 */
@Transactional(readOnly = false)
public class NotReadOnlyAttributeViewHelper {

    @Transactional(readOnly = false)
    public String doSomething() {
        return "";
    }

}
