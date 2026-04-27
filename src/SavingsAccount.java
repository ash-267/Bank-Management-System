public class SavingsAccount extends Account {

    private static final double MIN_BALANCE = 500.0; 
	
    public SavingsAccount(String holderName, String type, double balance, int pin) {
        super(holderName, type, balance, pin);
    }
    
    public SavingsAccount(int ID, String holderName, String Type, double balance, int pin) {
        super(ID, holderName, Type, balance, pin);
    }

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

    @Override
    public void displayDetails() {
        System.out.println("--- Savings Account ---");
        super.displayDetails();
        System.out.println("Min Balance : Rs." + MIN_BALANCE);
    }

    @Override
    public String toCSV() {
        return accountId + "," + holderName + "," + balance + "," + accountPin + ",SAVINGS";
    }
}
