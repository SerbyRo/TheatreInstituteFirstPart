package DatabaseRepository;

import Domain.Client;
import Domain.TheatreException;
import Domain.Ticket;
import Domain.User;
import Utils.JdbcUtils;
import Validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class UserDatabaseRepository implements UserInterfaceRepository {
    private SessionFactory sessionFactory;

    public UserDatabaseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public User findOne(Long id) {
        User user1 = null;
        try(Session session = sessionFactory.openSession())
        {
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                user1 = session.createQuery("from User where id='"+id+"'",User.class)
                        .setMaxResults(1)
                        .uniqueResult();
                tx.commit();
            }catch (RuntimeException ex)
            {
                System.err.println("Eroare la get" + ex);
                if (tx != null)
                {
                    tx.rollback();
                }
            }
        }
        return user1;
    }

    @Override
    public Collection<User> findAll() {
        List<User> users = null;
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                users = session.createQuery("from User", User.class).list();
                tx.commit();
            }catch (RuntimeException ex)
            {
                System.err.println("Eroare la select "+ ex);
                if (tx != null)
                {
                    tx.rollback();
                }
            }
            return users;
        }
    }

    @Override
    public User save(User entity) throws TheatreException {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
                return null;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        System.out.println(entity);
        User user = findOne(entity.getUserID());
        if (user==null )
        {
            throw new IllegalArgumentException("Nu exista userul!\n");
        }
        return user;
    }

    @Override
    public User delete(Long id) {
        User user = findOne(id);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                User crit = session.createQuery("from User where id='" + id+"'", User.class)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Stergem utilizatorul " + crit.getUserID());
                session.delete(crit);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la stergere " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return user;
    }

    @Override
    public User update(User entity) {
        User user = null;
        try(Session session = sessionFactory.openSession())
        {
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                user = session.load(User.class,entity.getUserID());
                user.setFirst_name(entity.getFirst_name());
                user.setUsername(entity.getUsername());
                user.setPassword(entity.getPassword());
                tx.commit();
            }catch (RuntimeException ex) {
                System.err.println("Eroare la update " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return user;
    }

    @Override
    public boolean loginasClient(User user) {
        User user1 = null;
        try(Session session = sessionFactory.openSession())
        {
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                user1 = session.createQuery("from User where first_name='"+user.getFirst_name()+"' and username='"+user.getUsername()+"' and password='"+user.getPassword()+"'",User.class)
                        .setMaxResults(1)
                        .uniqueResult();
                tx.commit();
            }catch (RuntimeException ex)
            {
                System.err.println("Eroare la get" + ex);
                if (tx != null)
                {
                    tx.rollback();
                }
            }
        }
        if (user1 != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean loginasAdmin(User user) {
        User user1 = null;
        try(Session session = sessionFactory.openSession())
        {
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                user1 = session.createQuery("from User where username='"+user.getUsername()+"' and password='"+user.getPassword()+"'",User.class)
                        .setMaxResults(1)
                        .uniqueResult();
                tx.commit();
            }catch (RuntimeException ex)
            {
                System.err.println("Eroare la get" + ex);
                if (tx != null)
                {
                    tx.rollback();
                }
            }
        }
        if (user1 != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public User findOneByUsername(String username) {
        User user1 = null;
        try(Session session = sessionFactory.openSession())
        {
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                user1 = session.createQuery("from User where username='"+username+"'",User.class)
                        .setMaxResults(1)
                        .uniqueResult();
                tx.commit();
            }catch (RuntimeException ex)
            {
                System.err.println("Eroare la get" + ex);
                if (tx != null)
                {
                    tx.rollback();
                }
            }
        }
        return user1;
    }

    @Override
    public User findOneByUsernameFirstname(String firstname, String username) {
        return null;
    }

}
