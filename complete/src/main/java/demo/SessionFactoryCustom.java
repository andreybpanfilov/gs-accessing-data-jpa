package demo;

import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.engine.query.spi.QueryPlanCache;
import org.hibernate.engine.spi.SessionBuilderImplementor;
import org.hibernate.internal.SessionFactoryImpl;

public class SessionFactoryCustom extends SessionFactoryImpl {

    public SessionFactoryCustom(MetadataImplementor metadata, SessionFactoryOptions options, QueryPlanCache.QueryPlanCreator queryPlanCacheFunction) {
        super(metadata, options, queryPlanCacheFunction);
    }

    @Override
    public SessionBuilderImplementor withOptions() {
        return new SessionBuilderCustom(this);
    }

}
