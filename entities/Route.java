package entities;

public class Route {
    private int id;
    private String user;
    private int carId;
    private String town;
    private String date;
    private int initialAddressId;
    private int endAddressId;
    private String startTime;
    private String duration;

    public Route(){}

    public Route(String user, int carId, String town, String date, int initialAddressId, int endAddressId, String startTime, String duration){
        this(0, user, carId, town, date, initialAddressId, endAddressId, startTime, duration);
    }

    public Route(int id, String user, int carId, String town, String date, int initialAddressId, int endAddressId, String startTime, String duration) {
        setId(id);
        setUser(user);
        setCarId(carId);
        setTown(town);
        setDate(date);
        setInitialAddressId(initialAddressId);
        setEndAddressId(endAddressId);
        setStartTime(startTime);
        setDuration(duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getInitialAddressId() {
        return initialAddressId;
    }

    public void setInitialAddressId(int initialAddressId) {
        this.initialAddressId = initialAddressId;
    }

    public int getEndAddressId() {
        return endAddressId;
    }

    public void setEndAddressId(int endAddressId) {
        this.endAddressId = endAddressId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    private void setDuration(String duration){
        this.duration = duration;
    }

}
