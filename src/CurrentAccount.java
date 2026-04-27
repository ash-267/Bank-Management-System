// Child class: CurrentAccount — inherits from Account
// Allows overdraft up to a limit
public class CurrentAccount extends Account {

    private static final double OVERDRAFT_LIMIT = 2000.0;

    public CurrentAccount(String accountId, String holderName, double balance, int pin) {
        super(accountId, holderName, balance, pin);
    }

    // Override withdraw to allow overdraft up to OVERDRAFT_LIMIT
    @Override
    public synchronized void withdraw(double amount)
            throws InsufficientBalanceException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if ((balance - amount) < -OVERDRAFT_LIMIT) {
            throw new InsufficientBalanceException(
                "Overdraft limit of Rs." + OVERDRAFT_LIMIT + " exceeded!");
        }
        balance -= amount;
        System.out.println("Withdrawn Rs." + amount + " | New Balance: Rs." + balance);
    }

    // Override display to show account type
    @Override
    public void displayDetails() {
        System.out.println("--- Current Account ---");
        super.displayDetails();
        System.out.println("Overdraft Limit: Rs." + OVERDRAFT_LIMIT);
    }

    // Override toCSV to mark account type
    @Override
    public String toCSV() {
        return accountId + "," + holderName + "," + balance + ",CURRENT";
    }
}
