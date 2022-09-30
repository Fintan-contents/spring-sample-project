package com.example.web.common.helper;

import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ページングに関するViewHelper。
 * 
 * @author sample
 *
 */
@Component
@Transactional(readOnly = true)
public class PagingViewHelper {

    /**
     * 現在のページ情報をもとにナビゲーションを行うページ番号の配列を生成して返す。
     *
     * @param result 現在のページ情報
     * @return ナビゲーションを行うページ番号の配列
     */
    public int[] getPageNumbers(Page<?> result) {

        int size = 4;
        int start = Math.max(result.getNumber() - size, result.getPageable().first().getPageNumber());
        int end = Math.min(result.getNumber() + size, result.getTotalPages() - 1);

        return IntStream.rangeClosed(start, end).toArray();
    }
}
