package com.example.web.security.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;

import com.example.common.generated.model.SystemAccount;
import com.example.common.generated.model.Users;

/**
 * ログインのMapper。
 * 
 * @author sample
 *
 */
@Mapper
public interface SecurityLoginMapper {

    /**
     * ログインIDを条件に、システムアカウントを検索する。
     * 
     * @param loginId ログインID
     * @return 検索結果
     */
    SystemAccount selectSystemAccountByLoginId(String loginId);

    /**
     * 主キーを条件に、ユーザを検索する。
     * 
     * @param userId ユーザID
     * @return 検索結果
     */
    Users selectUsersByPrimaryKey(Integer userId);

    /**
     * ログインIDを条件に、システムアカウントのログイン情報を更新する（最終ログイン日時を設定し、認証失敗回数を0にする）。
     * 
     * @param lastLoginDateTime 最終ログイン日時
     * @param loginId 認証失敗回数
     * @return 更新件数
     */
    int updateSystemAccountSetLoginInfoByLoginId(LocalDateTime lastLoginDateTime, String loginId);

    /**
     * ログインIDを条件に、システムアカウントの認証失敗回数を+1する。
     * 
     * @param loginId ログインID
     * @return 更新件数
     */
    int updateSystemAccountIncrementFailedCountByLoginId(String loginId);
}
