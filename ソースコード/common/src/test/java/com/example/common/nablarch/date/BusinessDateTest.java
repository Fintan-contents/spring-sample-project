package com.example.common.nablarch.date;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nablarch.core.date.BusinessDateProvider;

@SpringBootTest
public class BusinessDateTest {
    @Autowired
    private BusinessDateProvider businessDateProvider;

    @Test
    void getDate() {
        String date = businessDateProvider.getDate();
        assertEquals("20200101", date);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "00 | 20200101",
            "01 | 20210101",
            "02 | 20220101",
    })
    void getDateSegment(String segment, String expected) {
        String date = businessDateProvider.getDate(segment);
        assertEquals(expected, date);
    }
}
