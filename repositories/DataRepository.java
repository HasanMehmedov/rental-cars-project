package repositories;

import repositories.base.Repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DataRepository<T> implements Repository<T> {
    protected Connection connection;

    public DataRepository(Connection connection){
        this.connection = connection;
    }

    @Override
    public void create(T element) throws SQLException {
        List<String> values = getValues(element);
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            params.append("?, ");
        }

        params.delete(params.length() - 2, params.length());

        String queryString = String.format("" +
                "INSERT INTO %s(%s) " +
                "VALUES(%s)", getTableName(), getColumns(), params.toString());

        PreparedStatement query = this.connection.prepareStatement(queryString);
        for (int i = 0; i < values.size(); i++) {
            query.setString(i + 1, values.get(i));
        }

        query.executeUpdate();
    }

    @Override
    public T read(String data) throws SQLException {
        int id = getValueId(data);
        String queryString = String.format("" +
                "SELECT * FROM %s " +
                "WHERE id = %d", getTableName(), id);

        PreparedStatement query = this.connection.prepareStatement(queryString);
        ResultSet rs = query.executeQuery();
        rs.next();

        return parseRow(rs);
    }


    @Override
    public void update(String data) throws SQLException, IOException {
        List<String> updateValues = new ArrayList<>();
        updateValues.addAll(getUpdateValues());
        List<String> updateColumns = new ArrayList<>();
        updateColumns.addAll(getUpdateColumns());
        int id = getValueId(data);

        StringBuilder params = new StringBuilder();

        for (int i = 0; i < updateValues.size(); i++) {
            if(updateValues.get(i).length() == 0){
                updateColumns.remove(i);
                updateValues.remove(i);
                i--;
                continue;
            }

            params.append(String.format("%s = ?, ", updateColumns.get(i)));
        }

        if(updateValues.size() == 0){
            return;
        }
        params.delete(params.length() - 2, params.length());

        String queryString = String.format("" +
                "UPDATE %s " +
                "SET %s " +
                "WHERE id = %d", getTableName(), params.toString(), id);

        PreparedStatement query = this.connection.prepareStatement(queryString);
        for (int i = 0; i < updateValues.size(); i++) {

            query.setString(i + 1, updateValues.get(i));
        }

        query.executeUpdate();
    }

    @Override
    public void delete(String data) throws SQLException{
        int id = getValueId(data);
        String queryString = String.format("" +
                "DELETE FROM %s " +
                "WHERE id = %d", getTableName(), id);

        PreparedStatement query = this.connection.prepareStatement(queryString);
        query.executeUpdate();
    }

    @Override
    public List<T> getAll() throws SQLException {
        String queryString = "SELECT * FROM " + getTableName();
        PreparedStatement query = this.connection.prepareStatement(queryString);

        ResultSet rs = query.executeQuery();
        return toList(rs);
    }

    public List<T> toList(ResultSet rs) throws SQLException {
        List<T> list = new ArrayList<>();
        while (rs.next()){
            T object = this.parseRow(rs);
            list.add(object);
        }

        return list;
    }

    @Override
    public List<T> getAll(String data, String fieldName) throws SQLException {

        String queryString = String.format("" +
                "SELECT * " +
                "FROM %s " +
                "WHERE %s = ?", getTableName(), fieldName);

        PreparedStatement query = this.connection.prepareStatement(queryString);
        query.setString(1, data);

        ResultSet rs = query.executeQuery();
        return toList(rs);
    }

    public boolean validateData(String... data) throws SQLException {
        List<String> validateValues = Arrays.asList(data);
        List<String> validateColumns = getValidationColumns();

        StringBuilder params = new StringBuilder();
        for (int i = 0; i < validateValues.size(); i++) {
            if(validateValues.get(i).matches(".*NULL")){
                params.append(String.format("%s IS %s AND ", validateColumns.get(i), validateValues.get(i)));
                continue;
            }
            params.append(String.format("%s = \'%s\' AND ", validateColumns.get(i), validateValues.get(i)));
        }

        params.delete(params.length() - 5, params.length());

        String queryString = String.format("" +
                "SELECT * " +
                "FROM %s " +
                "WHERE %s", getTableName(), params.toString());

        PreparedStatement query = this.connection.prepareStatement(queryString);

        ResultSet rs = query.executeQuery();

        return rs.next();
    }

    public boolean checkIfFieldValueExists(String data, String fieldName) throws SQLException {
        String queryString = String.format("" +
                "SELECT * " +
                "FROM %s " +
                "WHERE %s = ?", getTableName(), fieldName);

        PreparedStatement query = this.connection.prepareStatement(queryString);
        query.setString(1, data);
        ResultSet rs = query.executeQuery();

        return rs.next();
    }

    public void updateField(int id, String data, String fieldName) throws SQLException {
        String queryString = String.format("" +
                "UPDATE %s " +
                "SET %s = ? " +
                "WHERE id = %d", getTableName(), fieldName, id);

        PreparedStatement query = this.connection.prepareStatement(queryString);
        query.setString(1, data);
        query.executeUpdate();
    }

    protected abstract List<String> getValidationColumns();

    protected abstract List<String> getUpdateColumns();

    protected abstract List<String> getUpdateValues() throws IOException, SQLException;

    protected abstract T parseRow(ResultSet rs) throws SQLException;

    public abstract int getValueId(String data) throws SQLException;

    protected abstract String getColumns();

    protected abstract String getTableName();

    protected abstract List<String> getValues(T element);
}
