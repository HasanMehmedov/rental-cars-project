package entities;

public class Service {
    private int id;
    private String problemType;
    private int carId;

    public Service(){}

    public Service(String problemType, int carId){
        this(0, problemType, carId);
    }

    public Service(int id, String problemType, int carId){
        setId(id);
        setProblemType(problemType);
        setCarId(carId);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getProblemType() {
        return problemType;
    }

    private void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public int getCarId() {
        return carId;
    }

    private void setCarId(int carId) {
        this.carId = carId;
    }
}
