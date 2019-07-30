package entities;

public class Car {
    private int id;
    private String brand;
    private String model;
    private String regNumber;
    private int year;
    private int mileage;
    private int batteryLevel;
    private boolean isFree;
    private String town;
    private int addressId;

    public Car(){}

    public Car(String brand, String model, String regNumber, int year, int mileage, int batteryLevel, boolean isFree, String town, int addressId){
        this(0, brand, model, regNumber, year, mileage, batteryLevel, isFree, town, addressId);
    }

    public Car(int id, String brand, String model, String regNumber, int year, int mileage, int batteryLevel, boolean isFree, String town, int addressId) {
        setId(id);
        setBrand(brand);
        setModel(model);
        setRegNumber(regNumber);
        setYear(year);
        setMileage(mileage);
        setBatteryLevel(batteryLevel);
        setWhetherIsFree(isFree);
        setTown(town);
        setAddressId(addressId);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    private void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    private void setModel(String model) {
        this.model = model;
    }

    public String getRegNumber() {
        return regNumber;
    }

    private void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public int getYear() {
        return year;
    }

    private void setYear(int year) {
        this.year = year;
    }

    public int getMileage() {
        return mileage;
    }

    private void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    private void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public boolean getWhetherIsFree() {
        return isFree;
    }

    private void setWhetherIsFree(boolean free) {
        isFree = free;
    }

    public int getAddressId() {
        return addressId;
    }

    private void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getTown() {
        return town;
    }

    private void setTown(String town) {
        this.town = town;
    }
}
