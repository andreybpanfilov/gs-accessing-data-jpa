package demo;

import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.internal.SessionFactoryImpl;

public class SessionBuilderCustom<T extends SessionBuilder> extends SessionFactoryImpl.SessionBuilderImpl<T> {

    public SessionBuilderCustom(SessionFactoryImpl sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Session openSession() {
        Session session = super.openSession();
        System.out.println("here we are");
        return session;
    }

}

