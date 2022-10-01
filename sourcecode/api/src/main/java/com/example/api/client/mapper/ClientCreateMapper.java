package com.example.api.client.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.common.generated.model.Client;

/**
 * 顧客登録用のDBアクセス
 */
@Mapper
public interface ClientCreateMapper {

    /**
     * 顧客登録を行う。
     * 
     * @param client 顧客情報
     * @return 登録件数
     */
    int insertClient(Client client);

    /**
     * 顧客検索件数を取得する。
     * 引数の顧客名(完全一致)で検索した件数を返却する。
     *
     * @param clientName 顧客名
     * @return 顧客件数
     */
    long countByClientName(String clientName);
}
