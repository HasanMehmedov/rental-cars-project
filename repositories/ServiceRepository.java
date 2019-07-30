package repositories;

import entities.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ServiceRepository extends DataRepository<Service> {

    public ServiceRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected List<String> getValidationColumns() {
        return List.of("type", "car_id");
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
    protected Service parseRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String problemType = rs.getString("type");
        int carId = rs.getInt("car_id");

        return new Service(id, problemType, carId);
    }

    @Override
    public int getValueId(String carId) throws SQLException {
        String queryString = "" +
                "SELECT id " +
                "FROM services " +
                "WHERE car_id = ?";

        PreparedStatement query = this.connection.prepareStatement(queryString);
        query.setString(1, carId);
        ResultSet rs = query.executeQuery();
        rs.next();

        return rs.getInt("id");
    }

    @Override
    protected String getColumns() {
        return "type, car_id";
    }

    @Override
    protected String getTableName() {
        return "services";
    }

    @Override
    protected List<String> getValues(Service element) {
        return List.of(element.getProblemType(), String.valueOf(element.getCarId()));
    }
}
