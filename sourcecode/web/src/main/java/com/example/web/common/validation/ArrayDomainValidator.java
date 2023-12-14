package com.example.web.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import nablarch.core.validation.ee.Domain;
import nablarch.core.validation.ee.DomainValidator;

/**
 * {@link ArrayDomain}のバリデーター。
 * 
 * @author sample
 *
 */
public class ArrayDomainValidator implements ConstraintValidator<ArrayDomain, String[]> {

    private DomainValidator domainValidator;

    @Override
    public void initialize(ArrayDomain constraintAnnotation) {
        Domain domain = new DomainImpl(
                constraintAnnotation.value(),
                constraintAnnotation.message(),
                constraintAnnotation.groups(),
                constraintAnnotation.payload());

        domainValidator = new DomainValidator();
        domainValidator.initialize(domain);
    }

    @Override
    public boolean isValid(String[] value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean b = true;
        for (String v : value) {
            b = b && domainValidator.isValid(v, context);
        }

        return b;
    }
}
