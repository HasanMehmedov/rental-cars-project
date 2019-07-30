package entities;

public class Branch {
    private int id;
    private String name;
    private String address;
    private String town;

    public Branch() {
    }

    public Branch(String name, String address, String town){
        this(0, name, address, town);
    }

    public Branch(int id, String name, String address, String town){
        setId(id);
        setName(name);
        setAddress(address);
        setTown(town);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    private void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    private void setTown(String town) {
        this.town = town;
    }
}
