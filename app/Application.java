package app;

import java.io.IOException;
import java.sql.*;

public class Application {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/cars_db?useTimezone=true&serverTimezone=UTC";

    public static void main(String[] args) throws SQLException, IOException, NoSuchFieldException {
        Connection connection = getConnection();

        Runnable engine = new Engine(connection);
        engine.run();
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING, "root", "");
    }
}
