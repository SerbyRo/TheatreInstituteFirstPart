package Domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "Shows")
public class Show extends HasId<Long>{
    private String name;
    private String description;
    private String type;
    private int nrofseats;

    public Show(){

    }
    public Show(Long id, String name, String description, String type, int nrofseats) {
        setId(id);
        this.name = name;
        this.description = description;
        this.type = type;
        this.nrofseats = nrofseats;
    }

    public Show(String name, String description, String type, int nrofseats) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.nrofseats = nrofseats;
    }

    public Show(Long id, int nrofseats) {
        setId(id);
        this.nrofseats = nrofseats;
    }
    @Id
    @Column (name = "show_id")
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getShowID()
    {
        return getID();
    }
    public void setShowID(Long id)
    {
        setId(id);
    }

    @Override
    public String toString() {
        return "Show{ id= '" +getShowID()+'\''+
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", nrofseats='" + nrofseats + '\'' +
                '}';
    }
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "nrofseats")
    public int getNrofseats() {
        return nrofseats;
    }

    public void setNrofseats(int nrofseats) {
        this.nrofseats = nrofseats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show shows = (Show) o;
        return nrofseats == shows.nrofseats && Objects.equals(name, shows.name) && Objects.equals(description, shows.description) && Objects.equals(type, shows.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, type, nrofseats);
    }
}
