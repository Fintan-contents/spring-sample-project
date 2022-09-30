package com.example.api.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import nablarch.common.code.Code;
import nablarch.core.cache.BasicStaticDataCache;
import org.assertj.core.api.AssertProvider;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JsonContentAssert;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * RestControllerのテストクラスの基底クラス。
 *
 */
public abstract class RestControllerTestBase {

    /**
     * テスト用のHTTPクライアント
     */
    @Autowired
    protected TestRestTemplate http;

    @Autowired
    BasicStaticDataCache<Code> codeDefinitionCache;

    /**
     * コードのキャッシュをクリアする。
     * 
     * テスト内でコードマスタを入れ替えた際にキャッシュが使用されると
     * テストデータが読み込まれないため{@code BeforeEach}でキャッシュをクリアしている。
     */
    @BeforeEach
    void refreshCode() {
        codeDefinitionCache.refresh();
    }

    /**
     * クラスパス上のファイルを読み込んで文字列で返す。
     * 主にレスポンスボディの期待値となるJSONファイルを読み込むために使用されることを想定している。
     * 
     * @param path ファイルのパス。テストクラスの完全修飾名に対応するディレクトリからの相対パスとなる
     * @return ファイルの内容。UTF-8でデコードされる
     */
    protected String read(String path) {
        String prefix = getClass().getName().replace('.', '/');
        Resource resource = new ClassPathResource(prefix + "/" + path);
        try (InputStream in = resource.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * AssertJでJSONのアサーションをするためのヘルパーメソッド。
     * 
     * @param json JSON文字列
     * @return {@link JsonContentAssert}のプロバイダー
     */
    protected AssertProvider<JsonContentAssert> forJson(String json) {
        return () -> new JsonContentAssert(getClass(), json);
    }
}