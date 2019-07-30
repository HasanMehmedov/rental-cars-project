package repositories;

import entities.Problem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProblemRepository extends DataRepository<Problem> {
    public ProblemRepository(Connection connection) {
        super(connection);
    }

    @Override
    public void delete(String problemId) throws SQLException{
        String queryString = String.format("" +
                "DELETE FROM %s " +
                "WHERE id = ?", getTableName());

        PreparedStatement query = this.connection.prepareStatement(queryString);
        query.setString(1, problemId);
        query.executeUpdate();
    }

    @Override
    protected List<String> getValidationColumns() {
        return List.of("car_id", "type");
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
    protected Problem parseRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int carId = rs.getInt("car_id");
        String type = rs.getString("type");

        return new Problem(id, carId, type);
    }

    @Override
    public int getValueId(String carId) throws SQLException {
        String queryString = "" +
                "SELECT id " +
                "FROM problems " +
                "WHERE car_id = ?";

        PreparedStatement query = this.connection.prepareStatement(queryString);
        query.setString(1, carId);
        ResultSet rs = query.executeQuery();
        rs.next();

        return rs.getInt("id");
    }

    @Override
    protected String getColumns() {
        return "car_id, type";
    }

    @Override
    protected String getTableName() {
        return "problems";
    }

    @Override
    protected List<String> getValues(Problem element) {
        return List.of(String.valueOf(element.getCarId()), element.getType());
    }
}
