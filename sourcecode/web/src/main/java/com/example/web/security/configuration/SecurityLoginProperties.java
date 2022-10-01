package com.example.web.security.configuration;

/**
 * ログインのProperties。
 * 
 * @author sample
 *
 */
public class SecurityLoginProperties {

    /**
     * アカウントロックをかける基準となる認証失敗回数（この回数を超えるとアカウントロック扱いになる）。
     */
    private int failedCountToLock;

    /**
     * アカウントロックをかける基準となる認証失敗回数を返す。
     * 
     * @return アカウントロックをかける基準となる認証失敗回数
     */
    public int getFailedCountToLock() {
        return failedCountToLock;
    }

    /**
     * アカウントロックをかける基準となる認証失敗回数を設定する。
     * 
     * @param failedCountToLock アカウントロックをかける基準となる認証失敗回数
     */
    public void setFailedCountToLock(int failedCountToLock) {
        this.failedCountToLock = failedCountToLock;
    }
}
