package com.example.common.nablarch.validation;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import nablarch.core.repository.SystemRepository;
import nablarch.core.validation.validator.unicode.CompositeCharsetDef;
import nablarch.core.validation.validator.unicode.RangedCharsetDef;

/**
 * 文字種バリデーション機能に必要なインスタンスを{@link SystemRepository}へ登録するクラス。
 * {@link InitializingBean}を使用してSpringの初期化時に
 * {@link SystemRepository}への登録を行っている。
 * 
 * @author sample
 *
 */
public class CharsetDefSystemRepositoryLoader implements InitializingBean {

    /**
     * Nablarchデフォルト設定に含まれる文字種の定義を読み込むためのProperties
     */
    private final CharsetDefProperties properties;

    /**
     * コンストラクタ。
     * 
     * @param properties Nablarchデフォルト設定に含まれる文字種の定義を読み込むためのProperties
     */
    public CharsetDefSystemRepositoryLoader(CharsetDefProperties properties) {
        this.properties = properties;
    }

    @Override
    public void afterPropertiesSet() {
        // 半角スペース
        RangedCharsetDef halfWidthSpace = new RangedCharsetDef();
        halfWidthSpace.setStartCodePoint("U+0020");
        halfWidthSpace.setEndCodePoint("U+0020");

        // 半角英数字
        CompositeCharsetDef alphabetsAndNumbers = new CompositeCharsetDef();
        alphabetsAndNumbers.setCharsetDefList(List.of(
                properties.getNumericCharset(),
                properties.getLowerAlphabetCharset(),
                properties.getUpperAlphabetCharset()));

        // ASCII文字
        CompositeCharsetDef asciiCharset = new CompositeCharsetDef();
        asciiCharset.setCharsetDefList(List.of(
                alphabetsAndNumbers,
                properties.getAsciiSymbolCharset()));

        // 全角文字
        CompositeCharsetDef zenkakuCharset = new CompositeCharsetDef();
        zenkakuCharset.setCharsetDefList(List.of(
                properties.getZenkakuHiraganaCharset(),
                properties.getStrictZenkakuKatakanaCharset(),
                properties.getZenkakuAlphaCharset(),
                properties.getZenkakuNumCharset(),
                properties.getZenkakuGreekCharset(),
                properties.getZenkakuRussianCharset(),
                properties.getJisSymbolCharset(),
                properties.getZenkakuKeisenCharset(),
                properties.getLevel1KanjiCharset(),
                properties.getLevel2KanjiCharset(),
                properties.getZenkakuSpaceCharset()));

        // システム許容文字
        CompositeCharsetDef systemAllowedCharset = new CompositeCharsetDef();
        systemAllowedCharset.setCharsetDefList(List.of(
                asciiCharset,
                zenkakuCharset,
                properties.getHankakuKanaCharset(),
                halfWidthSpace));

        SystemRepository.load(() -> Map.of(
                "半角数字", properties.getNumericCharset(),
                "半角英数", alphabetsAndNumbers,
                "ASCII文字", asciiCharset,
                "全角文字", zenkakuCharset,
                "システム許容文字", systemAllowedCharset));
    }
}