package com.example.api.client.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.common.generated.model.Client;

/**
 * 顧客詳細用のDBアクセス
 */
@Mapper
public interface ClientDetailMapper {

    /**
     * 主キーで検索を行う。
     * 
     * @param clientId 顧客ID
     * @return 顧客情報
     */
    Client selectClientByPrimaryKey(int clientId);
}
