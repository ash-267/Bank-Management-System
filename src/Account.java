// Base class: Account — parent of SavingsAccount and CurrentAccount
public class Account {

    protected String accountId;
    protected String holderName;
    protected double balance;
    protected int accountPin;

    public Account(String accountId, String holderName, double balance, int pin) {
        this.accountId  = accountId;
        this.holderName = holderName;
        this.balance    = balance;
        this.accountPin = pin;
    }

    // Deposit money into this account
    public synchronized void deposit(double amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        System.out.println("Deposited Rs." + amount + " | New Balance: Rs." + balance);
    }

    // Withdraw money from this account
    public synchronized void withdraw(double amount)
            throws InsufficientBalanceException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new InsufficientBalanceException(
                "Insufficient balance! Available: Rs." + balance);
        }
        balance -= amount;
        System.out.println("Withdrawn Rs." + amount + " | New Balance: Rs." + balance);
    }

    // Display account details (overridden in child classes)
    public void displayDetails() {
        System.out.println("Account ID : " + accountId);
        System.out.println("Holder     : " + holderName);
        System.out.println("Balance    : Rs." + balance);
    }

    // Returns a CSV line for saving to accounts.txt
    public String toCSV() {
        return accountId + "," + holderName + "," + balance + ",BASE";
    }

    // Getters
    public String getAccountId()  { return accountId; }
    public String getHolderName() { return holderName; }
    public double getBalance()    { return balance; }
}
