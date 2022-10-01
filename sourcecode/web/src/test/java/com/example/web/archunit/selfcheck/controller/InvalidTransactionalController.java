package com.example.web.archunit.selfcheck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

/**
 * Controllerにトランザクション境界を設定している。
 *
 * @see com.example.web.archunit.TransactionBoundaryTest
 */
@Controller
@Transactional
public class InvalidTransactionalController {

    @Transactional
    public void doSomething() {
    }
}
