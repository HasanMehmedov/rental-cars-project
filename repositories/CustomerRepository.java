package repositories;

import entities.Customer;
import exceptions.AlreadyTakenException;
import exceptions.InvalidFieldException;
import exceptions.NotAvailableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static constants.Constants.*;

public class CustomerRepository extends DataRepository<Customer> {

    public CustomerRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected List<String> getValidationColumns() {
        return List.of("username", "password");
    }

    @Override
    protected List<String> getUpdateColumns(){
        return List.of("username", "password", "phone_number", "email");
    }

    @Override
    protected List<String> getUpdateValues() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String username;
        String password;
        String phoneNumber;
        String email;

        System.out.println("Type the new values (Let them blank for no changes):");

        while(true) {
            try {
                System.out.print("New username (Number of characters must be from 8 to 20): ");
                if ((username = reader.readLine()).length() == 0 ||
                        username.matches(BLANK_VALUE_REGEX)) {
                    username = "";
                } else if (!username.matches(USERNAME_REGEX)) {
                    throw new InvalidFieldException();
                } else if (super.checkIfFieldValueExists(username, "username")) {
                    throw new AlreadyTakenException();
                }

                break;
            } catch (InvalidFieldException | AlreadyTakenException e) {
                System.out.printf(e.getMessage(), "username");
            }
        }

        while(true) {
            try {
                System.out.print("New password (Minimum 8 characters and it must contain at least ont letter and onr character): ");
                if ((password = reader.readLine()).length() == 0 ||
                        password.matches(BLANK_VALUE_REGEX)) {
                    password = "";
                } else if (!password.matches(PASSWORD_REGEX)) {
                    throw new InvalidFieldException();
                } else if (super.checkIfFieldValueExists(password, "password")) {
                    throw new AlreadyTakenException();
                }

                break;
            } catch (InvalidFieldException | AlreadyTakenException e) {
                System.out.printf(e.getMessage(), "password");
            }
        }

        while(true) {
            try {
                System.out.print("New phone number: ");
                if ((phoneNumber = reader.readLine()).length() == 0 ||
                        phoneNumber.matches(BLANK_VALUE_REGEX)) {
                    phoneNumber = "";
                } else if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
                    throw new InvalidFieldException();
                } else if (super.checkIfFieldValueExists(phoneNumber, "phone_number")) {
                    throw new NotAvailableException();
                }

                break;
            } catch (InvalidFieldException | NotAvailableException e) {
                System.out.printf(e.getMessage(), "phone number");
            }
        }


        while(true) {
            try {
                System.out.print("New email: ");
                if ((email = reader.readLine()).length() == 0 ||
                        email.matches(BLANK_VALUE_REGEX)) {
                    email = "";
                } else if (!email.matches(EMAIL_REGEX)) {
                    throw new InvalidFieldException();
                } else if (super.checkIfFieldValueExists(email, "email")) {
                    throw new NotAvailableException();
                }

                break;
            } catch (InvalidFieldException | NotAvailableException e) {
                System.out.printf(e.getMessage(), "email address");
            }
        }

        return List.of(username, password, phoneNumber, email);
    }

    @Override
    protected Customer parseRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String egn = rs.getString("egn");
        String phoneNumber = rs.getString("phone_number");
        String email = rs.getString("email");

        return new Customer(id, username, password, firstName, lastName,
                            egn, phoneNumber, email);
    }

    @Override
    public int getValueId(String username) throws SQLException {
        String queryString = "" +
                "SELECT id " +
                "FROM customers " +
                "WHERE username = ?";

        PreparedStatement query = super.connection.prepareStatement(queryString);
        query.setString(1, username);
        ResultSet rs = query.executeQuery();
        rs.next();

        return rs.getInt("id");
    }

    @Override
    protected String getColumns() {
        return "username, password, first_name, last_name, egn, " +
                "phone_number, email";
    }

    @Override
    protected String getTableName() {
        return "customers";
    }

    @Override
    protected List<String> getValues(Customer element) {

        return List.of(element.getUsername(), element.getPassword(),
                element.getFirstName(), element.getLastName(), element.getEgn(),
                element.getPhoneNumber(), element.getEmail());
    }
}