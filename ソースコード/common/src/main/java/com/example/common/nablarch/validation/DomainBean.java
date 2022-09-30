package com.example.common.nablarch.validation;

import nablarch.common.code.validator.ee.CodeValue;
import nablarch.core.validation.ee.Digits;
import nablarch.core.validation.ee.Length;
import nablarch.core.validation.ee.NumberRange;
import nablarch.core.validation.ee.SystemChar;

/**
 * 本システムで使用するドメイン。
 *
 * @author sample
 *
 */
public class DomainBean {

    /** ID **/
    @Digits(integer = 9, fraction = 0)
    public String id;

    /** 組織ID **/
    @Digits(integer = 4, fraction = 0)
    public String organizationId;

    /** プロジェクトID **/
    @Digits(integer = 20, fraction = 0)
    public String projectId;

    /** プロジェクト名 **/
    @Length(max = 128, message = "{domainType.projectName.message}")
    @SystemChar(charsetDef = "全角文字", message = "{domainType.projectName.message}")
    public String projectName;

    /** プロジェクト種別 **/
    @CodeValue(codeId = "C0300001", pattern = "pattern01")
    public String projectType;

    /** プロジェクト分類 **/
    @CodeValue(codeId = "C0200001", pattern = "pattern01")
    public String projectClass;

    /** ユーザ名 **/
    @Length(max = 128, message = "{domainType.userName.message}")
    @SystemChar(charsetDef = "全角文字", message = "{domainType.userName.message}")
    public String userName;

    /** 備考 **/
    @Length(max = 512, message = "{domainType.note.message}")
    @SystemChar(charsetDef = "システム許容文字", allowLineSeparator = true, message = "{domainType.note.message}")
    public String note;

    /** 金額 **/
    @NumberRange(min = 0, max = 999999999, message = "{com.nablarch.example.app.entity.core.validation.validator.MoneyRange.message}")
    public String amountOfMoney;

    /** 顧客ID **/
    @Digits(integer = 10, fraction = 0)
    public String clientId;

    /** 顧客名 **/
    @Length(max = 128)
    @SystemChar(charsetDef = "全角文字")
    public String clientName;

    /** 業種コード **/
    @CodeValue(codeId = "C0100001")
    public String industryCode;

    /** バージョン番号 **/
    @NumberRange(max = 9999)
    public String versionNo;

    /** ページ番号 **/
    @NumberRange(min = 0, max = 9999)
    public String pageNumber;
}