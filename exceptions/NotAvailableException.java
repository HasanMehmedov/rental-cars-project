package exceptions;

public class NotAvailableException extends Exception {
    @Override
    public String getMessage(){
        return "There's someone else with this %s\n";
    }
}
