package com.example.batch.common.resident.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.batch.common.resident.configuration.ResidentBatchProperties;
import com.example.batch.common.resident.mapper.ResidentBatchMapper;

/**
 * 常駐バッチの制御を行う際のトランザクション境界となるクラス。
 *
 */
@Component
@Transactional
public class ResidentBatchService {

    @Autowired
    private ResidentBatchMapper mapper;
    @Autowired
    private ResidentBatchProperties residentBatchProperties;

    /**
     * 要求テーブルが設定通りか確認する。
     * 
     */
    @Transactional(readOnly = true)
    public void validate() {
        ResidentBatchProperties.RequestProperties request = residentBatchProperties.getRequest();
        mapper.selectRequestForValidation(request);
    }

    /**
     * ステータスを条件にして要求テーブルの主キーを取得する。
     * 
     * @param status 条件となるステータス
     * @return 主キーのリスト
     */
    @Transactional(readOnly = true)
    public List<?> selectRequestPrimaryKeyByStatus(String status) {
        ResidentBatchProperties.RequestProperties request = residentBatchProperties.getRequest();
        if (request.isStringPrimaryKey()) {
            return mapper.selectRequestStringPrimaryKeyByStatus(request, status);
        }
        return mapper.selectRequestPrimaryKeyByStatus(request, status);
    }

    /**
     * 主キーを条件にして要求テーブルのステータスを更新する。
     * 
     * @param status 更新するステータス
     * @param primaryKey 条件となる主キー
     */
    public void updateRequestStatusByPrimaryKey(String status, Object primaryKey) {
        ResidentBatchProperties.RequestProperties request = residentBatchProperties.getRequest();
        mapper.updateRequestStatusByPrimaryKey(request, status, primaryKey);
    }

    /**
     * ジョブ管理テーブルが持つステータスを「実行中」へ更新する。
     * 
     * @param jobId ジョブID
     */
    public void initialize(String jobId) {
        mapper.updateResidentBatchStateRunningByPrimaryKey(true, jobId);
    }

    /**
     * 主キーを条件に状態管理テーブルの起動フラグを取得する。
     * 
     * @param jobId 条件となる主キー(ジョブID)
     * @return 起動フラグ
     */
    @Transactional(readOnly = true)
    public boolean isRunning(String jobId) {
        return mapper.selectResidentBatchStateRunningByPrimaryKey(jobId);
    }
}
