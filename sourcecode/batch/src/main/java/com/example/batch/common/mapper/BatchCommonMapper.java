package com.example.batch.common.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * バッチで共通利用するマッパー。
 */
@Mapper
public interface BatchCommonMapper {

    /**
     * 指定したテーブルをTRUNCATEする。
     * @param tableName TRUNCATEするテーブルの名前
     */
    void truncateTable(String tableName);
}
