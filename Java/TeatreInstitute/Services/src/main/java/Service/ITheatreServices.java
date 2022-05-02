package Service;

import Domain.*;

import java.util.List;
import java.util.spi.LocaleServiceProvider;

public interface ITheatreServices {
    void loginUser(User user, ITheatreObserver client) throws TheatreException;
    void loginUser2(User user,ITheatreObserver client) throws TheatreException;
    void logoutUser(User user,ITheatreObserver client) throws TheatreException;
    Show findShowById(Long id) throws TheatreException;
    Client findClientByName(String first_name) throws TheatreException;
    Ticket addTicket(Ticket ticket) throws TheatreException;
    Show addShow(Show show) throws TheatreException;
    Show deleteShow(Show show) throws TheatreException;
    Show updateShow(Show show) throws TheatreException;
    Client addClient(Client client) throws TheatreException;
    List<Ticket> getAllTickets() throws TheatreException;
    List<Client> getAllClients() throws TheatreException;
    List<Show> getAllShows() throws TheatreException;

    void registerObserver(ITheatreObserver observer);
}
