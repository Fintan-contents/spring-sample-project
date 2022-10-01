package com.example.web.archunit.selfcheck.form;

import com.example.web.archunit.selfcheck.dto.sub.DummyDto;

public class DummyForm {

    public DummyDto toDummyDto() {
        return new DummyDto();
    }

    /** トップレベルでないクラスは、接尾辞Formでなくてよい。 */
    private static class DummyNestedClass {

    }
}
