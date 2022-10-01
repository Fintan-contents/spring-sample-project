package com.example.common.nablarch.mail;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import nablarch.common.mail.MailRequester;
import nablarch.core.repository.SystemRepository;

/**
 * メール送信機能に必要なインスタンスを{@link SystemRepository}へ登録するクラス。
 * {@link InitializingBean}を使用してSpringの初期化時に
 * {@link SystemRepository}への登録を行っている。
 * 
 * @author sample
 *
 */
public class MailSystemRepositoryLoader implements InitializingBean {

    /**
     * MailRequester
     */
    private final MailRequester mailRequester;

    /**
     * コンストラクタ。
     * 
     * @param mailRequester MailRequester
     */
    public MailSystemRepositoryLoader(MailRequester mailRequester) {
        this.mailRequester = mailRequester;
    }

    @Override
    public void afterPropertiesSet() {
        SystemRepository
                .load(() -> Map
                        .of(
                                "mailRequester", mailRequester));
    }
}
