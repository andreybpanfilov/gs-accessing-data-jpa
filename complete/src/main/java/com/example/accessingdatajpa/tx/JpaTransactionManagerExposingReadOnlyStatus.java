package com.example.accessingdatajpa.tx;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;

public class JpaTransactionManagerExposingReadOnlyStatus extends JpaTransactionManager {

    @Override
    protected Object doSuspend(Object transaction) {
        TxReadOnlyStatus.begin(null);
        boolean success = false;
        try {
            Object result = super.doSuspend(transaction);
            success = true;
            return result;
        } finally {
            if (!success) {
                TxReadOnlyStatus.complete();
            }
        }
    }

    @Override
    protected void doResume(Object transaction, Object suspendedResources) {
        try {
            super.doResume(transaction, suspendedResources);
        } finally {
            TxReadOnlyStatus.complete();
        }
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        TxReadOnlyStatus.begin(definition.isReadOnly());
        boolean success = false;
        try {
            super.doBegin(transaction, definition);
            success = true;
        } finally {
            if (!success) {
                TxReadOnlyStatus.complete();
            }
        }
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        try {
            super.doCleanupAfterCompletion(transaction);
        } finally {
            TxReadOnlyStatus.complete();
        }
    }

}
