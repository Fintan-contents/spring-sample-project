package com.example.web.common.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import nablarch.common.code.CodeManager;

/**
 * コード値に関するViewHelper。
 * 
 * @author sample
 *
 */
@Component
@Transactional(readOnly = true)
public class CodeViewHelper {

    @Autowired
    private CodeManager codeManager;

    /**
     * {@link CodeManager#getName(String, String)}を呼び出す。
     * 
     * @param codeId コードID
     * @param value コード値
     * @return 対応するコード名称
     * @throws IllegalArgumentException 指定したコードIDが存在しないか、
     *                                   対象のコード値または言語に対応するデータが存在しない場合
     */
    public String getName(String codeId, String value) throws IllegalArgumentException {
        return StringUtils.hasText(value) ? codeManager.getName(codeId, value) : null;
    }

    /**
     * {@link CodeManager#getValues(String, String)}を呼び出す。
     * 
     * @param codeId コードID
     * @param pattern 使用するパターンのカラム名（大文字・小文字を区別せずに使用する）
     * @return コードIDとパターンに紐付くコード値
     * @throws IllegalArgumentException 指定したコードIDが存在しないか、
     *                                   パターンまたは言語に対応するデータが存在しない場合
     */
    public List<String> getValues(String codeId, String pattern) throws IllegalArgumentException {
        return codeManager.getValues(codeId, pattern);
    }
}
