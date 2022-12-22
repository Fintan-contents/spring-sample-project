package com.example.batch.project.configuration;

/**
 * 業務日付更新バッチのプロパティを定義したクラス。
 */
public class UpdateBusinessDateProperties {
    /**
     * 更新対象の業務日付の区分。
     */
    private String segmentId;

    /**
     * 更新対象の業務日付の区分を取得する。
     * @return 更新対象の業務日付の区分
     */
    public String getSegmentId() {
        return segmentId;
    }

    /**
     * 更新対象の業務日付の区分を設定する。
     * @param segmentId 更新対象の業務日付の区分
     */
    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }
}
