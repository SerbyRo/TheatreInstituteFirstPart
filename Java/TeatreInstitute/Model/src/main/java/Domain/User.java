package Domain;

import java.util.Objects;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name="Users")
public class User extends HasId<Long>{
    private String first_name;
    private String username;
    private String password;

    public User(){

    }

    public User(Long id,String first_name, String username, String password) {
        setId(id);
        this.first_name = first_name;
        this.username = username;
        this.password = password;
    }

    public User(String first_name, String username, String password) {
        this.first_name = first_name;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User { id= " + getUserID()+'\''+
                " first_name= "+first_name+'\''+
                "username = "+username+'\''+
                " password = "+password+"}";
    }
    @Id
    @Column (name = "user_id")
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getUserID()
    {
        return getID();
    }
    public void setUserID(Long id)
    {
        setId(id);
    }
    @Column(name = "first_name")
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Column (name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(first_name, user.first_name) && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first_name, username, password);
    }
}
