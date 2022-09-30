package com.example.web.archunit.selfcheck.controller;

import org.springframework.web.bind.annotation.RestController;

/**
 * web用Controllerで、誤って{@link org.springframework.stereotype.Controller}ではなく
 * {@link RestController}を付与しているクラス。
 *
 * @see com.example.web.archunit.ControllerAnnotationTest
 */
@RestController
public class InvalidAnnotationController {
}
