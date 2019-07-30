package services;

import entities.*;
import exceptions.*;
import repositories.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static constants.Constants.*;

public class ManagerService {
    Connection connection;
    BufferedReader reader;
    DataRepository<Manager> managerRepository;
    DataRepository<Employee> employeeRepository;
    DataRepository<Branch> branchRepository;
    DataRepository<Car> carRepository;
    DataRepository<Customer> customerRepository;
    DataRepository<Coupon> couponRepository;
    DataRepository<Address> addressRepository;

    public ManagerService(){}

    public ManagerService(Connection connection, BufferedReader reader){
        this.connection = connection;
        this.reader = reader;
        this.managerRepository = new ManagerRepository(this.connection);
        this.employeeRepository = new EmployeeRepository(this.connection);
        this.branchRepository = new BranchRepository(this.connection);
        this.carRepository = new CarRepository(this.connection);
        this.customerRepository = new CustomerRepository(this.connection);
        this.couponRepository = new CouponRepository(this.connection);
        this.addressRepository = new AddressRepository(this.connection);
    }

    public void managerCommands() throws IOException, SQLException {
        int command;

        String username;
        String password;
        Manager manager;

        System.out.println("1. Log in;");
        System.out.println("2. Sign up.");

        int choice;
        if((choice = Integer.parseInt(reader.readLine())) == 1) {
            while (true) {
                try {
                    System.out.print("Username: ");
                    username = reader.readLine();
                    System.out.print("Password: ");
                    password = reader.readLine();
                    String managerId = "NULL";

                    if (!managerRepository.validateData(username, password, managerId)) {
                        throw new InvalidFieldException();
                    }

                    break;
                } catch (InvalidFieldException e) {
                    System.out.printf(e.getMessage(), "username or password");
                }
            }

            manager = managerRepository.read(username);


            while (true) {
                try {
                    System.out.println("1. Add employee;");
                    System.out.println("2. Delete employee;");
                    System.out.println("3. Add car;");
                    System.out.println("4. Delete car;");
                    System.out.println("5. Update car;");
                    System.out.println("6. Give coupon to customer;");
                    System.out.println("7. Take coupon from customer.");

                    String commandInput = reader.readLine();
                    if (!commandInput.matches(NUMBER_REGEX)) {
                        throw new InvalidInputException();
                    }
                    command = Integer.parseInt(commandInput);
                    switch (command) {
                        case 1:
                            addEmployee(manager);
                            break;
                        case 2:
                            deleteEmployee(manager);
                            break;
                        case 3:
                            addCar(manager);
                            break;
                        case 4:
                            deleteCar(manager);
                            break;
                        case 5:
                            updateCar(manager);
                            break;
                        case 6:
                            giveCouponToCustomer();
                            break;
                        case 7:
                            takeCouponFromCustomer();
                            break;
                        default:
                            throw new InvalidCommandException();
                    }

                    break;
                } catch (InvalidCommandException | InvalidInputException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println();
            }
        }else if(choice == 2){
            registerManager();
        }
    }

    private void registerManager() throws IOException, SQLException {
        String username;
        String password;
        String firstName;
        String lastName;
        String egn;
        String phoneNumber;
        String email;
        double salary = MANAGER_SALARY;
        int branchId;

        while (true) {
            try {
                System.out.print("Username (Number of characters must be from 8 to 20): ");
                if (!(username = reader.readLine()).matches(USERNAME_REGEX)) {
                    throw new InvalidFieldException();
                } else if (managerRepository.checkIfFieldValueExists(username, "username")) {
                    throw new AlreadyTakenException();
                } else {
                    break;
                }
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "username");
            } catch (AlreadyTakenException e) {
                System.out.printf(e.getMessage(), "username");
            }
        }

        while (true) {
            try {
                System.out.print("Password (Minimum 8 characters and it must contain at least ont letter and onr character): ");
                if (!(password = reader.readLine()).matches(PASSWORD_REGEX)) {
                    throw new InvalidFieldException();
                } else if (managerRepository.checkIfFieldValueExists(password, "password")) {
                    throw new AlreadyTakenException();
                } else {
                    break;
                }
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "password");
            } catch (AlreadyTakenException e) {
                System.out.printf(e.getMessage(), "password");
            }
        }

        while (true) {
            try {
                System.out.print("First name: ");
                if (!(firstName = reader.readLine()).matches(NAME_REGEX)) {
                    throw new InvalidFieldException();
                }

                break;
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "first name");
            }
        }

        while (true) {
            try {
                System.out.print("Last name: ");
                if (!(lastName = reader.readLine()).matches(NAME_REGEX)) {
                    throw new InvalidFieldException();
                }

                break;
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "last name");
            }
        }

        while (true) {
            try {
                System.out.print("EGN: ");
                if (!(egn = reader.readLine()).matches(EGN_REGEX)) {
                    throw new InvalidFieldException();
                } else if (managerRepository.checkIfFieldValueExists(egn, "egn")) {
                    throw new NotAvailableException();
                } else {
                    break;
                }
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage() , "EGN");
            } catch (NotAvailableException e) {
                System.out.printf(e.getMessage(), "EGN");
            }
        }

        while (true) {
            try {
                System.out.print("Phone number: ");
                if (!(phoneNumber = reader.readLine()).matches(PHONE_NUMBER_REGEX)) {
                    throw new InvalidFieldException();
                } else if (managerRepository.checkIfFieldValueExists(phoneNumber, "phone_number")) {
                    throw new NotAvailableException();
                } else {
                    break;
                }
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "phone number");
            } catch (NotAvailableException e) {
                System.out.printf(e.getMessage(), "phone number");
            }
        }

        while (true) {
            try {
                System.out.print("Email: ");
                if (!(email = reader.readLine()).matches(EMAIL_REGEX)) {
                    throw new InvalidFieldException();
                } else if (managerRepository.checkIfFieldValueExists(email, "email")) {
                    throw new NotAvailableException();
                } else {
                    break;
                }
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "email address");
            } catch (NotAvailableException e) {
                System.out.printf(e.getMessage(), "email address");
            }
        }

        int branchesCount = (int) branchRepository.getAll().stream().count();
        branchId = new Random().nextInt(branchesCount - 1) + 1;

        managerRepository.create(new Manager(username, password, firstName, lastName, egn, phoneNumber, email, salary, branchId));
    }

    private void addEmployee(Manager manager) throws IOException, SQLException {
        String username;
        String password;
        String firstName;
        String lastName;
        String egn;
        String phoneNumber;
        String email;
        double salary;
        int managerId;
        int branchId;

        while (true) {
            try {
                System.out.print("Username (Number of characters must be from 8 to 20): ");
                if (!(username = reader.readLine()).matches(USERNAME_REGEX)) {
                    throw new InvalidFieldException();
                } else if (managerRepository.checkIfFieldValueExists(username, "username")) {
                    throw new AlreadyTakenException();
                } else {
                    break;
                }
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "username");
            } catch (AlreadyTakenException e) {
                System.out.printf(e.getMessage(), "username");
            }
        }

        while (true) {
            try {
                System.out.print("Password (Minimum 8 characters and it must contain at least ont letter and onr character): ");
                if (!(password = reader.readLine()).matches(PASSWORD_REGEX)) {
                    throw new InvalidFieldException();
                } else if (managerRepository.checkIfFieldValueExists(password, "password")) {
                    throw new AlreadyTakenException();
                } else {
                    break;
                }
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "password");
            } catch (AlreadyTakenException e) {
                System.out.printf(e.getMessage(), "password");
            }
        }

        while (true) {
            try {
                System.out.print("First name: ");
                if (!(firstName = reader.readLine()).matches(NAME_REGEX)) {
                    throw new InvalidFieldException();
                }

                break;
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "first name");
            }
        }

        while (true) {
            try {
                System.out.print("Last name: ");
                if (!(lastName = reader.readLine()).matches(NAME_REGEX)) {
                    throw new InvalidFieldException();
                }

                break;
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "last name");
            }
        }

        while (true) {
            try {
                System.out.print("EGN: ");
                if (!(egn = reader.readLine()).matches(EGN_REGEX)) {
                    throw new InvalidFieldException();
                } else if (managerRepository.checkIfFieldValueExists(egn, "egn")) {
                    throw new NotAvailableException();
                } else {
                    break;
                }
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage() , "EGN");
            } catch (NotAvailableException e) {
                System.out.printf(e.getMessage(), "EGN");
            }
        }

        while (true) {
            try {
                System.out.print("Phone number: ");
                if (!(phoneNumber = reader.readLine()).matches(PHONE_NUMBER_REGEX)) {
                    throw new InvalidFieldException();
                } else if (managerRepository.checkIfFieldValueExists(phoneNumber, "phone_number")) {
                    throw new NotAvailableException();
                } else {
                    break;
                }
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "phone number");
            } catch (NotAvailableException e) {
                System.out.printf(e.getMessage(), "phone number");
            }
        }

        while (true) {
            try {
                System.out.print("Email: ");
                if (!(email = reader.readLine()).matches(EMAIL_REGEX)) {
                    throw new InvalidFieldException();
                } else if (managerRepository.checkIfFieldValueExists(email, "email")) {
                    throw new NotAvailableException();
                } else {
                    break;
                }
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "email address");
            } catch (NotAvailableException e) {
                System.out.printf(e.getMessage(), "email address");
            }
        }

        while (true) {
            try {
                System.out.print("Salary (Greater than or equal to 400): ");
                String salaryInput = reader.readLine();
                if (!salaryInput.matches(FLOATING_POINT_NUMBER_REGEX)) {
                    throw new InvalidInputException();
                } else if ((salary = Double.parseDouble(salaryInput)) < MIN_SALARY) {
                    throw new InvalidFieldException();
                }

                break;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "salary");
            }
        }

        managerId = manager.getId();
        branchId = manager.getBranchId();

        employeeRepository.create(new Employee(username, password, firstName, lastName, egn, phoneNumber, email, salary, managerId, branchId));
    }

    private void deleteEmployee(Manager manager) throws IOException, SQLException {
        String username;
        Employee employee;

        while (true) {
            try {
                System.out.print("Type employees username: ");
                username = reader.readLine();

                if (!employeeRepository.checkIfFieldValueExists(username, "username")) {
                    throw new NoSuchElementException();
                }

                employee = employeeRepository.read(username);
                break;
            } catch (NoSuchElementException e) {
                System.out.printf(e.getMessage(), "Username");
            }
        }

        if (employee.getManagerId() != manager.getId()) {
            System.out.println("You have no authority over this employee");
            return;
        }

        employeeRepository.delete(username);
    }

    private void addCar(Manager manager) throws SQLException, IOException {
        String brand;
        String model;
        String regNumber;
        int year;
        int mileage = 0;
        int batteryLevel = 100;
        boolean isFree = true;
        String town = branchRepository.getAll().get(manager.getBranchId() - 1).getTown();
        int addressId = getRandomAddress(town);

        while (true) {
            try {
                System.out.print("Brand: ");
                if (!(brand = reader.readLine()).matches(CAR_BRAND_REGEX)) {
                    throw new InvalidFieldException();
                }

                break;
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "brand");
            }
        }

        while (true) {
            try {
                System.out.print("Model: ");
                if (!(model = reader.readLine()).matches(CAR_MODEL_REGEX)) {
                    throw new InvalidFieldException();
                }

                break;
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "model");
            }
        }

        while (true) {
            try {
                System.out.print("Registration number: ");
                if (!(regNumber = reader.readLine()).matches(CAR_REGISTRATION_NUMBER_REGEX)) {
                    throw new InvalidFieldException();
                } else if (carRepository.checkIfFieldValueExists(regNumber, "reg_number")) {
                    throw new AlreadyTakenException();
                } else {
                    break;
                }
            } catch (InvalidFieldException | AlreadyTakenException e) {
                System.out.printf(e.getMessage(), "registration number");
            }
        }

        while (true) {
            try {
                System.out.print("Year: ");
                String yearInput;
                if (!(yearInput = reader.readLine()).matches(NUMBER_REGEX)) {
                    throw new InvalidInputException();
                }

                year = Integer.parseInt(yearInput);
                break;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }

        carRepository.create(new Car(brand, model, regNumber, year, mileage, batteryLevel, isFree, town, addressId));

    }

    private void deleteCar(Manager manager) throws IOException, SQLException {
        String regNumber;
        Car car;

        while (true) {
            try {
                System.out.print("Registration number: ");
                if (!(regNumber = reader.readLine()).matches(CAR_REGISTRATION_NUMBER_REGEX)) {
                    throw new InvalidFieldException();
                } else if (!carRepository.checkIfFieldValueExists(regNumber, "reg_number")) {
                    throw new NoSuchElementException();
                } else {
                    break;
                }
            } catch (InvalidFieldException | NoSuchElementException e) {
                System.out.printf(e.getMessage(), "registration number");
            }
        }

        car = carRepository.read(regNumber);
        String managersTown = branchRepository.getAll().get(manager.getBranchId() - 1).getTown();
        if (!car.getTown().equals(managersTown)) {
            System.out.println("You can't delete this car");
            return;
        }

        carRepository.delete(regNumber);
    }

    private void updateCar(Manager manager) throws IOException, SQLException {
        String regNumber;

        while (true) {
            try {
                System.out.print("Registration number: ");
                if (!(regNumber = reader.readLine()).matches(CAR_REGISTRATION_NUMBER_REGEX)) {
                    throw new InvalidFieldException();
                } else if (!carRepository.checkIfFieldValueExists(regNumber, "reg_number")) {
                    throw new NoSuchElementException();
                } else {
                    break;
                }
            } catch (InvalidFieldException | NoSuchElementException e) {
                System.out.printf(e.getMessage(), "registration number");
            }
        }

        String carTown = carRepository.read(regNumber).getTown();
        String managerTown = branchRepository.getAll().get(manager.getBranchId() - 1).getTown();

        if (!carTown.equals(managerTown)) {
            System.out.println("You can't update this car");
            return;
        }

        carRepository.update(regNumber);
    }

    private void giveCouponToCustomer() throws IOException, SQLException {
        String username;
        Customer customer;

        while (true) {
            try {
                System.out.print("Type customer username: ");
                username = reader.readLine();

                if (!customerRepository.checkIfFieldValueExists(username, "username")) {
                    throw new NoSuchElementException();
                }

                customer = customerRepository.read(username);
                break;
            } catch (NoSuchElementException e) {
                System.out.printf(e.getMessage(), "username");
            }
        }

        couponRepository.create(new Coupon(customer.getId()));
    }

    private void takeCouponFromCustomer() throws IOException, SQLException {
        String username;
        Customer customer;

        while (true) {
            try {
                System.out.print("Type customer username: ");
                username = reader.readLine();

                if (!customerRepository.checkIfFieldValueExists(username, "username")) {
                    throw new NoSuchElementException();
                }

                customer = customerRepository.read(username);
                if (!couponRepository.checkIfFieldValueExists(String.valueOf(customer.getId()), "customer_id")) {
                    System.out.println("Customer has no coupons");
                    return;
                }

                break;
            } catch (NoSuchElementException e) {
                System.out.printf(e.getMessage(), "username");
            }
        }

        couponRepository.delete(String.valueOf(customer.getId()));
    }

    private int getRandomAddress(String town) throws SQLException {
        List<Address> towns = new ArrayList<>();
        String queryString = "" +
                "SELECT * " +
                "FROM addresses " +
                "WHERE town = ?";

        PreparedStatement query = this.connection.prepareStatement(queryString);
        query.setString(1, town);
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            towns.add(new Address(rs.getInt("id"), rs.getString("name"), rs.getString("town")));
        }

        Random rnd = new Random();
        return towns.get(rnd.nextInt(towns.size())).getId();
    }
}
