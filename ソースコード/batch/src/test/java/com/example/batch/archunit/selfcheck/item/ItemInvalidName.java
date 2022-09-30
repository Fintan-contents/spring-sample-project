package com.example.batch.archunit.selfcheck.item;

import com.example.batch.common.item.LineNumberItem;

/**
 * 命名規約に反したItemクラス。
 *
 * @see com.example.batch.archunit.NamingConventionTest
 */
public class ItemInvalidName implements LineNumberItem {
    @Override
    public void setLineNumber(int lineNumber) {

    }

    @Override
    public int getLineNumber() {
        return 0;
    }
}
