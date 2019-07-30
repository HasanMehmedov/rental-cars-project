package entities;

public class Coupon {
    private int id;
    private int customerId;

    public Coupon(){}

    public Coupon(int customerId){
        this(0, customerId);
    }

    public Coupon(int id, int customerId){
        setId(id);
        setCustomerId(customerId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
