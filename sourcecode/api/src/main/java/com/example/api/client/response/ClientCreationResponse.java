package com.example.api.client.response;

import org.springframework.beans.BeanUtils;

import com.example.common.generated.model.Client;

/**
 * 顧客登録レスポンス
 */
public class ClientCreationResponse {
    /** 顧客ID */
    private Integer clientId;

    /** 顧客名 */
    private String clientName;

    /** 業種コード */
    private String industryCode;

    /** バージョン番号 */
    private Long versionNo;

    /**
     * 顧客ID を取得する。
     *
     * @return 顧客ID
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * 顧客ID を設定する
     *
     * @param clientId 顧客ID
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

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
     * Clientモデルからレスポンスを生成する。
     *
     * @param client モデル
     * @return レスポンス
     */
    public static ClientCreationResponse fromClient(Client client) {
        ClientCreationResponse response = new ClientCreationResponse();
        BeanUtils.copyProperties(client, response);
        return response;
    }
}
