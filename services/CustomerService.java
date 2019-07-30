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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static constants.Constants.*;

public class CustomerService {
    Connection connection;
    BufferedReader reader;
    DataRepository<Customer> customerRepository;
    DataRepository<BankAccount> bankAccountRepository;
    DataRepository<Address> addressRepository;
    DataRepository<Car> carRepository;
    DataRepository<Coupon> couponRepository;
    DataRepository<Route> routeRepository;

    public CustomerService(){}

    public CustomerService(Connection connection, BufferedReader reader){
        this.connection = connection;
        this.reader = reader;
        this.customerRepository = new CustomerRepository(this.connection);
        this.bankAccountRepository = new BankAccountRepository(this.connection);
        this.addressRepository = new AddressRepository(this.connection);
        this.carRepository = new CarRepository(this.connection);
        this.couponRepository = new CouponRepository(this.connection);
        this.routeRepository = new RouteRepository(this.connection);
    }

    public void customerCommands() throws IOException, SQLException {
        int command;

        while (true) {
            try {
                System.out.println("1. Registration;");
                System.out.println("2. Update customer data;");
                System.out.println("3. Make a route;");
                System.out.println("4. Show all your routes;");
                System.out.println("5. Exit.");

                String commandInput = reader.readLine();
                if (!commandInput.matches(NUMBER_REGEX)) {
                    throw new InvalidInputException();
                }

                command = Integer.parseInt(commandInput);
                switch (command) {
                    case 1:
                        registerCustomer();
                        break;
                    case 2:
                        updateCustomerData();
                        break;
                    case 3:
                        makeRoute();
                        break;
                    case 4:
                        showAllRoutes();
                        break;
                    case 5:
                        return;
                    default:
                        throw new InvalidCommandException();
                }
                break;
            } catch (InvalidInputException | InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
        }
    }

    private void registerCustomer() throws IOException, SQLException {
        String username;
        String password;
        String firstName;
        String lastName;
        String egn;
        String phoneNumber;
        String email;
        String creditCardId;
        double amount;

        while (true) {
            try {
                System.out.print("Username (Number of characters must be from 8 to 20): ");
                if (!(username = reader.readLine()).matches(USERNAME_REGEX)) {
                    throw new InvalidFieldException();
                } else if (customerRepository.checkIfFieldValueExists(username, "username")) {
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
                } else if (customerRepository.checkIfFieldValueExists(password, "password")) {
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
                } else if (customerRepository.checkIfFieldValueExists(egn, "egn")) {
                    throw new NotAvailableException();
                } else {
                    break;
                }
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "EGN");
            } catch (NotAvailableException e) {
                System.out.printf(e.getMessage(), "EGN");
            }
        }

        while (true) {
            try {
                System.out.print("Phone number: ");
                if (!(phoneNumber = reader.readLine()).matches(PHONE_NUMBER_REGEX)) {
                    throw new InvalidFieldException();
                } else if (customerRepository.checkIfFieldValueExists(phoneNumber, "phone_number")) {
                    throw new NotAvailableException();
                } else {
                    break;
                }
            } catch (NotAvailableException e) {
                System.out.printf(e.getMessage(), "phone number");
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "phone number");
            }
        }

        while (true) {
            try {
                System.out.print("Email: ");
                if (!(email = reader.readLine()).matches(EMAIL_REGEX)) {
                    throw new InvalidFieldException();
                } else if (customerRepository.checkIfFieldValueExists(email, "email")) {
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
                System.out.print("Credit card ID: ");
                if (!(creditCardId = reader.readLine()).matches(CREDIT_CARD_REGEX)) {
                    throw new InvalidFieldException();
                } else if (bankAccountRepository.checkIfFieldValueExists(creditCardId, "credit_card_id")) {
                    throw new NotAvailableException();
                } else {
                    break;
                }
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "credit card ID");
            } catch (NotAvailableException e) {
                System.out.printf(e.getMessage(), "credit card ID");
            }
        }

        while (true) {
            try {
                System.out.print("Amount: ");
                String amountInput = reader.readLine();
                if (!amountInput.matches(FLOATING_POINT_NUMBER_REGEX)) {
                    throw new InvalidInputException();
                }else if ((amount = Double.parseDouble(amountInput)) <= 0) {
                    throw new AmountException();
                }
                break;
            } catch (InvalidInputException | AmountException e) {
                System.out.println(e.getMessage());
            }
        }

        customerRepository.create(new Customer(username, password, firstName, lastName, egn, phoneNumber, email));
        int customerId = customerRepository.getValueId(username);
        bankAccountRepository.create(new BankAccount(creditCardId, amount, customerId));
    }

    private void updateCustomerData() throws SQLException, IOException {

        String username;
        String password;

        while (true) {
            try {
                System.out.print("Username: ");
                username = reader.readLine();
                System.out.print("Password: ");
                password = reader.readLine();

                if (!customerRepository.validateData(username, password)) {
                    throw new InvalidFieldException();
                }

                break;
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "username or password");
            }
        }

        int customerId = customerRepository.getValueId(username);
        customerRepository.update(username);
        bankAccountRepository.update(String.valueOf(customerId));
    }

    private void makeRoute() throws SQLException, IOException {

        String username;
        String password;
        Customer customer;
        String town;
        int carId;
        double price;
        List<String> listOfTowns;
        List<Car> listOfCars;


        while (true) {
            try {
                System.out.print("Type your username: ");
                username = reader.readLine();
                System.out.print("Type your password: ");
                password = reader.readLine();

                if (!customerRepository.validateData(username, password)) {
                    throw new InvalidFieldException();
                }

                break;
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "username or password");
            }
        }

        customer = customerRepository.read(username);


        listOfTowns = addressRepository.getAll()
                .stream().map(Address::getTown).distinct()
                .sorted().collect(Collectors.toList());

        System.out.println("Choose town:");
        for (int i = 0; i < listOfTowns.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, listOfTowns.get(i));
        }

        while (true) {
            try {
                int townId;

                if ((townId = Integer.parseInt(reader.readLine())) > listOfTowns.size() || townId <= 0) {
                    throw new InvalidOptionException();
                }

                town = listOfTowns.get(townId - 1);
                break;
            } catch (InvalidOptionException e) {
                System.out.println(e.getMessage());
            }
        }

        listOfCars = carRepository.getAll(town, "town").stream().filter(Car::getWhetherIsFree).collect(Collectors.toList());
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

                break;
            } catch (InvalidOptionException e) {
                System.out.println(e.getMessage());
            }
        }

        Car car = listOfCars.get(carId - 1);
        carRepository.updateField(car.getId(), "0", "is_free");

        System.out.println("Press \"ENTER\" to finish driving");
        long timeDriven = getDrivenTime();

        String startTime = getCurrentTime();
        String currDate = getCurrentDate();

        System.out.println("Would you like to use coupon?");

        boolean couponUsed = false;

        if (reader.readLine().equals("yes")) {
            boolean hasCoupon = couponRepository.checkIfFieldValueExists(String.valueOf(customer.getId()), "customer_id");
            if (!hasCoupon) {
                System.out.println("You don't have any coupons");
                price = 0.5 * timeDriven;
            } else {
                couponUsed = true;
                couponRepository.delete(String.valueOf(customer.getId()));
                price = 0;
            }
        } else {
            price = 0.5 * timeDriven;
        }


        int initialAddressId = car.getAddressId();
        int endAddressId = getRandomAddress(town);

        updateCarState(carRepository, car, timeDriven, endAddressId);

        String duration = convertTime(timeDriven * 1000);
        routeRepository.create(new Route("customer", car.getId(), town, currDate, initialAddressId, endAddressId, startTime, duration));
        int routeId = routeRepository.getAll().get(routeRepository.getAll().size() - 1).getId();

        insertIntoCustomersMappingTable(routeId, customer.getId(), price, couponUsed);

        BankAccount bankAccount = bankAccountRepository.read(String.valueOf(customer.getId()));

        double newAmount;
        if (bankAccount.getAmount() - price < 0) {
            newAmount = 0;
        } else {
            newAmount = bankAccount.getAmount() - price;
        }

        bankAccountRepository.updateField(bankAccount.getId(), String.valueOf(newAmount), "amount");

        printInvoice(startTime, duration, initialAddressId, endAddressId, price, car);
    }

    private void showAllRoutes() throws IOException, SQLException {
        String username;
        String password;
        int customerId;
        List<Route> routes;

        while (true) {
            try {
                System.out.print("Type your username: ");
                username = reader.readLine();
                System.out.print("Type your password: ");
                password = reader.readLine();

                if (!customerRepository.validateData(username, password)) {
                    throw new InvalidFieldException();
                }

                break;
            } catch (InvalidFieldException e) {
                System.out.printf(e.getMessage(), "username or password");
            }
        }
        System.out.println();

        customerId = customerRepository.getValueId(username);
        routes = getCustomerRoutes(customerId, routeRepository);

        if (routes.isEmpty()) {
            System.out.println("You have no routes");
            return;
        }

        printRoutes(routes);
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

    private void insertIntoCustomersMappingTable(int routeId, int customerId, double price, boolean couponUsed) throws SQLException {
        String queryString = "" +
                "INSERT INTO customers_routes(route_id, customer_id, price, coupon_used) " +
                "VALUES(?, ?, ?, ?)";

        PreparedStatement query = this.connection.prepareStatement(queryString);
        query.setInt(1, routeId);
        query.setInt(2, customerId);
        query.setDouble(3, price);
        query.setBoolean(4, couponUsed);

        query.executeUpdate();
    }

    private void printInvoice(String startTime, String duration, int initialAddressId, int endAddressId, double price, Car car) throws SQLException {
        System.out.println("Invoice:");
        System.out.printf("Begin time - %s;\n", startTime);
        System.out.printf("Duration - %s;\n", duration);
        System.out.printf("Initial address - %s\n", addressRepository.getAll(String.valueOf(initialAddressId), "id").get(0).getName());
        System.out.printf("End address - %s\n", addressRepository.getAll(String.valueOf(endAddressId), "id").get(0).getName());
        System.out.printf("Price - %.2f\n", price);
        System.out.printf("Car - %s %s\n", car.getBrand(), car.getModel());
    }

    private List<Route> getCustomerRoutes(int customerId, DataRepository<Route> routeRepository) throws SQLException {
        List<Integer> routesIds = new ArrayList<>();
        List<Route> routes = new ArrayList<>();
        String queryString = String.format("" +
                "SELECT route_id " +
                "FROM customers_routes " +
                "WHERE customer_id = %d", customerId);

        PreparedStatement query = this.connection.prepareStatement(queryString);
        ResultSet rs = query.executeQuery();

        while (rs.next()) {
            routesIds.add(rs.getInt("route_id"));
        }

        for (Integer routesId : routesIds) {
            routes.add(routeRepository.getAll(String.valueOf(routesId), "id").get(0));
        }

        return routes;
    }

    private void printRoutes(List<Route> routes) throws SQLException {
        System.out.println("Your routes:");
        for (Route route : routes) {
            Address initialAddress = addressRepository.getAll().get(route.getInitialAddressId() - 1);
            Address endAddress = addressRepository.getAll().get(route.getEndAddressId() - 1);
            Car car = carRepository.getAll().stream().filter(c -> c.getId() == route.getCarId()).collect(Collectors.toList()).get(0);
            System.out.printf("Initial address: %s, End address: %s, Town: %s, Car: %s %s\n",
                    initialAddress.getName(), endAddress.getName(),
                    initialAddress.getTown(), car.getBrand(), car.getModel());
        }
    }
}
