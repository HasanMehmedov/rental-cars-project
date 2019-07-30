package entities;

public class Customer extends Person {

    public Customer(){}

    public Customer(String username, String password, String firstName,
                    String lastName, String egn, String phoneNumber, String email){
        this(0, username, password, firstName, lastName, egn, phoneNumber, email);
    }

    public Customer(int id, String username, String password, String firstName,
                    String lastName, String egn, String phoneNumber, String email){
        super(id, username, password, firstName, lastName, egn, phoneNumber, email);
    }
}
