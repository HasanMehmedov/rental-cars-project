package exceptions;

public class AlreadyTakenException extends Exception {
    @Override
    public String getMessage(){
        return "The %s is already taken\n";
    }
}
