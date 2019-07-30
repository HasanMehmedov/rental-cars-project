package exceptions;

public class InvalidOptionException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid option";
    }
}
