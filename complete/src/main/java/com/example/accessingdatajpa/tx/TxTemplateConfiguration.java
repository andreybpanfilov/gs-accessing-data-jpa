package com.example.accessingdatajpa.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TxTemplateConfiguration {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean("customizableTransactionTemplate")
    @Scope("prototype")
    public TransactionTemplate customizableTransactionTemplate(Propagation propagation, boolean readOnly, int isolationLevel) {
        TransactionTemplate template = new TransactionTemplate(platformTransactionManager);
        template.setReadOnly(readOnly);
        template.setIsolationLevel(isolationLevel);
        template.setPropagationBehavior(propagation.value());
        return template;
    }

}
