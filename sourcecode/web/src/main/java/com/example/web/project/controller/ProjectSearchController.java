package com.example.web.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.web.common.errorhandling.OnRejectError;
import com.example.web.project.configuration.ProjectSearchProperties;
import com.example.web.project.dto.search.OrganizationDto;
import com.example.web.project.dto.search.ProjectDto;
import com.example.web.project.form.ProjectSearchForm;
import com.example.web.project.model.search.ProjectSearchCriteria;
import com.example.web.project.service.ProjectSearchService;

/**
 * プロジェクト検索機能のController。
 *
 * @author sample
 */
@Controller
@RequestMapping("project/search")
@SessionAttributes(types = ProjectSearchForm.class)
public class ProjectSearchController {

    /**
     * プロジェクト検索機能のService
     */
    @Autowired
    private ProjectSearchService service;

    /**
     * プロジェクト検索機能のProperties
     */
    @Autowired
    private ProjectSearchProperties properties;

    /**
     * 検索画面を表示する。
     *
     * @param form プロジェクト検索機能のForm
     * @return 画面テンプレートのパス
     */
    @GetMapping
    public String index(ProjectSearchForm form) {
        form.clear();
        return "project/search/index";
    }

    /**
     * 検索処理を行う。
     *
     * @param form プロジェクト検索機能のForm
     * @param bindingResult バインド結果
     * @param model 画面テンプレートへ渡す値を保持するクラス
     * @return 遷移先
     */
    @GetMapping("search")
    @OnRejectError(path = "project/search/index")
    public String search(@Validated ProjectSearchForm form, BindingResult bindingResult, Model model) {
        ProjectSearchCriteria criteria = form.toProjectSearchCriteria(properties.getRecordsPerPage());
        Page<ProjectDto> searchResult = service.selectProjectes(criteria);
        model.addAttribute("searchResult", searchResult);
        return "project/search/index";
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
