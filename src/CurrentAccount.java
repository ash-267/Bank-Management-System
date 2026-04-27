public class CurrentAccount extends Account {

    private static final double OVERDRAFT_LIMIT = 2000.0;

    public CurrentAccount(String holderName, String type, double balance, int pin) {
        super(holderName, type, balance, pin);
    }
    
    public CurrentAccount(int ID, String holderName, String Type, double balance, int pin) {
        super(ID, holderName, Type, balance, pin);
    }

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

    @Override
    public void displayDetails() {
        System.out.println("--- Current Account ---");
        super.displayDetails();
        System.out.println("Overdraft Limit: Rs." + OVERDRAFT_LIMIT);
    }

    @Override
    public String toCSV() {
        return accountId + "," + holderName + "," + balance + "," + accountPin + ",CURRENT";
    }
}
