package Domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import java.util.Objects;
@Entity
@Table (name = "Clients")
public class Client extends HasId<Long>{
    private String first_name;
    private String username;
    private String password;
    private int age;
    private String gender;
    public Client(){

    }

    public Client(Long id,String first_name, String username, String password, int age, String gender) {
        setId(id);
        this.first_name = first_name;
        this.username = username;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }

    public Client(Long id,String first_name, String username, String password) {
        setId(id);
        this.first_name = first_name;
        this.username = username;
        this.password = password;
    }

    public Client(String first_name, String username, String password) {
        this.first_name = first_name;
        this.username = username;
        this.password = password;
    }

    public Client(String first_name, String username, String password, int age, String gender) {
        this.first_name = first_name;
        this.username = username;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }


    @Override
    public String toString() {
        return "Client{ id= " +getClientID()+'\''+
                ", first_name='" + first_name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ",age='" + age + '\'' +
                "gender='" + gender + '\'' +
                '}';
    }
    @Id
    @Column(name = "client_id")
    @GeneratedValue(generator ="increment")
    @GenericGenerator(name="increment",strategy = "increment")
    public Long getClientID()
    {
        return getID();
    }
    public void setClientID(Long id)
    {
        setId(id);
    }
    @Column (name = "first_name")
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    @Column (name = "username")
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
    @Column (name = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    @Column (name = "gender")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return age == client.age && Objects.equals(first_name, client.first_name) && Objects.equals(username, client.username) && Objects.equals(password, client.password) && Objects.equals(gender, client.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first_name, username, password, age, gender);
    }
}
