package com.example.web.project.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.common.exception.ApplicationException;
import com.example.web.common.errorhandling.OnRejectError;
import com.example.web.common.upload.UploadSupport;
import com.example.web.project.configuration.ProjectUploadProperties;
import com.example.web.project.form.ProjectUploadForm;

import jp.fintan.keel.spring.web.token.transaction.TransactionTokenCheck;
import jp.fintan.keel.spring.web.token.transaction.TransactionTokenType;

/**
 * プロジェクトアップロード機能のController。
 *
 * @author sample
 */
@Controller
@RequestMapping("project/upload")
@TransactionTokenCheck("project/upload")
public class ProjectUploadController {

    @Autowired
    private ProjectUploadProperties properties;
    @Autowired
    private UploadSupport uploadSupport;

    /**
     * 入力画面を表示する。
     *
     * @param form プロジェクトアップロード機能のForm
     * @return 画面テンプレートのパス
     */
    @GetMapping
    public String index(ProjectUploadForm form) {
        return "project/upload/index";
    }

    /**
     * 確認画面を表示する。
     *
     * @param form プロジェクトアップロード機能のForm
     * @param bindingResult バインド結果
     * @return 画面テンプレートのパス
     * @throws IOException 
     */
    @PostMapping("confirm")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @OnRejectError(path = "project/upload/index")
    public String confirm(ProjectUploadForm form, BindingResult bindingResult) throws IOException {
        MultipartFile file = form.getFile();
        if (file == null || StringUtils.isBlank(file.getOriginalFilename())) {
            throw ApplicationException.fieldError("file", "errors.projectUpload.noFile");
        } else if (file.isEmpty()) {
            throw ApplicationException.fieldError("file", "errors.projectUpload.emptyFile");
        } else if (file.getSize() > 512_000) {
            throw ApplicationException.fieldError("file", "errors.projectUpload.fileExceededMaxSize");
        }

        String tempFileId = uploadSupport.saveTemporary(file);
        form.setTempFileId(tempFileId);

        return "project/upload/confirm";
    }

    /**
     * 登録処理を行う。
     *
     * @param form プロジェクトアップロード機能のForm
     * @param bindingResult バインド結果
     * @return リダイレクト先のパス(PRGパターン)
     */
    @PostMapping(path = "execute", params = "execute")
    @TransactionTokenCheck
    @OnRejectError(path = "project/upload/index")
    public String execute(@Validated ProjectUploadForm form, BindingResult bindingResult) {
        if (!uploadSupport.existsTemporaryFile(form.getTempFileId())) {
            throw ApplicationException.globalError("errors.projectUpload.tempFileNotFound");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuuMMddHHmmss");
        String fileName = "uploaded-projects_"
                + dateTimeFormatter.format(LocalDateTime.now())
                + RandomStringUtils.randomAlphabetic(4)
                + ".csv";
        Path dest = Path.of(properties.getDir()).resolve(fileName);
        uploadSupport.moveFromTemporaryFile(form.getTempFileId(), dest);
        return "redirect:/project/upload/complete";
    }

    /**
     * 完了画面を表示する。
     *
     * @return 画面テンプレートのパス
     */
    @GetMapping("complete")
    public String complete() {
        return "project/upload/complete";
    }

    /**
     * 入力画面へ戻る。
     *
     * @param form プロジェクトアップロード機能のForm
     * @return 画面テンプレートのパス
     */
    @PostMapping(path = "execute", params = "back")
    public String back(ProjectUploadForm form) {
        if (form.getTempFileId() != null && uploadSupport.existsTemporaryFile(form.getTempFileId())) {
            uploadSupport.removeTemporaryFile(form.getTempFileId());
        }
        return "project/upload/index";
    }
}
