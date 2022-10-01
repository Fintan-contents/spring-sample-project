package com.example.web.common.mvc;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import com.example.web.App;

/**
 * Controllerに対する共通的な処理をまとめたクラス。
 * 
 * @author sample
 *
 */
@ControllerAdvice(basePackageClasses = App.class)
public class CommonControllerAdvice {

    /**
     * Controllerの設定を行う。
     * 
     * @param dataBinder データバインダー
     */
    @InitBinder
    public void init(WebDataBinder dataBinder) {
        // パラメーターが空文字列の場合はnullに変換する
        dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
