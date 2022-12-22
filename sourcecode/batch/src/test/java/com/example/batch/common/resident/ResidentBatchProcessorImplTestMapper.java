package com.example.batch.common.resident;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResidentBatchProcessorImplTestMapper {

    String selectTestRequestsStatusByPrimaryKey(long id);
}
