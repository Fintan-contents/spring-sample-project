package com.example.web.common.errorhandling;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * セッションタイムアウト時にCSRFトークンエラーが発生した場合、ログイン画面へ遷移させるためのErrorViewResolver。
 * 
 * @author sample
 *
 */
@Component
public class ErrorViewResolverImpl implements ErrorViewResolver, Ordered {

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        // 403エラーが発生した際にセッションが新規作成されていたらセッションタイムアウトしたものとみなす
        if (status == HttpStatus.FORBIDDEN) {
            HttpSession session = request.getSession(false);
            if (session != null && session.isNew()) {
                return new ModelAndView("redirect:/login");
            }
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
