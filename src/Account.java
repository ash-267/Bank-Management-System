public class Account {

    protected int accountId;
    protected static int SaccountNum = 1000;
    protected static int CaccountNum = 2000;
    protected String holderName;
    protected double balance;
    protected int accountPin;
    
    public int createAccoundID (String Type) {
    	if (Type.equals("SAVINGS")) {
    		SaccountNum++;
    		return SaccountNum;
    	} else if (Type.equals("CURRENT")) {
    		CaccountNum++;
    		return CaccountNum;
    	} else {
    		return 0;
    	}
    }
    
    public void updateSanCan (int ID, String type) {
    	if (type.equals("SAVINGS") && (ID > SaccountNum)) {
    		SaccountNum = ID;
    	} else if (type.equals("CURRENT") && (ID > CaccountNum)) {
    		CaccountNum = ID;
    	}
    }

    public Account(String holderName, String Type, double balance, int pin) {
        this.accountId  = createAccoundID(Type);
        this.holderName = holderName;
        this.balance    = balance;
        this.accountPin = pin;
    }
    
    public Account(int ID , String holderName, String Type, double balance, int pin) {
        this.accountId  = ID;
        updateSanCan(ID, Type);
        this.holderName = holderName;
        this.balance    = balance;
        this.accountPin = pin;
    }

    public synchronized void deposit(double amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        System.out.println("Deposited Rs." + amount + " | New Balance: Rs." + balance);
    }

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

    public void displayDetails() {
        System.out.println("Account ID : " + accountId);
        System.out.println("Holder     : " + holderName);
        System.out.println("Balance    : Rs." + balance);
    }

    public String toCSV() {
        return accountId + "," + holderName + "," + balance + "," + accountPin + ",BASE";
    }

    public int getAccountId() { 
    	return accountId; 
    }
    
    public String getHolderName() { 
    	return holderName; 
    }
    
    public double getBalance() { 
    	return balance; 
    }
    
    public int getPin() {
    	return accountPin;
    }
}
