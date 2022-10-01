package com.example.api.archunit.selfcheck.mapper;

import com.example.api.archunit.selfcheck.model.sub.DummyModel;

public interface DummyMapper {

    DummyModel select(Integer id);
}
