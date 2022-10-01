package com.example.common.nablarch.db;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import nablarch.core.db.connection.AppDbConnection;
import nablarch.core.db.connection.ConnectionFactory;
import nablarch.core.db.connection.DbConnectionContext;
import nablarch.core.db.transaction.SimpleDbTransactionManager;
import nablarch.core.transaction.TransactionContext;

/**
 * Springのトランザクション管理機能と統合するSimpleDbTransactionManagerサブクラス。
 * 
 * 
 * @author sample
 *
 */
public class UnmanagedSimpleDbTransactionManager extends SimpleDbTransactionManager {

    /**
     * トランザクション名
     */
    private static final String DB_TRANSACTION_NAME = TransactionContext.DEFAULT_TRANSACTION_CONTEXT_KEY;

    /**
     * ConnectionFactory
     */
    private final ConnectionFactory connectionFactory;

    /**
     * Springのトランザクションマネージャ
     */
    private final PlatformTransactionManager transactionManager;

    /**
     * トランザクションの状態を保持するスレッドローカル
     */
    private final ThreadLocal<TransactionStatus> transactionStatusHolder = new ThreadLocal<>();

    /**
     * コンストラクタ。
     * 
     * @param connectionFactory ConnectionFactory
     * @param transactionManager Springのトランザクションマネージャ
     */
    public UnmanagedSimpleDbTransactionManager(ConnectionFactory connectionFactory,
            PlatformTransactionManager transactionManager) {
        this.connectionFactory = connectionFactory;
        this.transactionManager = transactionManager;
    }

    @Override
    public void beginTransaction() {
        // トランザクションを開始、または既存トランザクションへ参加し、状態をスレッドローカルへ保存する
        TransactionStatus status = transactionManager.getTransaction(null);
        transactionStatusHolder.set(status);

        // Nablarchで使用するためAppDbConnectionを作成してDbConnectionContextへセット
        AppDbConnection dbConnection = connectionFactory.getConnection(DB_TRANSACTION_NAME);
        DbConnectionContext.setConnection(DB_TRANSACTION_NAME, dbConnection);
    }

    @Override
    public void commitTransaction() {
        // トランザクションを開始した場合はコミットを行う。
        // 既存トランザクションへ参加した場合は何も行わない。
        TransactionStatus status = transactionStatusHolder.get();
        if (status != null && status.isNewTransaction()) {
            transactionManager.commit(status);
        }
    }

    @Override
    public void rollbackTransaction() {
        // トランザクションを開始した場合はロールバックを行う。
        // 既存トランザクションへ参加した場合は何も行わない。
        TransactionStatus status = transactionStatusHolder.get();
        if (status != null && status.isNewTransaction()) {
            transactionManager.rollback(status);
        }
    }

    @Override
    public void endTransaction() {
        transactionStatusHolder.remove();
        DbConnectionContext.removeConnection(DB_TRANSACTION_NAME);
    }
}