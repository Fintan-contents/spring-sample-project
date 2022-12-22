package com.example.web.project.form;

import org.springframework.web.multipart.MultipartFile;

import nablarch.core.validation.ee.Domain;
import nablarch.core.validation.ee.Required;

/**
 * プロジェクトアップロード機能のForm。
 *
 * @author sample
 */
public class ProjectUploadForm {

    /**
     * ファイル
     */
    private MultipartFile file;

    /**
     * 一時ファイルID
     */
    @Required
    @Domain("tempFileId")
    private String tempFileId;

    /**
     * ファイルを取得する。
     * 
     * @return ファイル
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     * ファイルを設定する。
     * 
     * @param file ファイル
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    /**
     * 一時ファイルIDを取得する。
     * 
     * @return 一時ファイルID
     */
    public String getTempFileId() {
        return tempFileId;
    }

    /**
     * 一時ファイルIDを設定する。
     * 
     * @param tempFileId 一時ファイルID
     */
    public void setTempFileId(String tempFileId) {
        this.tempFileId = tempFileId;
    }
}
