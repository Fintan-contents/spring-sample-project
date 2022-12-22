package com.example.api.client.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.common.generated.model.Client;

/**
 * 顧客更新用のDBアクセス
 */
@Mapper
public interface ClientUpdateMapper {

    /**
     * 主キーで検索を行う。
     * 
     * @param clientId 顧客ID
     * @return 顧客情報
     */
    Client selectClientByPrimaryKey(int clientId);

    /**
     * 顧客更新を行う。
     * 
     * @param client 顧客情報
     * @return 更新件数
     */
    int updateClientByPrimaryKey(Client client);

    /**
     * 顧客検索件数を取得する。
     * 引数の顧客名(完全一致)で検索した件数を返却する。
     *
     * @param clientName 顧客名
     * @return 顧客件数
     */
    long countByClientName(String clientName);
}
