package com.example.clientserverapplicationcontracts.server;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class ContractManager {

    private SessionFactory sessionFactory;

    public void init() {
    	this.sessionFactory = new Configuration()
                    .addAnnotatedClass(Contract.class)
                    .buildSessionFactory();
    }

    public List<Contract> getAllContracts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Contract> query = session.createQuery("from Contract ", Contract.class);
            return query.list();
        }
    }
}
