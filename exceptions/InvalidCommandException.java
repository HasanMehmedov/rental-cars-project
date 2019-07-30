package exceptions;

public class InvalidCommandException extends Exception {
    @Override
    public String getMessage(){
        return "Invalid command!";
    }
}
