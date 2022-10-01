package com.example.web.archunit.selfcheck.dto.sub;

import com.example.web.archunit.selfcheck.model.sub.DummyModel;

public class DummyDto {

    DummyModel toDummyModel() {
        return new DummyModel();
    }
}
