package com.example.api.archunit.selfcheck.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controllerにトランザクション境界を設定している。
 *
 * @see com.example.api.archunit.TransactionBoundaryTest
 */
@RestController
@Transactional
public class InvalidTransactionalController {

    @Transactional
    public void doSomething() {
    }
}
