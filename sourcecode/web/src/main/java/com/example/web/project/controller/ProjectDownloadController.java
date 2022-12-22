package com.example.web.project.controller;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.common.exception.DataNotFoundException;
import com.example.web.common.errorhandling.OnRejectError;
import com.example.web.common.mvc.CsvFileDownloadView;
import com.example.web.common.mvc.FileDownloadAttributes;
import com.example.web.project.configuration.ProjectDownloadProperties;
import com.example.web.project.form.ProjectDownloadForm;
import com.example.web.project.model.download.ProjectsByUserView;
import com.example.web.project.service.ProjectDownloadService;

import jp.fintan.keel.spring.web.token.transaction.TransactionTokenCheck;
import jp.fintan.keel.spring.web.token.transaction.TransactionTokenType;

/**
 * ユーザ別従事プロジェクトファイルダウンロードのController。
 * 
 * @author sample
 */
@Controller
@RequestMapping("project/download")
@TransactionTokenCheck("project/download")
public class ProjectDownloadController {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuuMMddHHmmss");

    @Autowired
    private ProjectDownloadService service;
    @Autowired
    private ProjectDownloadProperties properties;

    /**
     * 初期画面を表示する。
     * 
     * @param form ユーザ別従事プロジェクトファイルダウンロードのForm
     * @return 画面テンプレートのパス
     */
    @GetMapping
    public String index(ProjectDownloadForm form) {
        return "project/download/index";
    }

    /**
     * 確認画面を表示する。
     * 
     * @param form ユーザ別従事プロジェクトファイルダウンロードのForm
     * @param bindingResult バインド結果
     * @param authentication 認証情報
     * @return 画面テンプレートのパス
     */
    @PostMapping("confirm")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @OnRejectError(path = "project/download/index")
    public String confirm(ProjectDownloadForm form, BindingResult bindingResult, Authentication authentication) {
        String loginId = authentication.getName();
        service.validate(loginId);
        return "project/download/confirm";
    }

    /**
     * 要求処理を行う。
     * 
     * @param form ユーザ別従事プロジェクトファイルダウンロードのForm
     * @param bindingResult バインド結果
     * @param authentication 認証情報
     * @return リダイレクト先のパス(PRGパターン)
     */
    @PostMapping(path = "execute", params = "execute")
    @TransactionTokenCheck
    @OnRejectError(path = "project/download/index")
    public String execute(ProjectDownloadForm form, BindingResult bindingResult, Authentication authentication) {
        String loginId = authentication.getName();
        service.request(loginId);
        return "redirect:/project/download/complete";
    }

    /**
     * 完了画面を表示する。
     * 
     * @return 画面テンプレートのパス
     */
    @GetMapping("complete")
    public String complete() {
        return "project/download/complete";
    }

    /**
     * 初期画面へ戻る。
     * 
     * @param form ユーザ別従事プロジェクトファイルダウンロードのForm
     * @return 画面テンプレートのパス
     */
    @PostMapping(path = "execute", params = "back")
    public String back(ProjectDownloadForm form) {
        return "project/download/index";
    }

    /**
     * ファイルをダウンロードする。
     * 
     * @param form ユーザ別従事プロジェクトファイルダウンロードのForm
     * @param authentication 認証情報
     * @param model 画面テンプレートへ渡す値を保持するクラス
     * @return CSVダウンロードを行うViewのBean名
     */
    @GetMapping("file")
    public String file(ProjectDownloadForm form, Authentication authentication, Model model) {
        String loginId = authentication.getName();
        ProjectsByUserView view = service.selectProjectsByUserByLoginId(loginId);
        if (view.isUncreatedOrExpired()) {
            throw new DataNotFoundException();
        }
        Path dir = Path.of(properties.getDir());
        String targetFilePath = "file:" + dir.resolve(view.getFileName()).toString();
        String downloadFileName = "ユーザ別従事プロジェクト_" + view.getCreateDatetime().format(dateTimeFormatter) + ".csv";
        model
                .addAttribute(CsvFileDownloadView.DOWNLOAD_FILE_INFO_KEY,
                        new FileDownloadAttributes(targetFilePath, downloadFileName));
        return CsvFileDownloadView.VIEW_NAME;
    }

    /**
     * ユーザ別従事プロジェクト情報を画面テンプレートへ渡す。
     * 
     * @param authentication 認証情報
     * @return ユーザ別従事プロジェクト情報
     */
    @ModelAttribute
    public ProjectsByUserView projectsByUserView(Authentication authentication) {
        String loginId = authentication.getName();
        return service.selectProjectsByUserByLoginId(loginId);
    }
}
