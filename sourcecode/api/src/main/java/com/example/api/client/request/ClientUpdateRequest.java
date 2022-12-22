package com.example.api.client.request;

import org.springframework.beans.BeanUtils;

import com.example.common.generated.model.Client;

import nablarch.core.validation.ee.Domain;
import nablarch.core.validation.ee.Required;

/**
 * 顧客更新リクエスト
 */
public class ClientUpdateRequest {
    /** 顧客名 */
    @Required
    @Domain("clientName")
    private String clientName;

    /** 業種コード */
    @Required
    @Domain("industryCode")
    private String industryCode;

    /** バージョン番号 */
    @Required
    @Domain("versionNo")
    private Long versionNo;

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
     * バージョン番号 を取得する。
     *
     * @return バージョン番号
     */
    public Long getVersionNo() {
        return versionNo;
    }

    /**
     * バージョン番号 を設定する
     *
     * @param versionNo バージョン番号
     */
    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * Clientモデルに変換する。
     *
     * @param clientId 顧客ID
     * @return Clientモデル
     */
    public Client toClient(Integer clientId) {
        Client model = new Client();
        BeanUtils.copyProperties(this, model);
        model.setClientId(clientId);
        return model;
    }
}
