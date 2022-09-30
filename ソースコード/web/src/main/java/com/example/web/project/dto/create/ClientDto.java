package com.example.web.project.dto.create;

/**
 * 顧客を表すDto。
 * 
 * @author sample
 */
public class ClientDto {

    /** 顧客名 */
    private String clientName;

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
}
