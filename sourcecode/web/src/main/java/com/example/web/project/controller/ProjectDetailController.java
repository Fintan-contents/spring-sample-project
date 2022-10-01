package com.example.web.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.common.exception.DataNotFoundException;
import com.example.web.project.dto.detail.ProjectDto;
import com.example.web.project.form.ProjectDetailForm;
import com.example.web.project.service.ProjectDetailService;

/**
 * プロジェクト詳細機能のController。
 *
 * @author sample
 */
@Controller
@RequestMapping("project/detail")
public class ProjectDetailController {

    /**
     * プロジェクト詳細機能のService
     */
    @Autowired
    private ProjectDetailService service;

    /**
     * 詳細画面を表示する。
     *
     * @param form プロジェクト詳細機能のForm
     * @param bindingResult バインド結果
     * @param model 画面テンプレートへ渡す値を保持するクラス
     * @return 画面テンプレートのパス
     */
    @GetMapping
    public String index(ProjectDetailForm form, BindingResult bindingResult, Model model) {
        if (form.getProjectId() == null) {
            throw new DataNotFoundException();
        }
        ProjectDto projectDto = service.findProjectById(form.getProjectId());
        if (projectDto == null) {
            throw new DataNotFoundException();
        }
        model.addAttribute("projectDto", projectDto);
        return "project/detail/index";
    }
}
