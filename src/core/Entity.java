package core;

public class Entity {
    private long id = UNKNOWN_ID;
    public static final long UNKNOWN_ID = -1;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}