package entities;

public class BankAccount {
    private int id;
    private String creditCardId;
    private double amount;
    private int customerId;

    public BankAccount(){}

    public BankAccount(String creditCardId, double amount, int customerId){
        this(0, creditCardId, amount, customerId);
    }

    public BankAccount(int id, String creditCardId, double amount, int customerId){
        setId(id);
        setCreditCardId(creditCardId);
        setAmount(amount);
        setCustomerId(customerId);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(String creditCardId) {
        this.creditCardId = creditCardId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
