package com.example.common.util;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import nablarch.core.date.BusinessDateProvider;

@SpringJUnitConfig(classes = { BusinessDateSupplier.class })
class BusinessDateSupplierTest {
    @Autowired
    private BusinessDateSupplier sut;
    @MockBean
    private BusinessDateProvider provider;

    @Test
    void testGetDate() {
        when(provider.getDate()).thenReturn("20201022");

        LocalDate actual = sut.getDate();

        assertThat(actual).isEqualTo("2020-10-22");
    }

    @Test
    void testSetFixedDate() {
        sut.setFixedDate("20210102");

        LocalDate actual = sut.getDate();

        assertThat(actual).isEqualTo("2021-01-02");
    }

    @AfterEach
    void tearDown() {
        sut.setFixedDate(null);
    }
}