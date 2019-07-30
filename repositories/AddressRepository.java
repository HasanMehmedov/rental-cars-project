package repositories;

import entities.Address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AddressRepository extends DataRepository<Address> {
    public AddressRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected List<String> getValidationColumns() {
        return List.of("name", "town");
    }

    @Override
    @Deprecated
    protected List<String> getUpdateColumns() {
        return null;
    }

    @Override
    @Deprecated
    protected List<String> getUpdateValues() {
        return null;
    }

    @Override
    protected Address parseRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String town = rs.getString("town");

        return new Address(id, name, town);
    }

    @Override
    public int getValueId(String addressName) throws SQLException {
        String queryString = "" +
                "SELECT id " +
                "FROM addresses " +
                "WHERE name = ?";

        PreparedStatement query = super.connection.prepareStatement(queryString);
        query.setString(1, addressName);
        ResultSet rs = query.executeQuery();
        rs.next();

        return rs.getInt("id");
    }

    @Override
    protected String getColumns() {
        return "name, town";
    }

    @Override
    protected String getTableName() {
        return "addresses";
    }

    @Override
    protected List<String> getValues(Address element) {
        return List.of(element.getName(), element.getTown());
    }
}
