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

import com.example.common.exception.DataNotFoundException;
import com.example.web.common.errorhandling.OnRejectError;
import com.example.web.project.dto.update.OrganizationDto;
import com.example.web.project.dto.update.ProjectDto;
import com.example.web.project.form.ProjectUpdateForm;
import com.example.web.project.service.ProjectUpdateService;

import jp.fintan.keel.spring.web.token.transaction.TransactionTokenCheck;
import jp.fintan.keel.spring.web.token.transaction.TransactionTokenType;

/**
 * プロジェクト更新機能のController。
 *
 * @author sample
 */
@Controller
@RequestMapping("project/update")
@TransactionTokenCheck("project/update")
public class ProjectUpdateController {

    /**
     * プロジェクト更新機能のService
     */
    @Autowired
    private ProjectUpdateService service;

    /**
     * 入力画面を表示する。
     *
     * @param form プロジェクト更新機能のForm
     * @param bindingResult バインド結果
     * @return 画面テンプレートのパス
     */
    @GetMapping
    public String index(ProjectUpdateForm form, BindingResult bindingResult) {
        ProjectDto projectDto = service.findProjectById(form.getProjectId());
        if (projectDto == null) {
            throw new DataNotFoundException();
        }
        form.copyFrom(projectDto);
        return "project/update/index";
    }

    /**
     * 確認画面を表示する。
     *
     * @param form プロジェクト更新機能のForm
     * @param bindingResult バインド結果
     * @return 画面テンプレートのパス
     */
    @PostMapping("confirm")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    @OnRejectError(path = "project/update/index")
    public String confirm(@Validated ProjectUpdateForm form, BindingResult bindingResult) {
        return "project/update/confirm";
    }

    /**
     * 更新処理を行う。
     *
     * @param form プロジェクト更新機能のForm
     * @param bindingResult バインド結果
     * @return リダイレクト先のパス(PRGパターン)
     */
    @PostMapping(path = "execute", params = "execute")
    @TransactionTokenCheck
    @OnRejectError(path = "project/update/index")
    public String execute(@Validated ProjectUpdateForm form, BindingResult bindingResult) {
        ProjectDto projectDto = form.toProjectDto();
        service.updateProject(projectDto);
        return "redirect:/project/update/complete";
    }

    /**
     * 完了画面を表示する。
     *
     * @return 画面テンプレートのパス
     */
    @GetMapping("complete")
    public String complete() {
        return "project/update/complete";
    }

    /**
     * 入力画面へ戻る。
     *
     * @param form プロジェクト更新機能のForm
     * @return 画面テンプレートのパス
     */
    @PostMapping(path = "execute", params = "back")
    public String back(ProjectUpdateForm form) {
        return "project/update/index";
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
