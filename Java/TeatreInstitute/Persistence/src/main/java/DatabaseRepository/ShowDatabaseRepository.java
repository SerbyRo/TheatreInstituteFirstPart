package DatabaseRepository;

import Domain.Client;
import Domain.Show;
import Domain.TheatreException;
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

public class ShowDatabaseRepository implements ShowInterfaceRepository{
    private SessionFactory sessionFactory;

    public ShowDatabaseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Show findOne(Long id) {
        Show show = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                show = session.createQuery("from Show where id='" + id + "'", Show.class)
                        .setMaxResults(1)
                        .uniqueResult();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la get" + ex);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        return show;
    }

    @Override
    public Collection<Show> findAll() {
        List<Show> shows = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                shows=session.createQuery("from Show",Show.class).list();
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select " + ex);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        return shows;
    }

    @Override
    public Show save(Show entity) throws TheatreException {
        try (Session session = sessionFactory.openSession()) {
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
        Show show = findOne(entity.getShowID());
        if (show==null )
        {
            throw new IllegalArgumentException("Nu exista userul!\n");
        }
        return show;
    }

    @Override
    public Show delete(Long id) {
        Show show = findOne(id);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Show crit = session.createQuery("from Show where id='" +id+"'", Show.class)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Stergem Show-ul " + crit.getShowID());
                session.delete(crit);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la stergere " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return show;
    }

    @Override
    public Show update(Show entity) {
        Show show = null;
        try(Session session = sessionFactory.openSession())
        {
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                show = session.load(Show.class,entity.getShowID());
                show.setDescription(entity.getDescription());
                show.setName(entity.getName());
                show.setNrofseats(entity.getNrofseats());
                show.setType(entity.getType());
                tx.commit();
            }catch (RuntimeException ex) {
                System.err.println("Eroare la update " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return show;
    }

    @Override
    public Show findById(long id) {
        return null;
    }

    @Override
    public void updatenrofseats(Show show) {
        Show show1 = null;
        try(Session session = sessionFactory.openSession())
        {
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                show1 = session.load(Show.class,show.getShowID());
                show1.setNrofseats(show
                        .getNrofseats());
                tx.commit();
            }catch (RuntimeException ex) {
                System.err.println("Eroare la update " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

}
