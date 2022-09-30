package com.example.api.client.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.api.client.model.search.ClientSearchCriteria;
import com.example.common.generated.model.Client;

/**
 * 顧客検索用のDBアクセス
 */
@Mapper
public interface ClientSearchMapper {

    /**
     * 顧客検索を行う。
     * 引数の顧客名(前方一致)、業種コード(完全一致)で検索した結果を返却する。
     * 顧客名・業種コードが設定されていない場合は検索条件に含めない。
     * 
     * @param criteria 検索条件
     * @return 検索結果顧客リスト
     */
    List<Client> selectClientByCriteria(ClientSearchCriteria criteria);

    /**
     * 顧客検索件数を取得する。
     * 引数の顧客名(前方一致)、業種コード(完全一致)で検索した件数を返却する。
     * 顧客名・業種コードが設定されていない場合は検索条件に含めない。
     * 
     * @param criteria 検索条件
     * @return 検索結果顧客件数
     */
    long countClientByCriteria(ClientSearchCriteria criteria);
}
