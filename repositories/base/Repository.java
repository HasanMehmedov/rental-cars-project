package repositories.base;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface Repository<T> {
    void create(T element) throws SQLException;
    T read(String data) throws SQLException;
    void update(String data) throws SQLException, IOException;
    void delete(String data) throws SQLException;
    List<T> getAll() throws SQLException;
    List<T> getAll(String data, String fieldName) throws SQLException;


}
