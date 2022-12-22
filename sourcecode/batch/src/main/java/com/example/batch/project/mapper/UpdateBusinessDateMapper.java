package com.example.batch.project.mapper;

import com.example.common.generated.model.BusinessDate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 業務日付更新バッチのMapper。
 */
@Mapper
public interface UpdateBusinessDateMapper {

    /**
     * 区分を指定して更新対象の業務日付を検索する。
     * @param segmentId 区分
     * @return 検索結果
     */
    BusinessDate selectBusinessDateByPrimaryKeyForUpdate(String segmentId);

    /**
     * 業務日付を更新する。
     * @param businessDate 業務日付
     * @return 更新件数
     */
    int updateBusinessDateByPrimaryKey(BusinessDate businessDate);
}
