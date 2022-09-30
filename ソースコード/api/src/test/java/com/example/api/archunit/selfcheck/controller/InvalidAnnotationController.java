package com.example.api.archunit.selfcheck.controller;

import org.springframework.stereotype.Controller;

/**
 * APIモジュールで、誤って{@link org.springframework.web.bind.annotation.RestController}ではなく、
 * {@link Controller}を付与しているControllerクラス。
 *
 * @see com.example.api.archunit.ControllerAnnotationTest
 */
@Controller
public class InvalidAnnotationController {
}
