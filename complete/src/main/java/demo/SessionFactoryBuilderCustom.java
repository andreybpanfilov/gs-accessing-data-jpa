package demo;

import org.hibernate.SessionFactory;
import org.hibernate.boot.internal.MetadataImpl;
import org.hibernate.boot.internal.SessionFactoryBuilderImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.bytecode.internal.SessionFactoryObserverForBytecodeEnhancer;
import org.hibernate.bytecode.spi.BytecodeProvider;
import org.hibernate.engine.query.spi.HQLQueryPlan;

public class SessionFactoryBuilderCustom extends SessionFactoryBuilderImpl {

    private final MetadataImplementor metadata;

    public SessionFactoryBuilderCustom(MetadataImplementor metadata) {
        super(metadata, ((MetadataImpl) metadata).getBootstrapContext());
        this.metadata = metadata;
    }

    @Override
    public SessionFactory build() {
        metadata.validate();
        final StandardServiceRegistry serviceRegistry = metadata.getMetadataBuildingOptions().getServiceRegistry();
        BytecodeProvider bytecodeProvider = serviceRegistry.getService(BytecodeProvider.class);
        addSessionFactoryObservers(new SessionFactoryObserverForBytecodeEnhancer(bytecodeProvider));
        return new SessionFactoryCustom(metadata, buildSessionFactoryOptions(), HQLQueryPlan::new);
    }

}
