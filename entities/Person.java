package entities;

public abstract class Person {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String egn;
    private String phoneNumber;
    private String email;

    public Person(){}

    public Person(String username, String password, String firstName,
                  String lastName, String egn, String phoneNumber, String email){
        this(0, username, password, firstName, lastName, egn, phoneNumber, email);
    }

    public Person(int id, String username, String password, String firstName,
    String lastName, String egn, String phoneNumber, String email){
        setId(id);
        setUsername(username);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
        setEgn(egn);
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEgn() {
        return egn;
    }

    private void setEgn(String egn) {
        this.egn = egn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }
}
