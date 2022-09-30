package com.example.common.nablarch.validation;

import nablarch.core.validation.validator.unicode.CharsetDef;
import nablarch.core.validation.validator.unicode.LiteralCharsetDef;

/**
 * Nablarchデフォルト設定に含まれる文字種の定義を読み込むためのProperties。
 *
 */
public class CharsetDefProperties {

    /** 半角数字 */
    private CharsetDef numericCharset = new LiteralCharsetDef();

    /** 半角英小文字 */
    private CharsetDef lowerAlphabetCharset = new LiteralCharsetDef();

    /** 半角英大文字 */
    private CharsetDef upperAlphabetCharset = new LiteralCharsetDef();

    /** ASCII記号 */
    private CharsetDef asciiSymbolCharset = new LiteralCharsetDef();

    /** 全角スペース */
    private CharsetDef zenkakuSpaceCharset = new LiteralCharsetDef();

    /** 半角カナ */
    private CharsetDef hankakuKanaCharset = new LiteralCharsetDef();

    /** JIS第1水準に含まれる全角英字 */
    private CharsetDef zenkakuAlphaCharset = new LiteralCharsetDef();

    /** JIS第1水準に含まれる全角数字 */
    private CharsetDef zenkakuNumCharset = new LiteralCharsetDef();

    /** JIS第1水準に含まれる全角ギリシャ文字 */
    private CharsetDef zenkakuGreekCharset = new LiteralCharsetDef();

    /** JIS第1水準に含まれる全角ロシア文字 */
    private CharsetDef zenkakuRussianCharset = new LiteralCharsetDef();

    /** JIS第1水準に含まれる全角ひらがな */
    private CharsetDef zenkakuHiraganaCharset = new LiteralCharsetDef();

    /** JIS第1水準に含まれる全角カタカナ */
    private CharsetDef strictZenkakuKatakanaCharset = new LiteralCharsetDef();

    /** JIS第1水準に含まれる全角記号 */
    private CharsetDef jisSymbolCharset = new LiteralCharsetDef();

    /** JIS第1水準に含まれる全角罫線 */
    private CharsetDef zenkakuKeisenCharset = new LiteralCharsetDef();

    /** JIS第1水準に含まれる漢字 */
    private CharsetDef level1KanjiCharset = new LiteralCharsetDef();

    /** JIS第2水準漢字 */
    private CharsetDef level2KanjiCharset = new LiteralCharsetDef();

    /** NEC選定IBM拡張 */
    private CharsetDef necExtendedCharset = new LiteralCharsetDef();

    /** NEC特殊文字 */
    private CharsetDef necSymbolCharset = new LiteralCharsetDef();

    /** IBM拡張文字 */
    private CharsetDef ibmExtendedCharset = new LiteralCharsetDef();

    /**
     * 半角数字を返す。
     *
     * @return 半角数字
     */
    public CharsetDef getNumericCharset() {
        return numericCharset;
    }

    /**
     * 半角数字を設定する。
     *
     * @param numericCharset 半角数字
     */
    public void setNumericCharset(CharsetDef numericCharset) {
        this.numericCharset = numericCharset;
    }

    /**
     * 半角英小文字を返す。
     *
     * @return 半角英小文字
     */
    public CharsetDef getLowerAlphabetCharset() {
        return lowerAlphabetCharset;
    }

    /**
     * 半角英小文字を設定する。
     *
     * @param lowerAlphabetCharset 半角英小文字
     */
    public void setLowerAlphabetCharset(CharsetDef lowerAlphabetCharset) {
        this.lowerAlphabetCharset = lowerAlphabetCharset;
    }

    /**
     * 半角英大文字を返す。
     *
     * @return 半角英大文字
     */
    public CharsetDef getUpperAlphabetCharset() {
        return upperAlphabetCharset;
    }

    /**
     * 半角英大文字を設定する。
     *
     * @param upperAlphabetCharset 半角英大文字
     */
    public void setUpperAlphabetCharset(CharsetDef upperAlphabetCharset) {
        this.upperAlphabetCharset = upperAlphabetCharset;
    }

    /**
     * ASCII記号を返す。
     *
     * @return ASCII記号
     */
    public CharsetDef getAsciiSymbolCharset() {
        return asciiSymbolCharset;
    }

    /**
     * ASCII記号を設定する。
     *
     * @param asciiSymbolCharset ASCII記号
     */
    public void setAsciiSymbolCharset(CharsetDef asciiSymbolCharset) {
        this.asciiSymbolCharset = asciiSymbolCharset;
    }

    /**
     * 全角スペースを返す。
     *
     * @return 全角スペース
     */
    public CharsetDef getZenkakuSpaceCharset() {
        return zenkakuSpaceCharset;
    }

    /**
     * 全角スペースを設定する。
     *
     * @param zenkakuSpaceCharset 全角スペース
     */
    public void setZenkakuSpaceCharset(CharsetDef zenkakuSpaceCharset) {
        this.zenkakuSpaceCharset = zenkakuSpaceCharset;
    }

    /**
     * 半角カナを返す。
     *
     * @return 半角カナ
     */
    public CharsetDef getHankakuKanaCharset() {
        return hankakuKanaCharset;
    }

    /**
     * 半角カナを設定する。
     *
     * @param hankakuKanaCharset 半角カナ
     */
    public void setHankakuKanaCharset(CharsetDef hankakuKanaCharset) {
        this.hankakuKanaCharset = hankakuKanaCharset;
    }

    /**
     * JIS第1水準に含まれる全角英字を返す。
     *
     * @return JIS第1水準に含まれる全角英字
     */
    public CharsetDef getZenkakuAlphaCharset() {
        return zenkakuAlphaCharset;
    }

    /**
     * JIS第1水準に含まれる全角英字を設定する。
     *
     * @param zenkakuAlphaCharset JIS第1水準に含まれる全角英字
     */
    public void setZenkakuAlphaCharset(CharsetDef zenkakuAlphaCharset) {
        this.zenkakuAlphaCharset = zenkakuAlphaCharset;
    }

    /**
     * JIS第1水準に含まれる全角数字を返す。
     *
     * @return JIS第1水準に含まれる全角数字
     */
    public CharsetDef getZenkakuNumCharset() {
        return zenkakuNumCharset;
    }

    /**
     * JIS第1水準に含まれる全角数字を設定する。
     *
     * @param zenkakuNumCharset JIS第1水準に含まれる全角数字
     */
    public void setZenkakuNumCharset(CharsetDef zenkakuNumCharset) {
        this.zenkakuNumCharset = zenkakuNumCharset;
    }

    /**
     * JIS第1水準に含まれる全角ギリシャ文字を返す。
     *
     * @return JIS第1水準に含まれる全角ギリシャ文字
     */
    public CharsetDef getZenkakuGreekCharset() {
        return zenkakuGreekCharset;
    }

    /**
     * JIS第1水準に含まれる全角ギリシャ文字を設定する。
     *
     * @param zenkakuGreekCharset JIS第1水準に含まれる全角ギリシャ文字
     */
    public void setZenkakuGreekCharset(CharsetDef zenkakuGreekCharset) {
        this.zenkakuGreekCharset = zenkakuGreekCharset;
    }

    /**
     * JIS第1水準に含まれる全角ロシア文字を返す。
     *
     * @return JIS第1水準に含まれる全角ロシア文字
     */
    public CharsetDef getZenkakuRussianCharset() {
        return zenkakuRussianCharset;
    }

    /**
     * JIS第1水準に含まれる全角ロシア文字を設定する。
     *
     * @param zenkakuRussianCharset JIS第1水準に含まれる全角ロシア文字
     */
    public void setZenkakuRussianCharset(CharsetDef zenkakuRussianCharset) {
        this.zenkakuRussianCharset = zenkakuRussianCharset;
    }

    /**
     * JIS第1水準に含まれる全角ひらがなを返す。
     *
     * @return JIS第1水準に含まれる全角ひらがな
     */
    public CharsetDef getZenkakuHiraganaCharset() {
        return zenkakuHiraganaCharset;
    }

    /**
     * JIS第1水準に含まれる全角ひらがなを設定する。
     *
     * @param zenkakuHiraganaCharset JIS第1水準に含まれる全角ひらがな
     */
    public void setZenkakuHiraganaCharset(CharsetDef zenkakuHiraganaCharset) {
        this.zenkakuHiraganaCharset = zenkakuHiraganaCharset;
    }

    /**
     * JIS第1水準に含まれる全角カタカナを返す。
     *
     * @return JIS第1水準に含まれる全角カタカナ
     */
    public CharsetDef getStrictZenkakuKatakanaCharset() {
        return strictZenkakuKatakanaCharset;
    }

    /**
     * JIS第1水準に含まれる全角カタカナを設定する。
     *
     * @param strictZenkakuKatakanaCharset JIS第1水準に含まれる全角カタカナ
     */
    public void setStrictZenkakuKatakanaCharset(CharsetDef strictZenkakuKatakanaCharset) {
        this.strictZenkakuKatakanaCharset = strictZenkakuKatakanaCharset;
    }

    /**
     * JIS第1水準に含まれる全角記号を返す。
     *
     * @return JIS第1水準に含まれる全角記号
     */
    public CharsetDef getJisSymbolCharset() {
        return jisSymbolCharset;
    }

    /**
     * JIS第1水準に含まれる全角記号を設定する。
     *
     * @param jisSymbolCharset JIS第1水準に含まれる全角記号
     */
    public void setJisSymbolCharset(CharsetDef jisSymbolCharset) {
        this.jisSymbolCharset = jisSymbolCharset;
    }

    /**
     * JIS第1水準に含まれる全角罫線を返す。
     *
     * @return JIS第1水準に含まれる全角罫線
     */
    public CharsetDef getZenkakuKeisenCharset() {
        return zenkakuKeisenCharset;
    }

    /**
     * JIS第1水準に含まれる全角罫線を設定する。
     *
     * @param zenkakuKeisenCharset JIS第1水準に含まれる全角罫線
     */
    public void setZenkakuKeisenCharset(CharsetDef zenkakuKeisenCharset) {
        this.zenkakuKeisenCharset = zenkakuKeisenCharset;
    }

    /**
     * JIS第1水準に含まれる漢字を返す。
     *
     * @return JIS第1水準に含まれる漢字
     */
    public CharsetDef getLevel1KanjiCharset() {
        return level1KanjiCharset;
    }

    /**
     * JIS第1水準に含まれる漢字を設定する。
     *
     * @param level1KanjiCharset JIS第1水準に含まれる漢字
     */
    public void setLevel1KanjiCharset(CharsetDef level1KanjiCharset) {
        this.level1KanjiCharset = level1KanjiCharset;
    }

    /**
     * JIS第2水準漢字を返す。
     *
     * @return JIS第2水準漢字
     */
    public CharsetDef getLevel2KanjiCharset() {
        return level2KanjiCharset;
    }

    /**
     * JIS第2水準漢字を設定する。
     *
     * @param level2KanjiCharset JIS第2水準漢字
     */
    public void setLevel2KanjiCharset(CharsetDef level2KanjiCharset) {
        this.level2KanjiCharset = level2KanjiCharset;
    }

    /**
     * NEC選定IBM拡張を返す。
     *
     * @return NEC選定IBM拡張
     */
    public CharsetDef getNecExtendedCharset() {
        return necExtendedCharset;
    }

    /**
     * NEC選定IBM拡張を設定する。
     *
     * @param necExtendedCharset NEC選定IBM拡張
     */
    public void setNecExtendedCharset(CharsetDef necExtendedCharset) {
        this.necExtendedCharset = necExtendedCharset;
    }

    /**
     * NEC特殊文字を返す。
     *
     * @return NEC特殊文字
     */
    public CharsetDef getNecSymbolCharset() {
        return necSymbolCharset;
    }

    /**
     * NEC特殊文字を設定する。
     *
     * @param necSymbolCharset NEC特殊文字
     */
    public void setNecSymbolCharset(CharsetDef necSymbolCharset) {
        this.necSymbolCharset = necSymbolCharset;
    }

    /**
     * IBM拡張文字を返す。
     *
     * @return IBM拡張文字
     */
    public CharsetDef getIbmExtendedCharset() {
        return ibmExtendedCharset;
    }

    /**
     * IBM拡張文字を設定する。
     *
     * @param ibmExtendedCharset IBM拡張文字
     */
    public void setIbmExtendedCharset(CharsetDef ibmExtendedCharset) {
        this.ibmExtendedCharset = ibmExtendedCharset;
    }
}
