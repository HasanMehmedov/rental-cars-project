package exceptions;

public class NoSuchElementException extends Exception {
    @Override
    public String getMessage() {
        return "The %s does not exist!";
    }
}
