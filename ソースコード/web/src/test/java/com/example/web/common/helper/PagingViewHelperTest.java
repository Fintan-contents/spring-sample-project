package com.example.web.common.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.web.test.WebTest;

@SpringBootTest
@WebTest
class PagingViewHelperTest {

    @Autowired
    PagingViewHelper sut;

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            " 1 |  10 | 0 | 10 | 100 | 0 | 4",
            "11 |  20 | 1 | 10 | 100 | 0 | 5",
            "21 |  30 | 2 | 10 | 100 | 0 | 6",
            "31 |  40 | 3 | 10 | 100 | 0 | 7",
            "41 |  50 | 4 | 10 | 100 | 0 | 8",
            "51 |  60 | 5 | 10 | 100 | 1 | 9",
            "61 |  70 | 6 | 10 | 100 | 2 | 9",
            "71 |  80 | 7 | 10 | 100 | 3 | 9",
            "81 |  90 | 8 | 10 | 100 | 4 | 9",
            "91 | 100 | 9 | 10 | 100 | 5 | 9",
    })
    void test(int contentBegin, int contentEnd, int page, int size, long total, int expectedBegin, int expectedEnd) {
        List<Object> content = IntStream.rangeClosed(contentBegin, contentEnd).boxed().collect(Collectors.toList());
        Pageable pageable = PageRequest.of(page, size);
        Page<?> result = new PageImpl<>(content, pageable, total);

        int[] pageNumbers = sut.getPageNumbers(result);

        int[] expected = IntStream.rangeClosed(expectedBegin, expectedEnd).toArray();
        assertArrayEquals(expected, pageNumbers, () -> Arrays.toString(pageNumbers));
    }
}
