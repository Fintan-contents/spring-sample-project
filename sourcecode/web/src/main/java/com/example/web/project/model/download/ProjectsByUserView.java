package com.example.web.project.model.download;

import java.time.LocalDateTime;

import com.example.web.project.configuration.ProjectDownloadProperties;

/**
 * ユーザ別従事プロジェクト情報の検索結果を格納するModel。
 *
 * @author sample
 */
public class ProjectsByUserView {

    /**
     * ユーザID
     */
    private Integer userId;

    /**
     * 要求日時
     */
    private LocalDateTime requestDatetime;

    /**
     * ステータス
     */
    private String status;

    /**
     * ファイル名
     */
    private String fileName;

    /**
     * 作成日時
     */
    private LocalDateTime createDatetime;

    /**
     * 有効期限
     */
    private LocalDateTime expireDatetime;

    /**
     * ファイルが未作成または有効期限切れかどうかチェックする。
     * 
     * @return ファイルが未作成または有効期限切れならtrueを返す
     */
    public boolean isUncreatedOrExpired() {
        return getFileName() == null || LocalDateTime.now().isAfter(getExpireDatetime());
    }

    /**
     * ファイル作成要求が可能かどうかチェックする。
     * 
     * @return ファイル作成要求が可能ならtrueを返す
     */
    public boolean isNotBeAbleToCreateFile() {
        return ProjectDownloadProperties.STATUS_UNPROCESSED.equals(getStatus())
                || ProjectDownloadProperties.STATUS_PROCESSING.equals(getStatus());
    }

    /**
     * ユーザIDを取得する。
     * 
     * @return ユーザID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * ユーザIDを設定する。
     * 
     * @param userId ユーザID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 要求日時を取得する。
     * 
     * @return 要求日時
     */
    public LocalDateTime getRequestDatetime() {
        return requestDatetime;
    }

    /**
     * 要求日時を設定する。
     * 
     * @param requestDatetime 要求日時
     */
    public void setRequestDatetime(LocalDateTime requestDatetime) {
        this.requestDatetime = requestDatetime;
    }

    /**
     * ステータスを取得する。
     * 
     * @return ステータス
     */
    public String getStatus() {
        return status;
    }

    /**
     * ステータスを設定する。
     * 
     * @param status ステータス
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * ファイル名を取得する。
     * 
     * @return ファイル名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * ファイル名を設定する。
     * 
     * @param fileName ファイル名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 作成日時を取得する。
     * 
     * @return 作成日時
     */
    public LocalDateTime getCreateDatetime() {
        return createDatetime;
    }

    /**
     * 作成日時を設定する。
     * 
     * @param createDatetime 作成日時
     */
    public void setCreateDatetime(LocalDateTime createDatetime) {
        this.createDatetime = createDatetime;
    }

    /**
     * 有効期限を取得する。
     * 
     * @return 有効期限
     */
    public LocalDateTime getExpireDatetime() {
        return expireDatetime;
    }

    /**
     * 有効期限を設定する。
     * 
     * @param expireDatetime 有効期限
     */
    public void setExpireDatetime(LocalDateTime expireDatetime) {
        this.expireDatetime = expireDatetime;
    }
}
