package com.example.api.client.request;

import org.springframework.beans.BeanUtils;

import com.example.api.client.model.search.ClientSearchCriteria;

import nablarch.core.validation.ee.Domain;

/**
 * 顧客検索リクエスト
 */
public class ClientSearchRequest {
    /** 顧客名 */
    @Domain("clientName")
    private String clientName;

    /** 業種コード */
    @Domain("industryCode")
    private String industryCode;

    /**
     * 顧客名 を取得する。
     *
     * @return 顧客名
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * 顧客名 を設定する
     *
     * @param clientName 顧客名
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * 業種コード を取得する。
     *
     * @return 業種コード
     */
    public String getIndustryCode() {
        return industryCode;
    }

    /**
     * 業種コード を設定する
     *
     * @param industryCode 業種コード
     */
    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    /**
     * 顧客検索条件に変換する。
     *
     * @return 顧客検索条件
     */
    public ClientSearchCriteria toClientSearchCriteria() {
        ClientSearchCriteria criteria = new ClientSearchCriteria();
        BeanUtils.copyProperties(this, criteria);
        return criteria;
    }
}
