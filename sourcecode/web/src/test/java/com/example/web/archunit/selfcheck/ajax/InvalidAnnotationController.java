package com.example.web.archunit.selfcheck.ajax;

import org.springframework.stereotype.Controller;

/**
 * Ajax用Controllerで、誤って{@link org.springframework.web.bind.annotation.RestController}ではなく、
 * {@link Controller}を付与しているクラス。
 *
 * @see com.example.web.archunit.ControllerAnnotationTest
 */
@Controller
public class InvalidAnnotationController {
}
