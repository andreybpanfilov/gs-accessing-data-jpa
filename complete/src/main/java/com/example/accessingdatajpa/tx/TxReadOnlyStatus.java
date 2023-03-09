package com.example.accessingdatajpa.tx;

import java.util.Deque;
import java.util.LinkedList;

import static org.springframework.transaction.support.TransactionSynchronizationManager.isCurrentTransactionReadOnly;
import static org.springframework.transaction.support.TransactionSynchronizationManager.isSynchronizationActive;

public class TxReadOnlyStatus {

    private static final ThreadLocal<Deque<Boolean>> txStack = ThreadLocal.withInitial(LinkedList::new);

    public static boolean isReadOnly() {
        Boolean readOnly = getTxStack().peekLast();
        if (readOnly != null) {
            return readOnly;
        }
        if (isSynchronizationActive()) {
            // active synchronization
            return isCurrentTransactionReadOnly();
        }
        return true;
    }

    private static Deque<Boolean> getTxStack() {
        return txStack.get();
    }

    static void begin(Boolean readOnly) {
        getTxStack().addLast(readOnly);
    }

    static void complete() {
        getTxStack().removeLast();
    }

}
