package com.example.web.archunit.selfcheck.ajax;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.archunit.selfcheck.request.DummyRequest;
import com.example.web.archunit.selfcheck.response.DummyResponse;

@RestController
@RequestMapping("/ajax/dummy")
public class DummyController {

    @PostMapping("/{id}")
    public DummyResponse post(@PathVariable Integer id, @RequestBody @Validated DummyRequest request) {
        return null;
    }
}
