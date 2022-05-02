import DatabaseRepository.*;
import DatabaseRepositoryWithoutOrm.ClientDatabasewormRepository;
import DatabaseRepositoryWithoutOrm.ShowDatabasewormRepository;
import DatabaseRepositoryWithoutOrm.TicketDatabasewormRepository;
import DatabaseRepositoryWithoutOrm.UserDatabasewormRepository;
import Domain.Client;
import Domain.Show;
import Domain.Ticket;
import Domain.User;
import Service.ITheatreServices;
import Service.MainService;
import Utils.AbstractServer;
import Utils.ServerException;
import Utils.TheatreRpcConcurrentServer;
import Validators.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

    private static SessionFactory sessionFactory;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }


    public static void main(String[] args){
        initialize();
        Properties serverProps = new Properties();
        try{
            serverProps.load(StartRpcServer.class.getResourceAsStream("server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        }catch (IOException ex)
        {
            System.err.println("Can't find server.properties" + ex);
            return;
        }
        Validator<Long, User> uservalidator = new UserValidator();
        Validator<Long, Client> clientvalidator = new ClientValidator();
        Validator<Long, Show> showvalidator = new ShowValidator();
        Validator<Long, Ticket> ticketvalidator = new TicketValidator();
//        UserInterfaceRepository userInterfaceRepository = new UserDatabaseRepository(sessionFactory);
//        ShowInterfaceRepository showInterfaceRepository = new ShowDatabaseRepository(sessionFactory);
//        ClientInterfaceRepository clientInterfaceRepository = new ClientDatabaseRepository(sessionFactory);
//        TicketInterfaceRepository ticketInterfaceRepository = new TicketDatabaseRepository(sessionFactory);
        UserInterfaceRepository userInterfaceRepository = new UserDatabasewormRepository(serverProps,uservalidator);
        ClientInterfaceRepository clientInterfaceRepository = new ClientDatabasewormRepository(serverProps,clientvalidator);
        ShowInterfaceRepository showInterfaceRepository = new ShowDatabasewormRepository(serverProps,showvalidator);
        TicketInterfaceRepository ticketInterfaceRepository = new TicketDatabasewormRepository(serverProps,ticketvalidator);
        ITheatreServices basketServices = new MainService(clientInterfaceRepository,showInterfaceRepository,ticketInterfaceRepository,userInterfaceRepository);
        int basketServerPort = defaultPort;
        try{
            basketServerPort = Integer.parseInt(serverProps.getProperty("Java.server.port"));
        }catch (NumberFormatException nef)
        {
            System.err.println("Wrong Port Number "+ nef.getMessage());
            System.err.println("Using default port" + defaultPort);
        }
        System.out.println("Starting server on port: " +basketServerPort);
        AbstractServer server = new TheatreRpcConcurrentServer(basketServerPort,basketServices);
        try{
            server.start();
        }catch (ServerException ex)
        {
            System.err.println("Error starting the server " + ex.getMessage());
        }finally {
            try{
                server.stop();
            }catch (ServerException ex)
            {
                System.err.println("Error stopping the server " + ex.getMessage());
            }
        }
    }
}
