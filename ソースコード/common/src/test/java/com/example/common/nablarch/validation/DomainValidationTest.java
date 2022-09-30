package com.example.common.nablarch.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

@SpringBootTest
public class DomainValidationTest {

    @Autowired
    private SpringValidatorAdapter validator;
    @Autowired
    private MessageSource messageSource;

    @Test
    void testInvalidUserName() {
        ExampleForm form = new ExampleForm();
        form.setUserName("a");

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(form, "form");

        assertEquals(0, bindingResult.getAllErrors().size());

        validator.validate(form, bindingResult);

        assertEquals(1, bindingResult.getAllErrors().size());

        FieldError fieldError = bindingResult.getFieldError("userName");
        assertNotNull(fieldError);
        assertEquals("全角で入力してください。", messageSource.getMessage(fieldError, null));
    }

    @Test
    void testValidUserName() {
        ExampleForm form = new ExampleForm();
        form.setUserName("あ");

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(form, "form");

        assertEquals(0, bindingResult.getAllErrors().size());

        validator.validate(form, bindingResult);

        assertEquals(0, bindingResult.getAllErrors().size());
    }
}
