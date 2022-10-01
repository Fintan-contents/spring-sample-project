package com.example.batch.common.item;

/**
 * 入力したアイテムに対応する入力ファイル上の行番号を持つことを表すインタフェース。
 */
public interface LineNumberItem {

    /**
     * 行番号を設定する。
     * @param lineNumber 行番号
     */
    void setLineNumber(int lineNumber);

    /**
     * 行番号を取得する。
     * @return 行番号
     */
    int getLineNumber();
}
