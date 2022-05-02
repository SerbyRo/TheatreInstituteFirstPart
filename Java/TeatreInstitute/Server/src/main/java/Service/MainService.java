package Service;

import DatabaseRepository.*;
import Domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainService implements ITheatreServices{
    private ClientInterfaceRepository clientInterfaceRepository;
    private ShowInterfaceRepository showInterfaceRepository;
    private TicketInterfaceRepository ticketInterfaceRepository;
    private UserInterfaceRepository userInterfaceRepository;
    private Map<String,ITheatreObserver> loggedClients;
    public MainService(ClientInterfaceRepository clientInterfaceRepository,ShowInterfaceRepository showInterfaceRepository,
                       TicketInterfaceRepository ticketInterfaceRepository,UserInterfaceRepository userInterfaceRepository)
    {
        this.clientInterfaceRepository=clientInterfaceRepository;
        this.showInterfaceRepository=showInterfaceRepository;
        this.ticketInterfaceRepository=ticketInterfaceRepository;
        this.userInterfaceRepository=userInterfaceRepository;
        loggedClients = new ConcurrentHashMap<>();
    }
    @Override
    public void loginUser(User user, ITheatreObserver client) throws TheatreException {
        User user1 = userInterfaceRepository.findOneByUsername(user.getUsername());
        if (user1 != null)
        {
            if (loggedClients.get(user.getUsername())!= null)
            {
                throw new TheatreException("User already logged in!\n");
            }
            loggedClients.put(user.getUsername(),client);
        }else
        {
            throw new TheatreException("Authentification failed!");
        }
    }

    @Override
    public void loginUser2(User user, ITheatreObserver client) throws TheatreException {
        User user1 = userInterfaceRepository.findOneByUsernameFirstname(user.getFirst_name(),user.getUsername());
        if (user1 != null)
        {
            if (loggedClients.get(user.getUsername())!= null)
            {
                throw new TheatreException("User already logged in!\n");
            }
            loggedClients.put(user.getUsername(),client);
        }else
        {
            throw new TheatreException("Authentification failed!");
        }
    }

    @Override
    public void logoutUser(User user, ITheatreObserver client) throws TheatreException {
        ITheatreObserver localClient = loggedClients.remove(user.getUsername());
        if (localClient == null)
        {
            throw new TheatreException("User " + user.getUsername()+" is not logged in");
        }
    }

    @Override
    public Show findShowById(Long id) throws TheatreException {
        return showInterfaceRepository.findOne(id);
    }

    @Override
    public Client findClientByName(String first_name) throws TheatreException {
        return clientInterfaceRepository.findByName(first_name);
    }
    public void updatenumberofticketsafterbuying(long show_id, int numberoftickets)
    {
        Show show = showInterfaceRepository.findOne(show_id);
        int numberofseats = show.getNrofseats();
        Show show1= new Show(show_id,numberofseats - numberoftickets);
        showInterfaceRepository.updatenrofseats(show1);
    }
    @Override
    public Ticket addTicket(Ticket ticket) throws TheatreException {
        Ticket ticket1 = ticketInterfaceRepository.save(ticket);
        updatenumberofticketsafterbuying(ticket.getShow_id().getShowID(),ticket.getNumberofseats());
        for (ITheatreObserver observer:loggedClients.values())
        {
            Thread th = new Thread(()->{
               try{
                   observer.ticketSold(ticket);
               }catch (TheatreException ex)
               {
                   ex.printStackTrace();
               }
            });
            th.start();
        }
        return ticket;
        //return ticket1;
    }

    @Override
    public Show addShow(Show show) throws TheatreException {
        Show show1 = showInterfaceRepository.save(show);
        for (ITheatreObserver observer:loggedClients.values())
        {
            Thread th = new Thread(()->{
                try{
                    observer.addedShow(show);
                }catch (TheatreException ex)
                {
                    ex.printStackTrace();
                }
            });
            th.start();
        }
        return show;
        //return show1;
    }

    @Override
    public Show deleteShow(Show show) throws TheatreException {
        Show show1 = showInterfaceRepository.delete(show.getShowID());
        for (ITheatreObserver observer:loggedClients.values())
        {
            Thread th = new Thread(()->{
                try{
                    observer.deletedShow(show);
                }catch (TheatreException ex)
                {
                    ex.printStackTrace();
                }
            });
            th.start();
        }
        return show;
        //return show1;
    }

    @Override
    public Show updateShow(Show show) throws TheatreException {
        Show show1 = showInterfaceRepository.update(show);
        for (ITheatreObserver observer:loggedClients.values())
        {
            Thread th = new Thread(()->{
                try{
                    observer.updatedShow(show);
                }catch (TheatreException ex)
                {
                    ex.printStackTrace();
                }
            });
            th.start();
        }
        return show;
        //return show1;
    }

    @Override
    public Client addClient(Client client) throws TheatreException {
       Client client1 = clientInterfaceRepository.save(client);
       for (ITheatreObserver observer:loggedClients.values())
       {
           Thread th = new Thread(()->{
               try{
                   observer.addedClient(client);
               }catch (TheatreException ex)
               {
                   ex.printStackTrace();
               }
           });
           th.start();
       }
       return client;
       //return client1;
    }

    @Override
    public List<Ticket> getAllTickets() throws TheatreException {
        return (List<Ticket>)ticketInterfaceRepository.findAll();
    }

    @Override
    public List<Client> getAllClients() throws TheatreException {
        return (List<Client>)clientInterfaceRepository.findAll();
    }

    @Override
    public List<Show> getAllShows() throws TheatreException {
        return (List<Show>) showInterfaceRepository.findAll();
    }

    @Override
    public void registerObserver(ITheatreObserver observer) {
        loggedClients.put(observer.toString(),observer);
    }
}
