// Child class: SavingsAccount — inherits from Account
// Has a minimum balance rule
public class SavingsAccount extends Account {

    private static final double MIN_BALANCE = 500.0; // minimum balance rule
	
    public SavingsAccount(String accountId, String holderName, double balance, int pin) {
        super(accountId, holderName, balance, pin);
    }

    // Override withdraw to enforce minimum balance rule
    @Override
    public synchronized void withdraw(double amount)
            throws InsufficientBalanceException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if ((balance - amount) < MIN_BALANCE) {
            throw new InsufficientBalanceException(
                "Cannot withdraw! Must keep minimum balance of Rs." + MIN_BALANCE);
        }
        balance -= amount;
        System.out.println("Withdrawn Rs." + amount + " | New Balance: Rs." + balance);
    }

    // Override display to show account type
    @Override
    public void displayDetails() {
        System.out.println("--- Savings Account ---");
        super.displayDetails();
        System.out.println("Min Balance : Rs." + MIN_BALANCE);
    }

    // Override toCSV to mark account type
    @Override
    public String toCSV() {
        return accountId + "," + holderName + "," + balance + ",SAVINGS";
    }
}
