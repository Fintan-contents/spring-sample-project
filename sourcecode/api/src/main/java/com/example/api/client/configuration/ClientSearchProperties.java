package com.example.api.client.configuration;

/**
 * 顧客検索のProperties
 */
public class ClientSearchProperties {
    /** 検索上限件数 */
    private int searchUpperLimit;

    /**
     * 検索上限件数 を取得する。
     *
     * @return searchUpperLimit
     */
    public int getSearchUpperLimit() {
        return searchUpperLimit;
    }

    /**
     * 検索上限件数 を設定する
     *
     * @param searchUpperLimit 検索上限件数
     */
    public void setSearchUpperLimit(int searchUpperLimit) {
        this.searchUpperLimit = searchUpperLimit;
    }
}
