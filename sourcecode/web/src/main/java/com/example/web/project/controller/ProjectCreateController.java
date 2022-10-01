package com.example.web.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.web.common.errorhandling.OnRejectError;
import com.example.web.project.dto.create.OrganizationDto;
import com.example.web.project.dto.create.ProjectDto;
import com.example.web.project.form.ProjectCreateForm;
import com.example.web.project.service.ProjectCreateService;

import jp.fintan.keel.spring.web.token.transaction.TransactionTokenCheck;
import jp.fintan.keel.spring.web.token.transaction.TransactionTokenType;

/**
 * プロジェクト登録機能のController。
 *
 * @author sample
 */
@Controller
@RequestMapping("project/create")
@TransactionTokenCheck("project/create")
public class ProjectCreateController {

    /**
     * プロジェクト登録機能のService
     */
    @Autowired
    private ProjectCreateService service;

    /**
     * 入力画面を表示する。
     *
     * @param form プロジェクト登録機能のForm
     * @return 画面テンプレートのパス
     */
    @GetMapping
    public String index(ProjectCreateForm form) {

        return "project/create/index";
    }

    /**
     * 確認画面を表示する。
     *
     * @param form プロジェクト登録機能のForm
     * @param bindingResult バインド結果
     * @return 画面テンプレートのパス
     */
    @PostMapping("confirm")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @OnRejectError(path = "project/create/index")
    public String confirm(@Validated ProjectCreateForm form, BindingResult bindingResult) {
        return "project/create/confirm";
    }

    /**
     * 登録処理を行う。
     *
     * @param form プロジェクト登録機能のForm
     * @param bindingResult バインド結果
     * @return リダイレクト先のパス(PRGパターン)
     */
    @PostMapping(path = "execute", params = "execute")
    @TransactionTokenCheck
    @OnRejectError(path = "project/create/index")
    public String execute(@Validated ProjectCreateForm form, BindingResult bindingResult) {
        ProjectDto projectDto = form.toProjectDto();
        service.insertProject(projectDto);
        return "redirect:/project/create/complete";
    }

    /**
     * 完了画面を表示する。
     *
     * @return 画面テンプレートのパス
     */
    @GetMapping("complete")
    public String complete() {
        return "project/create/complete";
    }

    /**
     * 入力画面へ戻る。
     *
     * @param form プロジェクト登録機能のForm
     * @return 画面テンプレートのパス
     */
    @PostMapping(path = "execute", params = "back")
    public String back(ProjectCreateForm form) {
        return "project/create/index";
    }

    /**
     * 事業部リストを画面テンプレートへ渡す。
     * 
     * @return 事業部リスト
     */
    @ModelAttribute("topOrganizations")
    public List<OrganizationDto> getTopOrganizationList() {
        return service.selectAllDivision();
    }

    /**
     * 部門リストを画面テンプレートへ渡す。
     * 
     * @return 部門リスト
     */
    @ModelAttribute("subOrganizations")
    public List<OrganizationDto> getSubOrganizationList() {
        return service.selectAllDepartment();
    }
}
