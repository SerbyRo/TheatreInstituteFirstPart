package Domain;

import java.io.Serializable;

public abstract class HasId<ID> implements Serializable {
    private static final long serialID=4567787324513442547L;
    private ID id;
    public ID getID()
    {
        return id;
    }
    public void setId(ID id1)
    {
        id=id1;
    }
    @Override
    public abstract String toString();
}
