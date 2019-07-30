package exceptions;

public class AmountException extends Exception {
    @Override
    public String getMessage() {
        return "Amount cannot be negative!";
    }
}
