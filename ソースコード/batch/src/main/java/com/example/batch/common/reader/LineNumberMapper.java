package com.example.batch.common.reader;

import org.springframework.batch.item.file.LineMapper;

import com.example.batch.common.item.LineNumberItem;

/**
 * {@link LineMapper}が変換したアイテムに行番号を設定する処理を追加したマッパー。
 * @param <T> 変換後のアイテムの型
 */
public class LineNumberMapper<T extends LineNumberItem> implements LineMapper<T> {
    private final LineMapper<T> delegate;

    /**
     * コンストラクタ。
     * @param delegate アイテムへの変換の委譲先となるマッパー
     */
    public LineNumberMapper(LineMapper<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
        final T item = delegate.mapLine(line, lineNumber);
        item.setLineNumber(lineNumber);
        return item;
    }
}
