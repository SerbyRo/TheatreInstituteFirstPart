package DatabaseRepository;

import Domain.Client;
import Domain.TheatreException;
import Domain.User;
import Utils.JdbcUtils;
import Validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.transaction.Transactional;

public class ClientDatabaseRepository implements ClientInterfaceRepository {
    private SessionFactory sessionFactory;

    public ClientDatabaseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        Client client = new Client("Serban","Serby","smecher",20,"masculin");
        Client client1 = findByName("Eric");
//        try{
//            Client client2 = save(client);
//        }catch (TheatreException ex)
//        {
//            System.out.println(ex.getMessage());
//        }
        //delete(3L);
        System.out.println(findAll().size());
    }

    @Override
    public Client findOne(Long id) {
        Client client = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                client = session.createQuery("from Client where id='" + id + "'", Client.class)
                        .setMaxResults(1)
                        .uniqueResult();
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la get" + ex);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        return client;
    }

    @Override
    public Collection<Client> findAll() {
        List<Client> clients = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                clients=session.createQuery("from Client",Client.class).list();
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select " + ex);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        return clients;
    }

    @Override
    public Client save(Client entity) throws TheatreException {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                System.out.println(entity);
                session.save(entity);
                System.out.println(entity);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare " + ex);
                if (tx != null)
                    tx.rollback();
            }
            return entity;
        }
    }


    @Override
    public Client delete(Long id) {
        Client client = findOne(id);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Client crit = session.createQuery("from Client where id='" +id+"'", Client.class)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Stergem clientul " + crit.getClientID());
                session.delete(crit);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la stergere " + ex);
                if (tx != null)
                    tx.rollback();
            }
            return client;
        }
    }

    @Override
    public Client update(Client entity) {
        Client new_client = null;
        try(Session session = sessionFactory.openSession())
        {
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                session.update(entity);
                new_client=entity;
                tx.commit();
            }catch (RuntimeException ex) {
                System.err.println("Eroare la update " + ex);
                if (tx != null)
                    tx.rollback();
            }
            return new_client;
        }

    }

    @Override
    public Client findByName(String name) {
        Client client = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                client = session.createQuery("from Client where first_name='" + name + "'", Client.class)
                        .setMaxResults(1)
                        .uniqueResult();
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la get" + ex);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        return client;
    }
}

