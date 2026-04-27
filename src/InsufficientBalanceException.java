// Custom Exception: Thrown when withdrawal amount exceeds account balance
public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
