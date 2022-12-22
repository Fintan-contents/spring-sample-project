package com.example.api.client.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 顧客削除用のDBアクセス
 */
@Mapper
public interface ClientDeleteMapper {

    /**
     * 顧客削除を行う。
     * 
     * @param clientId 顧客ID
     * @return 削除件数
     */
    int deleteClientByPrimaryKey(Integer clientId);
}
