package com.example.web.common.token.transaction;

import java.util.UUID;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jp.fintan.keel.spring.web.token.transaction.HttpSessionTransactionTokenStore;
import jp.fintan.keel.spring.web.token.transaction.TransactionToken;
import jp.fintan.keel.spring.web.token.transaction.TransactionTokenCheck;
import jp.fintan.keel.spring.web.token.transaction.TransactionTokenInterceptor;

/**
 * 二重送信防止トークンを設定する{@link RequestPostProcessor}。
 *
 */
public class TransactionTokenRequestPostProcessor implements RequestPostProcessor {

    /**
     * HTTPセッションを用いたトランザクショントークンストア
     */
    private final HttpSessionTransactionTokenStore store = new HttpSessionTransactionTokenStore();

    /**
     * トークン名
     */
    private final String tokenName;

    /**
     * コンストラクタ。
     * 
     * @param tokenName トークン名。{@link TransactionTokenCheck#value()}に設定する値を渡すこと
     */
    public TransactionTokenRequestPostProcessor(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
        RequestAttributes originalRequestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);
        try {
            String tokenKey = store.createAndReserveTokenKey(tokenName);
            String tokenValue = UUID.randomUUID().toString();

            TransactionToken token = new TransactionToken(tokenName, tokenKey, tokenValue);
            store.store(token);

            request.setParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER, token.getTokenString());
            return request;
        } finally {
            RequestContextHolder.setRequestAttributes(originalRequestAttributes);
        }
    }

    /**
     * {@link TransactionTokenRequestPostProcessor}を生成する。
     * MockMvcを用いたテストコード内でstatic importして使用されることを想定している。
     * 
     * @param tokenName トークン名
     * @return 生成された{@link TransactionTokenRequestPostProcessor}
     */
    public static TransactionTokenRequestPostProcessor transactionToken(String tokenName) {
        return new TransactionTokenRequestPostProcessor(tokenName);
    }
}
