package exceptions;

public class InvalidFieldException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid %s!\n";
    }
}
