package com.example.common.nablarch.db;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import nablarch.core.db.connection.ConnectionFactory;

@SpringBootTest
public class UnmanagedSimpleDbTransactionManagerTest {

    @Autowired
    UnmanagedSimpleDbTransactionManager sut;
    @Autowired
    PlatformTransactionManager transactionManager;

    @Test
    void commitTransaction() throws Exception {
        TestTransactionSynchronization tts = new TestTransactionSynchronization();
        sut.beginTransaction();
        TransactionSynchronizationManager.registerSynchronization(tts);
        sut.commitTransaction();
        sut.endTransaction();
        assertEquals(TransactionSynchronization.STATUS_COMMITTED, tts.status);
    }

    @Test
    void commitTransactionNoCallPlatformTransactionManager() {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        UnmanagedSimpleDbTransactionManager sut = new UnmanagedSimpleDbTransactionManager(connectionFactory, transactionManager);
        sut.commitTransaction();
        sut.endTransaction();
        verifyNoInteractions(transactionManager);
    }

    @Test
    void rollbackTransaction() throws Exception {
        TestTransactionSynchronization tts = new TestTransactionSynchronization();
        sut.beginTransaction();
        TransactionSynchronizationManager.registerSynchronization(tts);
        sut.rollbackTransaction();
        sut.endTransaction();
        assertEquals(TransactionSynchronization.STATUS_ROLLED_BACK, tts.status);
    }

    @Test
    void rollbackTransactionNoCallPlatformTransactionManager() {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        UnmanagedSimpleDbTransactionManager sut = new UnmanagedSimpleDbTransactionManager(connectionFactory, transactionManager);
        sut.rollbackTransaction();
        sut.endTransaction();
        verifyNoInteractions(transactionManager);
    }

    @Test
    void commitTransactionWithNestedTransaction() throws Exception {
        TransactionStatus status = transactionManager.getTransaction(null);
        TestTransactionSynchronization tts = new TestTransactionSynchronization();
        sut.beginTransaction();
        TransactionSynchronizationManager.registerSynchronization(tts);
        sut.commitTransaction();
        sut.endTransaction();
        assertNull(tts.status);
        transactionManager.commit(status);
        assertEquals(TransactionSynchronization.STATUS_COMMITTED, tts.status);
    }

    @Test
    void rollbackTransactionWithNestedTransaction() throws Exception {
        TransactionStatus status = transactionManager.getTransaction(null);
        TestTransactionSynchronization tts = new TestTransactionSynchronization();
        sut.beginTransaction();
        TransactionSynchronizationManager.registerSynchronization(tts);
        sut.rollbackTransaction();
        sut.endTransaction();
        assertNull(tts.status);
        transactionManager.rollback(status);
        assertEquals(TransactionSynchronization.STATUS_ROLLED_BACK, tts.status);
    }

    static class TestTransactionSynchronization implements TransactionSynchronization {

        Integer status;

        @Override
        public void afterCompletion(int status) {
            this.status = status;
        }
    }
}
