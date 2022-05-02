package Domain;

import java.util.Objects;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "Tickets")
public class Ticket extends HasId<Long>{
    private Client client_name;
    private Show show_id;
    private int numberofseats;
    private int rownumber;
    private int lodge;
    private double price;
    private String status;

    public Ticket(){

    }

    public Ticket(Client client_name, Show show_id, int numberofseats, int rownumber, int lodge, double price, String status) {
        this.client_name = client_name;
        this.show_id = show_id;
        this.numberofseats = numberofseats;
        this.rownumber = rownumber;
        this.lodge = lodge;
        this.price = price;
        this.status = status;
    }

    public Ticket(Long id, Client client_name, Show show_id, int numberofseats, int rownumber, int lodge, double price, String status) {
        setId(id);
        this.client_name = client_name;
        this.show_id = show_id;
        this.numberofseats = numberofseats;
        this.rownumber = rownumber;
        this.lodge = lodge;
        this.price = price;
        this.status = status;
    }

    public Ticket(Client client_name, Show show_id, int numberofseats, int rownumber, int lodge, int price) {
        this.client_name = client_name;
        this.show_id = show_id;
        this.numberofseats = numberofseats;
        this.rownumber = rownumber;
        this.lodge = lodge;
        this.price = price;
        this.status = "Available";
    }
    @Id
    @Column(name="ticket_id")
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getTicketID()
    {
        return getID();
    }
    public void setTicketID(Long id)
    {
        setId(id);
    }
    @Override
    public String toString() {
        return "Ticket{ id= " +getTicketID()+'\''+
                ", client_name='" + client_name + '\'' +
                ", show_id='" + show_id + '\'' +
                ", numberofseats='" + numberofseats + '\'' +
                ", rownumber='" + rownumber + '\'' +
                ", lodge='" + lodge + '\'' +
                ", price='" + price + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    @ManyToOne
    @JoinColumn(name = "client_name")
    public Client getClient_name() {
        return client_name;
    }

    public void setClient_name(Client client_name) {
        this.client_name = client_name;
    }
    @ManyToOne
    @JoinColumn(name = "show_id")
    public Show getShow_id() {
        return show_id;
    }

    public void setShow_id(Show show_id) {
        this.show_id = show_id;
    }
    @Column(name = "numberofseats")
    public int getNumberofseats() {
        return numberofseats;
    }

    public void setNumberofseats(int numberofseats) {
        this.numberofseats = numberofseats;
    }
    @Column(name = "rownumber")
    public int getRownumber() {
        return rownumber;
    }

    public void setRownumber(int rownumber) {
        this.rownumber = rownumber;
    }
    @Column(name = "lodge")
    public int getLodge() {
        return lodge;
    }

    public void setLodge(int lodge) {
        this.lodge = lodge;
    }
    @Column(name = "price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket tickets = (Ticket) o;
        return show_id == tickets.show_id && numberofseats == tickets.numberofseats && rownumber == tickets.rownumber && lodge == tickets.lodge && price == tickets.price && Objects.equals(client_name, tickets.client_name) && Objects.equals(status, tickets.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client_name, show_id, numberofseats, rownumber, lodge, price, status);
    }
}
