package repositories;

import entities.BankAccount;
import exceptions.InvalidFieldException;
import exceptions.NotAvailableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static constants.Constants.BLANK_VALUE_REGEX;
import static constants.Constants.CREDIT_CARD_REGEX;

public class BankAccountRepository extends DataRepository<BankAccount> {
    public BankAccountRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected List<String> getValidationColumns() {
        return List.of("credit_card_id");
    }

    @Override
    protected List<String> getUpdateColumns() {
        return List.of("credit_card_id");
    }

    @Override
    protected List<String> getUpdateValues() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String creditCardId;

        while(true){
            try {
                System.out.print("New credit card ID: ");
                if ((creditCardId = reader.readLine()).length() == 0 ||
                        creditCardId.matches(BLANK_VALUE_REGEX)) {
                    creditCardId = "";
                } else if (!creditCardId.matches(CREDIT_CARD_REGEX)) {
                    throw new InvalidFieldException();
                } else if (super.checkIfFieldValueExists(creditCardId, "credit_card_id")) {
                    throw new NotAvailableException();
                }

                break;
            } catch (InvalidFieldException | NotAvailableException e) {
                System.out.printf(e.getMessage(), "credit card ID");
            }
        }

        return List.of(creditCardId);
    }

    @Override
    protected BankAccount parseRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String creditCardId = rs.getString("credit_card_id");
        double amount = rs.getDouble("amount");
        int customerId = rs.getInt("customer_id");

        return new BankAccount(id, creditCardId, amount, customerId);
    }

    @Override
    public int getValueId(String customerId) throws SQLException {
        String queryString = String.format("" +
                "SELECT id " +
                "FROM bank_accounts " +
                "WHERE customer_id = %s", customerId);

        PreparedStatement query = super.connection.prepareStatement(queryString);
        ResultSet rs = query.executeQuery();
        rs.next();

        return rs.getInt("id");
    }

    @Override
    protected String getColumns() {
        return "credit_card_id, amount, customer_id";
    }

    @Override
    protected String getTableName() {
        return "bank_accounts";
    }

    @Override
    protected List<String> getValues(BankAccount element) {
        return List.of(element.getCreditCardId(), String.valueOf(element.getAmount()),
                        String.valueOf(element.getCustomerId()));
    }
}
