package repositories;

import entities.Car;
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
import static constants.Constants.CAR_REGISTRATION_NUMBER_REGEX;

public class CarRepository extends DataRepository<Car> {
    public CarRepository(Connection connection) {
        super(connection);
    }


    @Override
    protected List<String> getValidationColumns() {
        return List.of("reg_number");
    }

    @Override
    protected List<String> getUpdateColumns() {
        return List.of("reg_number");
    }

    @Override
    protected List<String> getUpdateValues() throws IOException, SQLException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String regNumber;

        System.out.println("Type the new values (Let them blank for no changes):");

        while(true) {
            System.out.print("New registration number: ");
            try {
                if(!(regNumber = reader.readLine()).matches(CAR_REGISTRATION_NUMBER_REGEX) &&
                        !regNumber.matches(BLANK_VALUE_REGEX)) {
                    throw new InvalidFieldException();
                }else if(super.checkIfFieldValueExists(regNumber, "reg_number")){
                    throw new AlreadyTakenException();
                }else if(regNumber.matches(BLANK_VALUE_REGEX) || regNumber.length() == 0){
                    regNumber = "";
                }

                break;
            }catch (InvalidFieldException | AlreadyTakenException e){
                System.out.printf(e.getMessage(), "registration number");
            }
        }

        return List.of(regNumber);
    }

    @Override
    protected Car parseRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String brand = rs.getString("brand");
        String model = rs.getString("model");
        String regNumber = rs.getString("reg_number");
        int year = rs.getInt("year");
        int mileage = rs.getInt("mileage");
        int batteryLevel = rs.getInt("battery_level");
        boolean isFree = rs.getBoolean("is_free");
        String town = rs.getString("town");
        int addressId = rs.getInt("address_id");

        return new Car(id, brand, model, regNumber, year, mileage,
                batteryLevel, isFree, town, addressId);
    }

    @Override
    public int getValueId(String regNumber) throws SQLException {
        String queryString = "" +
                "SELECT id " +
                "FROM cars " +
                "WHERE reg_number = ?";

        PreparedStatement query = super.connection.prepareStatement(queryString);
        query.setString(1, regNumber);
        ResultSet rs = query.executeQuery();
        rs.next();

        return rs.getInt("id");
    }

    @Override
    protected String getColumns() {
        return "brand, model, reg_number, year, mileage, battery_level, is_free, town, address_id";
    }

    @Override
    protected String getTableName() {
        return "cars";
    }

    @Override
    protected List<String> getValues(Car element) {
        return List.of(element.getBrand(), element.getModel(), element.getRegNumber(),
                String.valueOf(element.getYear()), String.valueOf(element.getMileage()),
                String.valueOf(element.getBatteryLevel()), element.getWhetherIsFree() ? "1" : "0",
                element.getTown(), String.valueOf(element.getAddressId()));
    }
}
