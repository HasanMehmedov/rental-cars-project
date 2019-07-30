package entities;

public class Employee extends Person {
    private double salary;
    private int managerId;
    private int branchId;

    public Employee(){}

    public Employee(String username, String password, String firstName,
                    String lastName, String egn, String phoneNumber,
                    String email, double salary, int managerId, int branchId){
        this(0, username, password, firstName, lastName, egn, phoneNumber, email, salary, managerId, branchId);
    }

    public Employee(int id, String username, String password, String firstName,
                    String lastName, String egn, String phoneNumber,
                    String email, double salary, int managerId, int branchId) {
        super(id, username, password, firstName, lastName, egn, phoneNumber, email);
        setSalary(salary);
        setBranchId(branchId);
        setManagerId(managerId);
    }

    public int getBranchId() {
        return branchId;
    }

    private void setBranchId(int branchId) {
        this.branchId = branchId;
    }


    public int getManagerId() {
        return managerId;
    }

    private void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
