package repositories;

import entities.Branch;
import exceptions.AlreadyTakenException;
import exceptions.InvalidFieldException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static constants.Constants.BLANK_VALUE_REGEX;
import static constants.Constants.NAME_REGEX;

public class BranchRepository extends DataRepository<Branch> {
    public BranchRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected List<String> getValidationColumns() {
        return List.of("name");
    }

    @Override
    protected List<String> getUpdateColumns() {
        return List.of("name", "address");
    }

    @Override
    protected List<String> getUpdateValues() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String name;
        String address;

        System.out.println("Type the new values (Let them blank for no changes):");

        while(true) {
            try {
                System.out.print("New branch name: ");
                if ((name = reader.readLine()).length() == 0 ||
                        name.matches(BLANK_VALUE_REGEX)) {
                    name = "";
                } else if (!name.matches(NAME_REGEX)) {
                    throw new InvalidFieldException();
                } else if (super.checkIfFieldValueExists(name, "name")) {
                    throw new AlreadyTakenException();
                }

                break;
            } catch (InvalidFieldException | AlreadyTakenException e) {
                System.out.printf(e.getMessage(), "branch name");
            }
        }

        while(true) {
            try {
                System.out.print("New address: ");
                if ((address = reader.readLine()).length() == 0 ||
                        address.matches(BLANK_VALUE_REGEX)) {
                    address = "";
                } else if (super.checkIfFieldValueExists(address, "address")) {
                    throw new AlreadyTakenException();
                }

                break;
            } catch (AlreadyTakenException e) {
                System.out.printf(e.getMessage(), "address");
            }
        }

        return List.of(name, address);
    }

    @Override
    protected Branch parseRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String address = rs.getString("address");
        String town = rs.getString("town");

        return new Branch(id, name, address, town);
    }

    @Override
    public int getValueId(String name) throws SQLException {
        String queryString = "" +
                "SELECT id " +
                "FROM branches " +
                "WHERE name = ?";

        PreparedStatement query = super.connection.prepareStatement(queryString);
        query.setString(1, name);
        ResultSet rs = query.executeQuery();
        rs.next();

        return rs.getInt("id");
    }

    @Override
    protected String getColumns() {
        return "name, address, town";
    }

    @Override
    protected String getTableName() {
        return "branches";
    }

    @Override
    protected List<String> getValues(Branch element) {
        return List.of(element.getName(), element.getAddress(), element.getTown());
    }
}
