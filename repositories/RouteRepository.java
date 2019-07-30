package repositories;

import entities.Route;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RouteRepository extends DataRepository<Route> {
    public RouteRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected List<String> getValidationColumns() {
        return List.of("date", "start_time");
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
    protected Route parseRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String user = rs.getString("user");
        int carId = rs.getInt("car_id");
        String town = rs.getString("town");
        String date = rs.getString("date");
        int initialAddressId = rs.getInt("initial_address_id");
        int endAddressId = rs.getInt("end_address_id");
        String startTime = rs.getString("start_time");
        String duration = rs.getString("duration");

        return new Route(id, user, carId,town, date, initialAddressId,
                        endAddressId, startTime, duration);
    }

    @Override
    public int getValueId(String time) throws SQLException {
        String queryString = "" +
                "SELECT id " +
                "FROM routes " +
                "WHERE start_time = ?";

        PreparedStatement query = this.connection.prepareStatement(queryString);
        query.setString(1, time);
        ResultSet rs = query.executeQuery();
        rs.next();

        return rs.getInt("id");
    }

    @Override
    protected String getColumns() {
        return "user, car_id, town, date, initial_address_id, " +
                "end_address_id, start_time, duration";
    }

    @Override
    protected String getTableName() {
        return "routes";
    }

    @Override
    protected List<String> getValues(Route element) {
        return List.of(element.getUser(), String.valueOf(element.getCarId()),
                element.getTown(), element.getDate(), String.valueOf(element.getInitialAddressId()),
                String.valueOf(element.getEndAddressId()), element.getStartTime(), String.valueOf(element.getDuration()));
    }
}
