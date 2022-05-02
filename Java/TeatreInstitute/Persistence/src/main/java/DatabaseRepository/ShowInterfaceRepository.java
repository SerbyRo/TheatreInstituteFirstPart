package DatabaseRepository;

import Domain.Show;

public interface ShowInterfaceRepository extends CRUDRepository<Long, Show> {
    public Show findById(long id);
    void updatenrofseats(Show show);
}

