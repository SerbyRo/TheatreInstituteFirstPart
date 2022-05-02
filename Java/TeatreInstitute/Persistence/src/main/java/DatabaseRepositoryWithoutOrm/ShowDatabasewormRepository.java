package DatabaseRepositoryWithoutOrm;

import DatabaseRepository.DatabaseException;
import DatabaseRepository.ShowInterfaceRepository;
import Domain.Client;
import Domain.Show;
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

public class ShowDatabasewormRepository implements ShowInterfaceRepository {
    private JdbcUtils dbUtils;
    private Validator<Long, Show> validator;
    private static final Logger logger= LogManager.getLogger();

    public ShowDatabasewormRepository(Properties props, Validator<Long,Show> validator)
    {
        logger.info("Initializing ShowDatabasewormRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        this.validator=validator;
    }
    public ShowDatabasewormRepository(Properties props)
    {
        logger.info("Initializing ShowDatabasewormRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public Show findOne(Long id) {
        if (id==null)
        {
            throw new IllegalArgumentException("Id-ul nu poate fi nul!\n");
        }
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Shows where show_id=?")){
            preparedStatement.setLong(1,id);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    return getShowFromResultSet(resultSet);
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
    public Collection<Show> findAll() {
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        List<Show> shows=new ArrayList<>();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select * from Shows")){
            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    shows.add(getShowFromResultSet(resultSet));
                }
            }
        }catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Error db "+e);
        }
        logger.traceExit();
        return shows;
    }

    @Override
    public Show save(Show entity) throws TheatreException {
        if ( entity == null)
        {
            throw new IllegalArgumentException("Entitatea nu poate fi nula!\n");
        }
        validator.validate(entity);
        logger.traceEntry("saving task{} ",entity);
        Connection connection= dbUtils.getConnection();

        try(PreparedStatement preparedStatement= connection.prepareStatement("insert into Shows (name,description,type,nrofseats) values (?,?,?,?,?)")){
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setString(3, entity.getType());
            preparedStatement.setInt(4,entity.getNrofseats());
            preparedStatement.executeUpdate();
            //logger.trace("Saved {} instances", result);
            return null;
        }catch (SQLException ex)
        {
            logger.error(ex);
            System.err.println("Error DB "+ ex);
        }

        Show show=findOne(entity.getShowID());
        if (show==null )
        {
            throw new TheatreException("Nu exista clientul!\n");
        }
        return show;
    }

    @Override
    public Show delete(Long id) {
        if ( id == null)
        {
            throw new IllegalArgumentException("Id-ul nu poate fi vid!\n");
        }
        logger.traceEntry("deleting task{} ",id);
        Connection connection= dbUtils.getConnection();
        Show show=findOne(id);
        if (show!=null)
        {
            try(PreparedStatement preparedStatement= connection.prepareStatement("delete from Shows where show_id=?")){
                preparedStatement.setLong(1,id);
                preparedStatement.executeQuery();
            }catch (SQLException ex)
            {
                logger.error(ex);
                System.err.println("Error DB "+ ex);
            }
        }
        return show;
    }

    @Override
    public Show update(Show entity) {
        if (entity == null)
        {
            throw new IllegalArgumentException("Entitatea nu poate fi nunla!\n");
        }
        logger.traceEntry("updating task{} ",entity);
        Connection connection= dbUtils.getConnection();
        Show show=findOne(entity.getShowID());
        if (show != null)
        {
            try(PreparedStatement preparedStatement= connection.prepareStatement("update Shows set name=?,description=?,type=?,nrofseats=? where show_id=?")){
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getDescription());
                preparedStatement.setString(3, entity.getType());
                preparedStatement.setInt(4,entity.getNrofseats());
                preparedStatement.setLong(5,entity.getShowID());
                preparedStatement.executeUpdate();
                return null;
            }catch (SQLException ex)
            {
                logger.error(ex);
                System.err.println("Error DB "+ ex);
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
        if (show == null)
        {
            throw new IllegalArgumentException("Entitatea nu poate fi nunla!\n");
        }
        logger.traceEntry("updating task{} ",show);
        Connection connection= dbUtils.getConnection();
        Show show1=findOne(show.getShowID());
        if (show1 != null)
        {
            try(PreparedStatement preparedStatement= connection.prepareStatement("update Shows set nrofseats=? where show_id=?")){
                preparedStatement.setInt(1,show.getNrofseats());
                preparedStatement.setLong(2,show.getShowID());
                preparedStatement.executeUpdate();

            }catch (SQLException ex)
            {
                logger.error(ex);
                System.err.println("Error DB "+ ex);
            }
        }
    }
    public Show getShowFromResultSet(ResultSet resultSet) {
        try{
            long id= resultSet.getLong("show_id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String type = resultSet.getString("type");
            int nrofseats = resultSet.getInt("nrofseats");
            Show show=new Show(id,name,description,type,nrofseats);
            //show.setId(id);
            return show;
        }catch (SQLException ex)
        {
            throw new DatabaseException("Eroare la baza de date!\n");
        }
    }
}
