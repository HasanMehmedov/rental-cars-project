package repositories;

import entities.Coupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CouponRepository extends DataRepository<Coupon> {
    public CouponRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected List<String> getValidationColumns() {
        return List.of("customer_id");
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
    protected Coupon parseRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int customerId = rs.getInt("customer_id");

        return new Coupon(id, customerId);
    }

    @Override
    public int getValueId(String customerId) throws SQLException {
        String queryString = "" +
                "SELECT id " +
                "FROM coupons " +
                "WHERE customer_id = ? " +
                "LIMIT 1";

        PreparedStatement query = this.connection.prepareStatement(queryString);
        query.setString(1, customerId);
        ResultSet rs = query.executeQuery();
        rs.next();

        return rs.getInt("id");
    }

    @Override
    protected String getColumns() {
        return "customer_id";
    }

    @Override
    protected String getTableName() {
        return "coupons";
    }

    @Override
    protected List<String> getValues(Coupon element) {
        return List.of(String.valueOf(element.getCustomerId()));
    }
}
