package com.example.accessingdatajpa;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SessionFactoryHasNameTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void test_session_factory_has_name() {
        assertThat(entityManagerFactory.unwrap(SessionFactoryImpl.class)
                .getName()).isEqualTo("my-cool-session-factory");
    }

}
