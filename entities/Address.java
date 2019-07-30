package entities;

public class Address {
    private int id;
    private String name;
    private String town;

    public Address(){}

    public Address(String name, String town){
        this(0, name, town);
    }

    public Address(int id, String name, String town){
        setId(id);
        setName(name);
        setTown(town);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
