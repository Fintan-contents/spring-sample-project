package com.example.api.client.request;

import org.springframework.beans.BeanUtils;

import com.example.common.generated.model.Client;

import nablarch.core.validation.ee.Domain;
import nablarch.core.validation.ee.Required;

/**
 * 顧客登録リクエスト
 */
public class ClientCreationRequest {
    /** 顧客名 */
    @Required
    @Domain("clientName")
    private String clientName;

    /** 業種コード */
    @Required
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
     * Clientモデルに変換する。
     * 
     * @return Clientモデル
     */
    public Client toClient() {
        Client model = new Client();
        BeanUtils.copyProperties(this, model);
        return model;
    }
}
