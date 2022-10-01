package com.example.web.common.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import com.example.web.test.WebTest;

@SpringBootTest
@WebTest
public class ArrayDomainTest {

    @Autowired
    SmartValidator validator;

    @Test
    void testValidate() {
        TestForm form = new TestForm();
        form.testField = new String[] { "01", "02", "03", "XX", "05" };

        Errors errors = new BeanPropertyBindingResult(form, "testForm");
        validator.validate(form, errors);

        assertEquals(1, errors.getFieldErrorCount("testField"));
    }

    @Test
    void testValidIfNull() {
        TestForm form = new TestForm();
        form.testField = null;

        Errors errors = new BeanPropertyBindingResult(form, "testForm");
        validator.validate(form, errors);

        assertEquals(0, errors.getFieldErrorCount("testField"));
    }

    static class TestForm {

        @ArrayDomain("projectType")
        String[] testField;
    }
}
