package DatabaseRepository;

import Domain.*;
import Utils.JdbcUtils;
import Validators.ClientValidator;
import Validators.ShowValidator;
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
import java.util.*;

public class TicketDatabaseRepository implements TicketInterfaceRepository{
    private SessionFactory sessionFactory;

    public TicketDatabaseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Ticket findOne(Long id) {
        Ticket ticket = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                ticket = session.createQuery("from Ticket where id='" + id + "'", Ticket.class)
                        .setMaxResults(1)
                        .uniqueResult();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la get" + ex);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        return ticket;
    }

    @Override
    public Collection<Ticket> findAll() {
        List<Ticket> tickets = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                tickets=session.createQuery("from Ticket",Ticket.class).list();
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select " + ex);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        return tickets;
    }

    @Override
    public Ticket save(Ticket entity) throws TheatreException {
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
        Ticket ticket = findOne(entity.getTicketID());
        if (ticket==null )
        {
            throw new IllegalArgumentException("Nu exista userul!\n");
        }
        return ticket;
    }

    @Override
    public Ticket delete(Long id) {
        Ticket ticket = findOne(id);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Ticket crit = session.createQuery("from Ticket where id='" +id+"'", Ticket.class)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Stergem Ticket-ul " + crit.getTicketID());
                session.delete(crit);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la stergere " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return ticket;
    }

    @Override
    public Ticket update(Ticket entity) {
        Ticket ticket = null;
        try(Session session = sessionFactory.openSession())
        {
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                ticket = session.load(Ticket.class,entity.getTicketID());
                ticket.setClient_name(entity.getClient_name());
                ticket.setShow_id(entity.getShow_id());
                ticket.setRownumber(entity.getRownumber());
                ticket.setLodge(entity.getLodge());
                ticket.setNumberofseats(entity.getNumberofseats());
                ticket.setPrice(entity.getPrice());
                ticket.setStatus(entity.getStatus());
                tx.commit();
            }catch (RuntimeException ex) {
                System.err.println("Eroare la update " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return ticket;
    }

    @Override
    public int getnumberofremainingseats(Show show) {

        try(Session session = sessionFactory.openSession())
        {
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Show show1 = session.load(Show.class,show.getShowID());
                if (show1 != null)
                {
                    return show.getNrofseats();
                }
                tx.commit();
            }catch (RuntimeException ex) {
                System.err.println("Eroare la update " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return 0;
    }

}
