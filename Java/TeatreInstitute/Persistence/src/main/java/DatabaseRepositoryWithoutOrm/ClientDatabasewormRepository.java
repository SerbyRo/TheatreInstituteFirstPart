package DatabaseRepositoryWithoutOrm;

import DatabaseRepository.ClientInterfaceRepository;
import DatabaseRepository.DatabaseException;
import Domain.Client;
import Domain.TheatreException;
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

public class ClientDatabasewormRepository implements ClientInterfaceRepository {
    private JdbcUtils dbUtils;
    private Validator<Long,Client> validator;
    private static final Logger logger= LogManager.getLogger();

    public ClientDatabasewormRepository(Properties props, Validator<Long,Client> validator)
    {
        logger.info("Initializing ClientDatabasewormRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        this.validator=validator;
    }
    public ClientDatabasewormRepository(Properties props)
    {
        logger.info("Initializing ClientDatabasewormRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public Client findOne(Long id) {
        if (id==null)
        {
            throw new IllegalArgumentException("Id-ul nu poate fi nul!\n");
        }
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Clients where client_id=?")){
            preparedStatement.setLong(1,id);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    return getClientFromResultSet(resultSet);
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
    public Collection<Client> findAll() {
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        List<Client> clients=new ArrayList<>();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select * from Clients")){
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    clients.add(getClientFromResultSet(resultSet));
                }
            }
        }catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Error db "+e);
        }
        logger.traceExit();
        return clients;
    }

    @Override
    public Client save(Client entity) throws TheatreException {
        if ( entity == null)
        {
            throw new IllegalArgumentException("Entitatea nu poate fi nula!\n");
        }
        validator.validate(entity);
        logger.traceEntry("saving task{} ",entity);
        Connection connection= dbUtils.getConnection();

        try(PreparedStatement preparedStatement= connection.prepareStatement("insert into Clients (first_name,username,password,age,gender) values (?,?,?,?,?)")){
            preparedStatement.setString(1, entity.getFirst_name());
            preparedStatement.setString(2, entity.getUsername());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setInt(4,entity.getAge());
            preparedStatement.setString(5, entity.getGender());
            preparedStatement.executeUpdate();
            //logger.trace("Saved {} instances", result);
            return null;
        }catch (SQLException ex)
        {
            logger.error(ex);
            System.err.println("Error DB "+ ex);
        }

        Client client=findOne(entity.getClientID());
        if (client==null )
        {
            throw new TheatreException("Nu exista clientul!\n");
        }
        return client;
    }

    @Override
    public Client delete(Long id) {
        if ( id == null)
        {
            throw new IllegalArgumentException("Id-ul nu poate fi vid!\n");
        }
        logger.traceEntry("deleting task{} ",id);
        Connection connection= dbUtils.getConnection();
        Client client=findOne(id);
        if (client!=null)
        {
            try(PreparedStatement preparedStatement= connection.prepareStatement("delete from Clients where client_id=?")){
                preparedStatement.setLong(1,id);
                preparedStatement.executeQuery();
            }catch (SQLException ex)
            {
                logger.error(ex);
                System.err.println("Error DB "+ ex);
            }
        }
        return client;
    }

    @Override
    public Client update(Client entity) {
        if (entity == null)
        {
            throw new IllegalArgumentException("Entitatea nu poate fi nunla!\n");
        }
        logger.traceEntry("updating task{} ",entity);
        Connection connection= dbUtils.getConnection();
        Client client=findOne(entity.getClientID());
        if (client != null)
        {
            try(PreparedStatement preparedStatement= connection.prepareStatement("update Clients set first_name=?,username=?,password=?,age=?,gender=? where client_id=?")){
                preparedStatement.setString(1, entity.getFirst_name());
                preparedStatement.setString(2, entity.getUsername());
                preparedStatement.setString(3, entity.getPassword());
                preparedStatement.setInt(4,entity.getAge());
                preparedStatement.setString(5, entity.getGender());
                preparedStatement.setLong(6,entity.getClientID());
                preparedStatement.executeUpdate();
                return null;
            }catch (SQLException ex)
            {
                logger.error(ex);
                System.err.println("Error DB "+ ex);
            }
        }
        return client;
    }

    @Override
    public Client findByName(String name) {
        if (name==null)
        {
            throw new IllegalArgumentException("Prenumele nu poate fi nul!\n");
        }
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Clients where first_name=?")){
            preparedStatement.setString(1,name);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    return getClientFromResultSet(resultSet);
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
    public Client getClientFromResultSet(ResultSet resultSet) {
        try{
            long id= resultSet.getLong("client_id");
            String first_name = resultSet.getString("first_name");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            int age = resultSet.getInt("age");
            String gender = resultSet.getString("gender");
            Client client=new Client(first_name,username,password,age,gender);
            client.setId(id);
            return client;
        }catch (SQLException ex)
        {
            throw new DatabaseException("Eroare la baza de date!\n");
        }
    }
}
