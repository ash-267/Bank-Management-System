// Transaction: Stores details of each banking transaction
public class Transaction {

    private String type;       // "DEPOSIT", "WITHDRAW", or "TRANSFER"
    private double amount;
    private String accountId;
    private String date;

    public Transaction(String type, double amount, String accountId, String date) {
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
        this.date = date;
    }

    // Returns a simple CSV line for file storage
    public String toCSV() {
        return accountId + "," + type + "," + amount + "," + date;
    }

    // Returns a readable summary for display
    @Override
    public String toString() {
        return "[" + date + "] " + type + " of Rs." + amount + " on Account: " + accountId;
    }

    // Getters
    public String getAccountId() { return accountId; }
    public String getType()      { return type; }
    public double getAmount()    { return amount; }
}
