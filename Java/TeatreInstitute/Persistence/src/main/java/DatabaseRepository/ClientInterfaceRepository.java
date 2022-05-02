package DatabaseRepository;

import Domain.Client;

public interface ClientInterfaceRepository extends CRUDRepository<Long, Client> {
    public Client findByName(String name);
}
