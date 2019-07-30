package entities;

public class Problem {
    private int id;
    private int carId;
    private String type;

    public Problem(){}

    public Problem(int carId, String type){
        this(0, carId, type);
    }

    public Problem(int id, int carId, String type){
        setId(id);
        setCarId(carId);
        setType(type);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    private void setCarId(int carId) {
        this.carId = carId;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }
}
