package Service;

import Domain.Client;
import Domain.Show;
import Domain.TheatreException;
import Domain.Ticket;

public interface ITheatreObserver {
    void ticketSold(Ticket ticket) throws TheatreException;
    void addedClient(Client client) throws TheatreException;
    void addedShow(Show show) throws TheatreException;
    void deletedShow(Show show) throws TheatreException;
    void updatedShow(Show show) throws TheatreException;
}
