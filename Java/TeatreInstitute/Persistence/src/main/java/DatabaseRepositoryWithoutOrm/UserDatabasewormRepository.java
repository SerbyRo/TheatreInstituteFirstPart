package DatabaseRepositoryWithoutOrm;

import DatabaseRepository.DatabaseException;
import DatabaseRepository.UserInterfaceRepository;
import Domain.TheatreException;
import Domain.Ticket;
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

public class UserDatabasewormRepository implements UserInterfaceRepository {
    private JdbcUtils dbUtils;
    private Validator<Long, User> validator;
    private static final Logger logger= LogManager.getLogger();

    public UserDatabasewormRepository(Properties props, Validator<Long,User> validator)
    {
        logger.info("Initializing UserDatabasewormRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        this.validator=validator;
    }
    public UserDatabasewormRepository(Properties props)
    {
        logger.info("Initializing UserDatabasewormRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public User findOne(Long id) {
        if (id==null)
        {
            throw new IllegalArgumentException("Id-ul nu poate fi nul!\n");
        }
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Users where user_id=?")){
            preparedStatement.setLong(1,id);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    return getUserFromResultSet(resultSet);
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
    public Collection<User> findAll() {
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        List<User> users=new ArrayList<>();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select * from Users")){
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    users.add(getUserFromResultSet(resultSet));
                }
            }
        }catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Error db "+e);
        }
        logger.traceExit();
        return users;
    }

    @Override
    public User save(User entity) throws TheatreException {
        if ( entity == null)
        {
            throw new IllegalArgumentException("Entitatea nu poate fi nula!\n");
        }
        validator.validate(entity);
        logger.traceEntry("saving task{} ",entity);
        Connection connection= dbUtils.getConnection();

        try(PreparedStatement preparedStatement= connection.prepareStatement("insert into Users (first_name,username,password) values (?,?,?)")){
            preparedStatement.setString(1, entity.getFirst_name());
            preparedStatement.setString(2, entity.getUsername());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.executeUpdate();
            //logger.trace("Saved {} instances", result);
            return null;
        }catch (SQLException ex)
        {
            logger.error(ex);
            System.err.println("Error DB "+ ex);
        }
        User user=findOne(entity.getUserID());
        if (user==null )
        {
            throw new IllegalArgumentException("Nu exista userul!\n");
        }
        return user;
    }

    @Override
    public User delete(Long id) {
        if ( id == null)
        {
            throw new IllegalArgumentException("Id-ul nu poate fi vid!\n");
        }
        logger.traceEntry("deleting task{} ",id);
        Connection connection= dbUtils.getConnection();
        User user=findOne(id);
        if (user!=null)
        {
            try(PreparedStatement preparedStatement= connection.prepareStatement("delete from Users where user_id=?")){
                preparedStatement.setLong(1,id);
                preparedStatement.executeQuery();
            }catch (SQLException ex)
            {
                logger.error(ex);
                System.err.println("Error DB "+ ex);
            }
        }
        return user;
    }

    @Override
    public User update(User entity) {
        if (entity == null)
        {
            throw new IllegalArgumentException("Entitatea nu poate fi nunla!\n");
        }
        logger.traceEntry("updating task{} ",entity);
        Connection connection= dbUtils.getConnection();
        User user=findOne(entity.getUserID());
        if (user != null)
        {
            try(PreparedStatement preparedStatement= connection.prepareStatement("update Users set first_name=?,username=?,password=? where user_id=?")){
                preparedStatement.setString(1, entity.getFirst_name());
                preparedStatement.setString(2,entity.getUsername());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setLong(4,entity.getUserID());
                preparedStatement.executeUpdate();
                return null;
            }catch (SQLException ex)
            {
                logger.error(ex);
                System.err.println("Error DB "+ ex);
            }
        }
        return user;
    }

    @Override
    public boolean loginasClient(User user) {
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select 1 from Users where first_name=? and username=? and password=?")) {
            preparedStatement.setString(1, user.getFirst_name());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }

        }catch (SQLException ex)
        {
            throw new DatabaseException("Eroare la baza de date!\n");
        }
        return false;
    }

    @Override
    public boolean loginasAdmin(User user) {
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select 1 from Users where username=? and password=?")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }

        }catch (SQLException ex)
        {
            throw new DatabaseException("Eroare la baza de date!\n");
        }
        return false;
    }

    @Override
    public User findOneByUsername(String username) {
        if (username==null)
        {
            throw new IllegalArgumentException("Username-ul nu poate fi nul!\n");
        }
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Users where username=?")){
            preparedStatement.setString(1,username);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    return getUserFromResultSet(resultSet);
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
    public User findOneByUsernameFirstname(String firstname,String username) {
        if (username==null)
        {
            throw new IllegalArgumentException("Username-ul nu poate fi nul!\n");
        }
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Users where first_name=? and username=?")){
            preparedStatement.setString(1,firstname);
            preparedStatement.setString(2,username);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    return getUserFromResultSet(resultSet);
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
    public User getUserFromResultSet(ResultSet resultSet) {
        try{
            long id= resultSet.getLong("user_id");
            String first_name= resultSet.getString("first_name");
            String username=resultSet.getString("username");
            String password= resultSet.getString("password");
            User user=new User(first_name,username,password);
            user.setId(id);
            return user;
        }catch (SQLException ex)
        {
            throw new DatabaseException("Eroare la baza de date!\n");
        }
    }
}
