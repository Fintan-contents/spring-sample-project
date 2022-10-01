package com.example.batch.archunit.selfcheck.item;

import com.example.batch.common.item.LineNumberItem;

public class DummyItem implements LineNumberItem {
    @Override
    public void setLineNumber(int lineNumber) {

    }

    @Override
    public int getLineNumber() {
        return 0;
    }
}
