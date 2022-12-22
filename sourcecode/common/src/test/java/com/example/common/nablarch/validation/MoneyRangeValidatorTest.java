package com.example.common.nablarch.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;

@SpringBootTest
class MoneyRangeValidatorTest {

    @Autowired
    SmartValidator validator;

    @ParameterizedTest
    @CsvSource(value = {
            "0",
            "99999",
            "NULL",
    }, delimiter = '|', nullValues = "NULL")
    void testValid(Integer value) {
        TestForm form = new TestForm();
        form.value = value;

        Errors errors = new BeanPropertyBindingResult(form, "target");
        validator.validate(form, errors);
        assertFalse(errors.hasErrors());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "-1",
            "100000",
    }, delimiter = '|', nullValues = "NULL")
    void testInvalid(Integer value) {
        TestForm form = new TestForm();
        form.value = value;
        Errors errors = new BeanPropertyBindingResult(form, "target");
        validator.validate(form, errors);

        FieldError error = errors.getFieldError("value");
        assertNotNull(error);
        assertEquals(MoneyRange.class.getSimpleName(), error.getCode());
    }

    public static class TestForm {

        @MoneyRange(min = 0, max = 99999)
        public Integer value;
    }
}
