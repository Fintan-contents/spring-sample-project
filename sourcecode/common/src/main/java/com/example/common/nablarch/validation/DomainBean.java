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
    /** 組織ID **/
    @Digits(integer = 4, fraction = 0)
    public String organizationId;

    /** ログインID **/
    @Length(max = 20)
    @SystemChar(charsetDef = "半角数字")
    public String loginId;

    /** アカウントパスワード **/
    @Length(max = 64)
    @SystemChar(charsetDef = "ASCII文字")
    public String userPassword;

    /** プロジェクトID **/
    @Digits(integer = 9, fraction = 0)
    public String projectId;

    /** プロジェクト名 **/
    @Length(max = 128, message = "{domainType.projectName.message}")
    @SystemChar(charsetDef = "全角文字", message = "{domainType.projectName.message}")
    public String projectName;

    /** プロジェクト種別 **/
    @CodeValue(codeId = "C0300001", pattern = "PATTERN01")
    public String projectType;

    /** プロジェクト分類 **/
    @CodeValue(codeId = "C0200001", pattern = "PATTERN01")
    public String projectClass;

    /** 顧客ID **/
    @Digits(integer = 9, fraction = 0)
    public String clientId;

    /** ユーザ氏名（漢字） **/
    @Length(max = 128, message = "{domainType.userName.message}")
    @SystemChar(charsetDef = "全角文字", message = "{domainType.userName.message}")
    public String userName;

    /** 備考 **/
    @Length(max = 512, message = "{domainType.note.message}")
    @SystemChar(charsetDef = "システム許容文字", allowLineSeparator = true, message = "{domainType.note.message}")
    public String note;

    /** 金額 **/
    @MoneyRange(min = 0, max = 999999999)
    public Integer amountOfMoney;

    /** ページ番号 **/
    @NumberRange(min = 0, max = 9999)
    public String pageNumber;

    /** ユーザID **/
    @NumberRange(max = 999999999)
    public String userId;

    /** 組織名 **/
    @Length(max = 128)
    @SystemChar(charsetDef = "全角文字")
    public String organizationName;

    /** ユーザ氏名（ふりがな） **/
    @Length(max = 128)
    @SystemChar(charsetDef = "全角文字")
    public String kanaNme;

    /** バージョン番号 **/
    @NumberRange(max = 9999)
    public String versionNo;

    /** 認証失敗回数 **/
    @SystemChar(charsetDef = "半角数字")
    public String failedCount;

    /** フラグ **/
    @Length(min = 1, max = 1)
    @SystemChar(charsetDef = "半角数字")
    public String flag;

    /** コードID **/
    @Length(max = 8)
    @SystemChar(charsetDef = "半角英数字")
    public String codeId;

    /** コード値 **/
    @Length(max = 2)
    @SystemChar(charsetDef = "半角英数字")
    public String codeValue;

    /** コード名称 **/
    @Length(max = 50)
    @SystemChar(charsetDef = "システム許容文字")
    public String codeName;

    /** オプション名称 **/
    @Length(max = 40)
    @SystemChar(charsetDef = "システム許容文字")
    public String option;

    /** コードパターン **/
    @Length(min = 1, max = 1)
    @SystemChar(charsetDef = "半角英数字")
    public String pattern;

    /** ソート順 **/
    @Length(min = 1, max = 1)
    @SystemChar(charsetDef = "半角数字")
    public String sortOrder;

    /** 言語 **/
    @Length(max = 2)
    @SystemChar(charsetDef = "半角英字")
    public String lang;

    /** コード略称 **/
    @Length(max = 50)
    @SystemChar(charsetDef = "システム許容文字")
    public String shortName;

    /** 業務日付区分 **/
    @Length(min = 2, max = 2)
    @SystemChar(charsetDef = "半角英数字")
    public String segmentId;

    /** 業務日付 **/
    @Length(min = 8, max = 8)
    @SystemChar(charsetDef = "半角数字")
    public String bizDate;

    /** 一時ファイルID **/
    @SystemChar(charsetDef = "ASCII文字")
    public String tempFileId;

    /** 顧客名 **/
    @Length(max = 128)
    @SystemChar(charsetDef = "全角文字")
    public String clientName;

    /** 業種コード **/
    @CodeValue(codeId = "C0100001")
    public String industryCode;
}
