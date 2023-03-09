package com.example.accessingdatajpa;

import com.example.accessingdatajpa.tx.TxReadOnlyStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@ComponentScan(basePackages = "com.example.accessingdatajpa")
public class TransactionStatusTests {

    @Autowired
    @Qualifier("customizableTransactionTemplate")
    ObjectProvider<TransactionTemplate> customizableTransactionTemplates;

    @Test
    void test_required_rw() {
        execute(Propagation.REQUIRED, false, () -> {
            assertThat(TxReadOnlyStatus.isReadOnly())
                    .isFalse();
        });
    }

    @Test
    void test_required_ro() {
        execute(Propagation.REQUIRED, true, () -> {
            assertThat(TxReadOnlyStatus.isReadOnly())
                    .isTrue();
        });
    }

    @Test
    void test_requires_new_rw() {
        execute(Propagation.REQUIRES_NEW, false, () -> {
            assertThat(TxReadOnlyStatus.isReadOnly())
                    .isFalse();
        });
    }

    @Test
    void test_requires_new_ro() {
        execute(Propagation.REQUIRES_NEW, true, () -> {
            assertThat(TxReadOnlyStatus.isReadOnly())
                    .isTrue();
        });
    }

    @Test
    void test_required_ro_required_rw() {
        execute(Propagation.REQUIRED, true, () -> {
            execute(Propagation.REQUIRED, false, () -> {
                assertThat(TxReadOnlyStatus.isReadOnly())
                        .isTrue();
            });
        });
    }

    @Test
    void test_required_rw_required_ro() {
        execute(Propagation.REQUIRED, false, () -> {
            execute(Propagation.REQUIRED, true, () -> {
                assertThat(TxReadOnlyStatus.isReadOnly())
                        .isFalse();
            });
        });
    }

    @Test
    void test_required_ro_requires_new_rw() {
        execute(Propagation.REQUIRED, true, () -> {
            execute(Propagation.REQUIRES_NEW, false, () -> {
                assertThat(TxReadOnlyStatus.isReadOnly())
                        .isFalse();
            });
        });
    }

    @Test
    void test_required_rw_requires_new_ro() {
        execute(Propagation.REQUIRED, false, () -> {
            execute(Propagation.REQUIRES_NEW, true, () -> {
                assertThat(TxReadOnlyStatus.isReadOnly())
                        .isTrue();
            });
        });
    }


    @Test
    void test_required_ro_not_supported_requires_new_rw() {
        execute(Propagation.REQUIRED, true, () -> {
            execute(Propagation.NOT_SUPPORTED, false, () -> {
                execute(Propagation.REQUIRES_NEW, false, () -> {
                    assertThat(TxReadOnlyStatus.isReadOnly())
                            .isFalse();
                });
            });
        });
    }

    @Test
    void test_required_rw_not_supported_requires_new_ro() {
        execute(Propagation.REQUIRED, false, () -> {
            execute(Propagation.NOT_SUPPORTED, false, () -> {
                execute(Propagation.REQUIRES_NEW, true, () -> {
                    assertThat(TxReadOnlyStatus.isReadOnly())
                            .isTrue();
                });
            });
        });
    }

    @Test
    void test_required_ro_not_supported_required_rw() {
        execute(Propagation.REQUIRED, true, () -> {
            execute(Propagation.NOT_SUPPORTED, false, () -> {
                execute(Propagation.REQUIRED, false, () -> {
                    assertThat(TxReadOnlyStatus.isReadOnly())
                            .isFalse();
                });
            });
        });
    }

    @Test
    void test_required_rw_not_supported_required_ro() {
        execute(Propagation.REQUIRED, false, () -> {
            execute(Propagation.NOT_SUPPORTED, false, () -> {
                execute(Propagation.REQUIRED, true, () -> {
                    assertThat(TxReadOnlyStatus.isReadOnly())
                            .isTrue();
                });
            });
        });
    }

    @Test
    void test_required_ro_not_supported_required_ro() {
        execute(Propagation.REQUIRED, true, () -> {
            execute(Propagation.NOT_SUPPORTED, false, () -> {
                execute(Propagation.REQUIRED, true, () -> {
                    assertThat(TxReadOnlyStatus.isReadOnly())
                            .isTrue();
                });
            });
        });
    }

    @Test
    void test_required_rw_not_supported_required_rw() {
        execute(Propagation.REQUIRED, false, () -> {
            execute(Propagation.NOT_SUPPORTED, false, () -> {
                execute(Propagation.REQUIRED, false, () -> {
                    assertThat(TxReadOnlyStatus.isReadOnly())
                            .isFalse();
                });
            });
        });
    }

    @Test
    void test_supports_rw() {
        execute(Propagation.SUPPORTS, false, () -> {
            assertThat(TxReadOnlyStatus.isReadOnly())
                    .isFalse();
        });
    }

    @Test
    void test_supports_ro() {
        execute(Propagation.SUPPORTS, true, () -> {
            assertThat(TxReadOnlyStatus.isReadOnly())
                    .isTrue();
        });
    }

    @Test
    void test_supports_rw_required_ro() {
        execute(Propagation.SUPPORTS, false, () -> {
            execute(Propagation.REQUIRED, true, () -> {
                assertThat(TxReadOnlyStatus.isReadOnly())
                        .isTrue();
            });
        });
    }

    @Test
    void test_supports_ro_required_rw() {
        execute(Propagation.SUPPORTS, true, () -> {
            execute(Propagation.REQUIRED, false, () -> {
                assertThat(TxReadOnlyStatus.isReadOnly())
                        .isFalse();
            });
        });
    }

    @Test
    void test_no_tx() {
        assertThat(TxReadOnlyStatus.isReadOnly())
                .isTrue();
    }

    @Test
    void test_not_supported_ro() {
        execute(Propagation.NOT_SUPPORTED, true, () -> {
            assertThat(TxReadOnlyStatus.isReadOnly())
                    .isTrue();
        });
    }

    protected void execute(Propagation propagation, boolean readOnly, Runnable runnable) {
        execute(propagation, readOnly, TransactionDefinition.ISOLATION_DEFAULT, runnable);
    }

    protected void execute(Propagation propagation, boolean readOnly, int isolationLevel, Runnable runnable) {
        template(propagation, readOnly, isolationLevel).executeWithoutResult(status -> {
            runnable.run();
        });
    }

    protected TransactionTemplate template(Propagation propagation, boolean readOnly, int isolationLevel) {
        return customizableTransactionTemplates.getObject(propagation, readOnly, isolationLevel);
    }


}
