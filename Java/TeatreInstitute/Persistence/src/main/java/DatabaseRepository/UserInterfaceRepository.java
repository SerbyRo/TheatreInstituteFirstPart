package DatabaseRepository;

import Domain.User;

public interface UserInterfaceRepository extends CRUDRepository<Long, User>{
    public boolean loginasClient(User user);
    public boolean loginasAdmin(User user);
    public User findOneByUsername(String username);

    public User findOneByUsernameFirstname(String firstname,String username);
}
