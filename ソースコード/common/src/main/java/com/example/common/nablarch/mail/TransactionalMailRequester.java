package com.example.common.nablarch.mail;

import nablarch.common.mail.AttachedFileSizeOverException;
import nablarch.common.mail.MailRequester;
import nablarch.common.mail.RecipientCountException;
import nablarch.common.mail.TemplateMailContext;
import nablarch.core.db.connection.AppDbConnection;
import nablarch.core.db.transaction.SimpleDbTransactionExecutor;
import nablarch.core.db.transaction.SimpleDbTransactionManager;

/**
 * メール送信要求を登録する際、必ずトランザクション内で行うためのMailRequesterサブクラス。
 * 
 * @author sample
 *
 */
public class TransactionalMailRequester extends MailRequester {

    /**
     * SimpleDbTransactionManager
     */
    private final SimpleDbTransactionManager transactionManager;

    /**
     * コンストラクタ。
     * 
     * @param transactionManager SimpleDbTransactionManager
     */
    public TransactionalMailRequester(SimpleDbTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public String requestToSend(TemplateMailContext ctx) throws AttachedFileSizeOverException, RecipientCountException {
        return new SimpleDbTransactionExecutor<String>(transactionManager) {
            @Override
            public String execute(AppDbConnection connection) {
                return TransactionalMailRequester.super.requestToSend(ctx);
            }
        }.doTransaction();
    }
}