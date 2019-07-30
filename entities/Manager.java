package entities;

public class Manager extends Person {
    private double salary;
    private int branchId;

    public Manager(){}

    public Manager(String username, String password, String firstName, String lastName,
                   String egn, String phoneNumber, String email, double salary, int branchId){
        this(0, username, password, firstName, lastName, egn, phoneNumber, email, salary, branchId);
    }

    public Manager(int id, String username, String password, String firstName,
                    String lastName, String egn, String phoneNumber, String email, double salary, int branchId){
        super(id, username, password, firstName, lastName, egn, phoneNumber, email);
        setSalary(salary);
        setBranchId(branchId);

    }

    public int getBranchId() {
        return branchId;
    }

    private void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
