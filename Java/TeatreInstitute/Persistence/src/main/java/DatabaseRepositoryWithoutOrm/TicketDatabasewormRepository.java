package DatabaseRepositoryWithoutOrm;

import DatabaseRepository.DatabaseException;
import DatabaseRepository.TicketInterfaceRepository;
import Domain.Client;
import Domain.Show;
import Domain.TheatreException;
import Domain.Ticket;
import Utils.JdbcUtils;
import Validators.ClientValidator;
import Validators.ShowValidator;
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

public class TicketDatabasewormRepository implements TicketInterfaceRepository {
    private JdbcUtils dbUtils;
    private Validator<Long, Ticket> validator;
    private static final Logger logger= LogManager.getLogger();

    public TicketDatabasewormRepository(Properties props, Validator<Long,Ticket> validator)
    {
        logger.info("Initializing TicketDatabasewormRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        this.validator=validator;
    }
    public TicketDatabasewormRepository(Properties props)
    {
        logger.info("Initializing TicketDatabasewormRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    public Client findclientbyname(String first_name)
    {

        Validator<Long,Client> validator=new ClientValidator();
        ClientDatabasewormRepository clientsDatabaseRepository=new ClientDatabasewormRepository(dbUtils.getProperties(),validator);
        for (Client client : clientsDatabaseRepository.findAll())
        {
            if (client.getFirst_name().equals(first_name))
            {
                return client;
            }
        }
        return null;
    }
    public Show findShowbyid(Long id)
    {

        Validator<Long,Show> validator=new ShowValidator();
        ShowDatabasewormRepository showDatabasewormRepository=new ShowDatabasewormRepository(dbUtils.getProperties(),validator);
        for (Show show: showDatabasewormRepository.findAll())
        {
            if (show.getShowID().equals(id))
            {
                return show;
            }
        }
        return null;
    }

    @Override
    public Ticket findOne(Long id) {
        if (id==null)
        {
            throw new IllegalArgumentException("Id-ul nu poate fi nul!\n");
        }
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Tickets where ticket_id=?")){
            preparedStatement.setLong(1,id);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    return getTicketFromResultSet(resultSet);
                }
                else {
                    return null;
                }
            }
        }catch (SQLException ex)
        {
            logger.error(ex);
            System.err.println("Error db "+ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Collection<Ticket> findAll() {
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        List<Ticket> tickets=new ArrayList<>();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select * from Tickets")){
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    tickets.add(getTicketFromResultSet(resultSet));
                }
            }
        }catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Error db "+e);
        }
        logger.traceExit();
        return tickets;
    }

    @Override
    public Ticket save(Ticket entity) throws TheatreException {
        if (entity == null)
        {
            throw new IllegalArgumentException("Entitatea nu poate fi nula!\n");
        }
        validator.validate(entity);
        logger.traceEntry("saving task{} ",entity);


        try(Connection connection= dbUtils.getConnection();PreparedStatement preparedStatement= connection.prepareStatement("insert into Tickets (client_name,show_id,numberofseats,rownumber,lodge,price,status) values (?,?,?,?,?,?,?)")){
            preparedStatement.setString(1,entity.getClient_name().getFirst_name());
            preparedStatement.setLong(2,entity.getShow_id().getShowID());
            preparedStatement.setInt(3,entity.getNumberofseats());
            preparedStatement.setInt(4,entity.getRownumber());
            preparedStatement.setInt(5,entity.getLodge());
            preparedStatement.setDouble(6,entity.getPrice());
            preparedStatement.setString(7, entity.getStatus());
            preparedStatement.executeUpdate();
            //logger.trace("Saved {} instances", result);
            return null;
        }catch (SQLException ex)
        {
            logger.error(ex);
            System.err.println("Error DB "+ ex);
        }

        return entity;
    }

    @Override
    public Ticket delete(Long id) {
        if ( id == null)
        {
            throw new IllegalArgumentException("Id-ul nu poate fi vid!\n");
        }
        logger.traceEntry("deleting task{} ",id);
        Connection connection= dbUtils.getConnection();
        Ticket ticket=findOne(id);
        if (ticket!=null)
        {
            try(PreparedStatement preparedStatement= connection.prepareStatement("delete from Tickets where ticket_id=?")){
                preparedStatement.setLong(1,id);
                preparedStatement.executeQuery();
            }catch (SQLException ex)
            {
                logger.error(ex);
                System.err.println("Error DB "+ ex);
            }
        }
        return ticket;
    }
    @Override
    public Ticket update(Ticket entity) {
        if (entity == null)
        {
            throw new IllegalArgumentException("Entitatea nu poate fi nunla!\n");
        }
        logger.traceEntry("updating task{} ",entity);
        Connection connection= dbUtils.getConnection();
        Ticket ticket=findOne(entity.getTicketID());
        if (ticket != null)
        {
            try(PreparedStatement preparedStatement= connection.prepareStatement("update Tickets set client_name=?,show_id=?,numberofseats=?,rownumber=?,lodge=?,price=?,status=? where ticket_id=?")){
                preparedStatement.setString(1,entity.getClient_name().getFirst_name());
                preparedStatement.setLong(2,entity.getShow_id().getShowID());
                preparedStatement.setInt(3,entity.getNumberofseats());
                preparedStatement.setInt(4,entity.getRownumber());
                preparedStatement.setInt(5,entity.getLodge());
                preparedStatement.setDouble(6,entity.getPrice());
                preparedStatement.setString(7, entity.getStatus());
                preparedStatement.setLong(8,entity.getTicketID());
                preparedStatement.executeUpdate();
                return null;
            }catch (SQLException ex)
            {
                logger.error(ex);
                System.err.println("Error DB "+ ex);
            }
        }
        return ticket;
    }

    @Override
    public int getnumberofremainingseats(Show show) {
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select S.nrofseats from Shows as S inner join Tickets T on T.show_id=S.show_id where S.id=?"))
        {
            preparedStatement.setLong(1,show.getShowID());
            ResultSet resultSet= preparedStatement.executeQuery();
            if (resultSet.next())
            {
                return resultSet.getInt("nrofseats");
            }
        }catch (SQLException ex)
        {
            System.out.println(ex);
        }
        return -1;
    }
    public Ticket getTicketFromResultSet(ResultSet resultSet) {
        try{
            long id= resultSet.getLong("ticket_id");
            long show_id=resultSet.getLong("show_id");
            String client_name=resultSet.getString("client_name");
            int numberofseats= resultSet.getInt("nrofseats");
            int rownumber = resultSet.getInt("rownumber");
            int lodge = resultSet.getInt("lodge");
            double price=resultSet.getDouble("price");
            String status = resultSet.getString("status");
            Client client = findclientbyname(client_name);
            Show show=findShowbyid(show_id);
            Ticket ticket=new Ticket(id,client,show,numberofseats,rownumber,lodge,price,status);
            return ticket;

        }catch (SQLException ex)
        {
            throw new DatabaseException("Eroare la baza de date!\n");
        }
    }
}
