package com.example.batch.common.resident.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.batch.common.resident.configuration.ResidentBatchProperties;

/**
 * 常駐バッチのアプリ基盤部品で使用するMapper。
 *
 */
@Mapper
public interface ResidentBatchMapper {

    /**
     * 要求テーブルの設定を元にSELECTを行い、設定の妥当性を検証する。
     * 
     * @param request 要求テーブルの設定
     * @return 検索結果
     */
    Map<String, Object> selectRequestForValidation(
            @Param("request") ResidentBatchProperties.RequestProperties request);

    /**
     * ステータスを条件にして要求テーブルの主キーを取得する。
     * 
     * @param request 要求テーブルの設定
     * @param status 条件となるステータス
     * @return 主キーのリスト
     */
    List<Long> selectRequestPrimaryKeyByStatus(
            @Param("request") ResidentBatchProperties.RequestProperties request,
            @Param("status") String status);

    /**
     * ステータスを条件にして要求テーブルの主キーを取得する。
     * (文字列型の主キーを扱うためのメソッド)
     * 
     * @param request 要求テーブルの設定
     * @param status 条件となるステータス
     * @return 主キーのリスト
     */
    List<String> selectRequestStringPrimaryKeyByStatus(
            @Param("request") ResidentBatchProperties.RequestProperties request,
            @Param("status") String status);

    /**
     * 主キーを条件にして要求テーブルのステータスを更新する。
     * 
     * @param request 要求テーブルの設定
     * @param status 更新するステータス
     * @param primaryKey 条件となる主キー
     * @return 更新件数
     */
    int updateRequestStatusByPrimaryKey(
            @Param("request") ResidentBatchProperties.RequestProperties request,
            @Param("status") String status,
            @Param("primaryKey") Object primaryKey);

    /**
     * 主キーを条件に状態管理テーブルの起動フラグを取得する。
     * 
     * @param jobId 条件となる主キー(ジョブID)
     * @return 起動フラグ
     */
    boolean selectResidentBatchStateRunningByPrimaryKey(
            @Param("jobId") String jobId);

    /**
     * 主キーを条件に状態管理テーブルの起動フラグを更新する。
     * 
     * @param running 更新する起動フラグ
     * @param jobId 条件となる主キー(ジョブID)
     * @return 更新件数
     */
    int updateResidentBatchStateRunningByPrimaryKey(
            @Param("running") boolean running,
            @Param("jobId") String jobId);
}
