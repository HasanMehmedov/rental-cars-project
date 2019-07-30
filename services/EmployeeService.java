package services;

import entities.*;
import exceptions.InvalidCommandException;
import exceptions.InvalidFieldException;
import exceptions.InvalidInputException;
import exceptions.InvalidOptionException;
import repositories.*;

import java.io.BufferedReader;
import java.io.IOException;
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

import static constants.Constants.NUMBER_REGEX;

public class EmployeeService {
    Connection connection;
    BufferedReader reader;
    DataRepository<Employee> employeeRepository;
    DataRepository<Branch> branchRepository;
    DataRepository<Car> carRepository;
    DataRepository<Address> addressRepository;
    DataRepository<Problem> problemRepository;
    DataRepository<Route> routeRepository;
    DataRepository<Service> serviceRepository;

    public EmployeeService(){}

    public EmployeeService(Connection connection, BufferedReader reader){
        this.connection = connection;
        this.reader = reader;
        this.employeeRepository = new EmployeeRepository(this.connection);
        this.branchRepository = new BranchRepository(this.connection);
        this.carRepository = new CarRepository(this.connection);
        this.addressRepository = new AddressRepository(this.connection);
        this.problemRepository = new ProblemRepository(this.connection);
        this.routeRepository = new RouteRepository(this.connection);
        this.serviceRepository = new ServiceRepository(this.connection);
    }

    public void employeeCommands() throws IOException, SQLException {
        int command;

        String username;
        String password;
        Employee employee;

        while (true) {
            try {
                System.out.print("Username: ");
                username = reader.readLine();
                System.out.print("Password: ");
                password = reader.readLine();
                String managerId = "NOT NULL";

                if (!employeeRepository.validateData(username, password, managerId)) {
                    throw new InvalidFieldException();
                }

                break;
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "username or password");
            }
        }

        employee = employeeRepository.read(username);
        while (true) {
            try {
                System.out.println("1. Drive a car for repair;");
                System.out.println("2. Exit.");

                String commandInput = reader.readLine();
                if (!commandInput.matches(NUMBER_REGEX)) {
                    throw new InvalidInputException();
                }

                command = Integer.parseInt(commandInput);
                switch (command) {
                    case 1:
                        driveCarForRepair(employee);
                        break;
                    case 2:
                        return;
                    default:
                        throw new InvalidCommandException();
                }

                break;
            } catch (InvalidInputException | InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void driveCarForRepair(Employee employee) throws SQLException, IOException {
        Car car;
        int carId;
        String town = branchRepository.getAll(String.valueOf(employee.getBranchId()), "id").get(0).getTown();
        List<Car> listOfCars = carRepository.getAll(town, "town").stream().filter(Car::getWhetherIsFree).collect(Collectors.toList());
        List<Problem> carProblems;
        Problem problemToResolve;

        if (listOfCars.isEmpty()) {
            System.out.println("No available cars");
            return;
        }

        System.out.println("Cars available:");
        for (int i = 0; i < listOfCars.size(); i++) {
            String address = addressRepository.getAll(String.valueOf(listOfCars.get(i).getAddressId()), "id").get(0).getName();

            System.out.printf("%d. %s %s, address: %s, battery: %d%%\n",
                    i + 1, listOfCars.get(i).getBrand(), listOfCars.get(i).getModel(),
                    address, listOfCars.get(i).getBatteryLevel());
        }

        while (true) {
            try {
                System.out.print("Choose car: ");
                if ((carId = Integer.parseInt(reader.readLine())) > listOfCars.size() || carId <= 0) {
                    throw new InvalidOptionException();
                }

                car = listOfCars.get(carId - 1);
                carProblems = problemRepository.getAll(String.valueOf(car.getId()), "car_id");
                if (carProblems.isEmpty()) {
                    System.out.println("There are no problems to resolve");
                    return;
                }

                break;
            } catch (InvalidOptionException e) {
                System.out.println(e.getMessage());
            }
        }


        System.out.println("Choose problem to resolve");
        for (int i = 0; i < carProblems.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, carProblems.get(i).getType());
        }

        int problemId;
        while (true) {
            try {
                if ((problemId = Integer.parseInt(reader.readLine())) > carProblems.size() || problemId <= 0) {
                    throw new InvalidOptionException();
                }

                problemToResolve = carProblems.get(problemId - 1);
                break;
            } catch (InvalidOptionException e) {
                System.out.println(e.getMessage());
            }
        }

        carRepository.updateField(car.getId(), "0", "is_free");

        System.out.println("Press \"ENTER\" to finish driving");
        long timeDriven = getDrivenTime();

        String startTime = getCurrentTime();
        String currDate = getCurrentDate();

        int initialAddressId = car.getAddressId();
        int endAddressId = getRandomAddress(town);

        updateCarState(carRepository, car, timeDriven, endAddressId);

        if (problemToResolve.getType().equals("recharge")) {
            carRepository.updateField(car.getId(), "100", "battery_level");
        }

        problemRepository.delete(String.valueOf(problemToResolve.getId()));

        String duration = convertTime(timeDriven * 1000);
        routeRepository.create(new Route("employee", car.getId(), town, currDate, initialAddressId, endAddressId, startTime, duration));
        int routeId = routeRepository.getAll().get(routeRepository.getAll().size() - 1).getId();

        serviceRepository.create(new Service(problemToResolve.getType(), car.getId()));
        int serviceId = serviceRepository.getAll().get(serviceRepository.getAll().size() - 1).getId();

        insertIntoEmployeesMappingTable(routeId, employee.getId(), serviceId);
    }

    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    private String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(cal.getTime());
    }

    private long getDrivenTime() throws IOException {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        Thread t1 = new Thread();
        t1.start();
        if (reader.readLine() != null) {
            endTime = (System.currentTimeMillis() - startTime) / 1000;
            t1.interrupt();
        }

        return endTime;
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

    private void updateCarState(DataRepository<Car> carRepository, Car car, long timeDriven, int endAddressId) throws SQLException {
        int carId = car.getId();

        carRepository.updateField(carId, String.valueOf(car.getMileage() + timeDriven), "mileage");
        carRepository.updateField(carId, "1", "is_free");

        long newBatteryLevel = car.getBatteryLevel() - 2 * timeDriven;
        if (newBatteryLevel < 0) {
            newBatteryLevel = 0;
        }
        carRepository.updateField(carId, String.valueOf(newBatteryLevel), "battery_level");

        carRepository.updateField(carId, String.valueOf(endAddressId), "address_id");
    }

    private String convertTime(long timeDriven) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(timeDriven),
                TimeUnit.MILLISECONDS.toMinutes(timeDriven) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDriven)),
                TimeUnit.MILLISECONDS.toSeconds(timeDriven) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDriven)));
    }

    private void insertIntoEmployeesMappingTable(int routeId, int employeeId, int serviceId) throws SQLException {
        String queryString = "" +
                "INSERT INTO employees_routes(route_id, employee_id, service_id) " +
                "VALUES(?, ?, ?)";

        PreparedStatement query = this.connection.prepareStatement(queryString);
        query.setInt(1, routeId);
        query.setInt(2, employeeId);
        query.setInt(3, serviceId);

        query.executeUpdate();
    }
}
