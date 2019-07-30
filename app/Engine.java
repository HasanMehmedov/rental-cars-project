package app;

import entities.*;
import exceptions.*;
import repositories.*;
import services.CustomerService;
import services.EmployeeService;
import services.ManagerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static constants.Constants.*;

public class Engine implements Runnable {
    private Connection connection;
    private BufferedReader reader;
    private DataRepository<Customer> customerRepository;
    private DataRepository<BankAccount> bankAccountRepository;
    private DataRepository<Car> carRepository;
    private DataRepository<Address> addressRepository;
    private DataRepository<Coupon> couponRepository;
    private DataRepository<Route> routeRepository;
    private DataRepository<Branch> branchRepository;
    private DataRepository<Problem> problemRepository;
    private DataRepository<Service> serviceRepository;
    private DataRepository<Employee> employeeRepository;
    private DataRepository<Manager> managerRepository;

    public Engine(Connection connection) {
        this.connection = connection;
        this.reader = new BufferedReader(new InputStreamReader(System.in));

        customerRepository = new CustomerRepository(this.connection);
        bankAccountRepository = new BankAccountRepository(this.connection);
        carRepository = new CarRepository(this.connection);
        addressRepository = new AddressRepository(this.connection);
        couponRepository = new CouponRepository(this.connection);
        routeRepository = new RouteRepository(this.connection);
        branchRepository = new BranchRepository(this.connection);
        problemRepository = new ProblemRepository(this.connection);
        serviceRepository = new ServiceRepository(this.connection);
        employeeRepository = new EmployeeRepository(this.connection);
        managerRepository = new ManagerRepository(this.connection);
    }

    public void run() {
        int command;

        while (true) {
            try {
                System.out.println("Your are:");
                System.out.println("1. Customer;");
                System.out.println("2. Employee;");
                System.out.println("3. Manager;");
                System.out.println("4. Exit.");

                String commandInput = reader.readLine();
                if (!commandInput.matches(NUMBER_REGEX)) {
                    throw new InvalidInputException();
                }
                command = Integer.parseInt(commandInput);

                if (command == 1) {
                    CustomerService service = new CustomerService(connection, reader);
                    service.customerCommands();
                } else if (command == 2) {
                    EmployeeService service = new EmployeeService(connection, reader);
                    service.employeeCommands();
                } else if (command == 3) {
                    ManagerService service = new ManagerService(connection, reader);
                    service.managerCommands();
                } else if (command == 4) {
                    return;
                } else {
                    throw new InvalidCommandException();
                }

                System.out.println("Would you like to continue?");

                if (reader.readLine().equals("no")) {
                    break;
                }
            } catch (IOException | SQLException | InvalidInputException | InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}