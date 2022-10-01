package com.example.common.nablarch.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class ValidatorFactoryBuilderImplTest {

    private ValidatorFactoryBuilderImpl sut;

    @BeforeEach
    void init() {
        @SuppressWarnings("resource")
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        sut = new ValidatorFactoryBuilderImpl(localValidatorFactoryBean);
    }

    @Test
    void testUsingContext() {
        assertThrows(UnsupportedOperationException.class, () -> sut.usingContext());
    }

    @Test
    void testGetMessageInterpolator() {
        assertThrows(UnsupportedOperationException.class, () -> sut.getMessageInterpolator());
    }

    @Test
    void testGetTraversableResolver() {
        assertThrows(UnsupportedOperationException.class, () -> sut.getTraversableResolver());
    }

    @Test
    void testGetConstraintValidatorFactory() {
        assertThrows(UnsupportedOperationException.class, () -> sut.getConstraintValidatorFactory());
    }

    @Test
    void testGetParameterNameProvider() {
        assertThrows(UnsupportedOperationException.class, () -> sut.getParameterNameProvider());
    }

    @Test
    void testGetClockProvider() {
        assertThrows(UnsupportedOperationException.class, () -> sut.getClockProvider());
    }

    @Test
    void testUnwrap() {
        assertThrows(UnsupportedOperationException.class, () -> sut.unwrap(Object.class));
    }

    @Test
    void testClose() {
        sut.close();
    }
}
