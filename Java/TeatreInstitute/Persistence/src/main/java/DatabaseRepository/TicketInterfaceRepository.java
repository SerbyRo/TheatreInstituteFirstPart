package DatabaseRepository;

import Domain.Show;
import Domain.Ticket;

public interface TicketInterfaceRepository extends CRUDRepository<Long, Ticket> {
    public int getnumberofremainingseats(Show show);
}
