package com.example.web.archunit.selfcheck.mapper;

import com.example.web.archunit.selfcheck.model.sub.DummyModel;

public interface DummyMapper {

    DummyModel select(Integer id);
}
