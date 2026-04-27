public class Transaction {

    private String type;
    private double amount;
    private int accountId;
    private String date;

    public Transaction(String type, double amount, int accountId, String date) {
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
        this.date = date;
    }

    public String toCSV() {
        return accountId + "," + type + "," + amount + "," + date;
    }

    @Override
    public String toString() {
        return "[" + date + "] " + type + " of Rs." + amount + " on Account: " + accountId;
    }

    public int getAccountId() { return accountId; }
    public String getType()      { return type; }
    public double getAmount()    { return amount; }
}
