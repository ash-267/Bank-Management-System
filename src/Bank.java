import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Bank: Manages all accounts and transactions
// Handles file saving and loading
public class Bank {

    private List<Account>     accounts     = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    private static final String ACCOUNTS_FILE    = "accounts.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    // -------------------------------------------------------
    // Create a new account and save it
    // -------------------------------------------------------
    public void createAccount(String id, String name, double balance, String type, int pin) {
        // Check for duplicate account ID
        if (findAccount(id) != null) {
            System.out.println("Error: Account ID already exists.");
            return;
        }

        Account account;
        if (type.equalsIgnoreCase("SAVINGS")) {
            account = new SavingsAccount(id, name, balance, pin);
        } else {
            account = new CurrentAccount(id, name, balance, pin);
        }
        accounts.add(account);
        saveAccountsToFile();
        System.out.println("Account created successfully! ID: " + id);
    }

    // -------------------------------------------------------
    // Deposit money into an account
    // -------------------------------------------------------
    public void deposit(String id, double amount, int pin) {
        Account account = findAccount(id);
        if (account == null) {
            System.out.println("Error: Account not found.");
            return;
        } else {
        	if (pin == account.accountPin) {
        		try {
                    account.deposit(amount);
                    recordTransaction("DEPOSIT", amount, id);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
        	} else {
        		System.out.println("Incorrect Pin!");
        		return;
        	}        	
        }
    }

    // -------------------------------------------------------
    // Withdraw money from an account
    // -------------------------------------------------------
    public void withdraw(String id, double amount, int pin) {
    	Account account = findAccount(id);
    	if (account == null) {
            System.out.println("Error: Account not found.");
            return;
        } else {
        	if (pin != account.accountPin) {
        		System.out.println("Incorrect Pin!");
        		return;
        	} else {
        		try {
                    account.withdraw(amount);
                    recordTransaction("WITHDRAW", amount, id);
                } catch (InsufficientBalanceException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
        	}
        }
        
    }

    // -------------------------------------------------------
    // Transfer money from one account to another
    // Uses two threads (one to withdraw, one to deposit)
    // synchronized keyword ensures thread safety
    // -------------------------------------------------------
    public void transfer(String fromId, String toId, double amount) {
        Account from = findAccount(fromId);
        Account to   = findAccount(toId);

        if (from == null || to == null) {
            System.out.println("Error: One or both accounts not found.");
            return;
        }

        // Thread 1: withdraws from sender account
        Thread withdrawThread = new Thread(() -> {
            try {
                System.out.println("Thread-1: Withdrawing Rs." + amount + " from " + fromId + "...");
                from.withdraw(amount);
                recordTransaction("TRANSFER-OUT", amount, fromId);
            } catch (InsufficientBalanceException | IllegalArgumentException e) {
                System.out.println("Transfer Error: " + e.getMessage());
            }
        });

        // Thread 2: deposits into receiver account
        Thread depositThread = new Thread(() -> {
            try {
                System.out.println("Thread-2: Depositing Rs." + amount + " into " + toId + "...");
                to.deposit(amount);
                recordTransaction("TRANSFER-IN", amount, toId);
            } catch (IllegalArgumentException e) {
                System.out.println("Transfer Error: " + e.getMessage());
            }
        });

        // Run withdraw first, wait for it to finish, then deposit
        withdrawThread.start();
        try {
            withdrawThread.join(); // wait for withdrawal to complete before depositing
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
            return;
        }
        depositThread.start();
        try {
            depositThread.join(); // wait for deposit to complete
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
            return;
        }

        System.out.println("Transfer successful!");
    }

    // -------------------------------------------------------
    // Display account details
    // -------------------------------------------------------
    public void viewAccount(String id, int pin) {
        Account account = findAccount(id);
        if (account == null) {
            System.out.println("Error: Account not found.");
            return;
        } else {
        	if (pin != account.accountPin) {
        		System.out.println("Incorrect Pin!");
        		return;
        	} else {
        		account.displayDetails();
        	}
        }
    }

    // -------------------------------------------------------
    // Display transaction history for an account
    // -------------------------------------------------------
    public void viewTransactions(String id, int pin) {
    	Account account = findAccount(id);
    	if (account == null) {
            System.out.println("Error: Account not found.");
            return;
        } else {
        	if (pin != account.accountPin) {
        		System.out.println("Incorrect Pin!");
        		return;
        	} else {
        		System.out.println("--- Transaction History for " + id + " ---");
                boolean found = false;
                for (Transaction t : transactions) {
                    if (t.getAccountId().equals(id)) {
                        System.out.println(t);
                        found = true;
                    }
                }
                if (!found) System.out.println("No transactions found.");
        	}
        }
    }

    // -------------------------------------------------------
    // Helper: find an account by ID
    // -------------------------------------------------------
    private Account findAccount(String id) {
        for (Account a : accounts) {
            if (a.getAccountId().equals(id)) return a;
        }
        return null;
    }

    // -------------------------------------------------------
    // Helper: record a transaction and save to file
    // -------------------------------------------------------
    private void recordTransaction(String type, double amount, String id) {
        String date = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Transaction t = new Transaction(type, amount, id, date);
        transactions.add(t);
        saveTransactionToFile(t);
        saveAccountsToFile(); // update balance in file
    }

    // -------------------------------------------------------
    // File Handling: Save all accounts to accounts.txt
    // -------------------------------------------------------
    public void saveAccountsToFile() {
        try (FileWriter fw = new FileWriter(ACCOUNTS_FILE)) {
            for (Account a : accounts) {
                fw.write(a.toCSV() + "\n");
            }
        } catch (IOException e) {
            System.out.println("File Error (accounts): " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // File Handling: Append one transaction to transactions.txt
    // -------------------------------------------------------
    private void saveTransactionToFile(Transaction t) {
        try (FileWriter fw = new FileWriter(TRANSACTIONS_FILE, true)) { // append=true
            fw.write(t.toCSV() + "\n");
        } catch (IOException e) {
            System.out.println("File Error (transactions): " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // File Handling: Load accounts from accounts.txt on startup
    // -------------------------------------------------------
    public void loadAccountsFromFile() {
        File file = new File(ACCOUNTS_FILE);
        if (!file.exists()) return; // no file yet, skip

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Expected format: id,name,balance,type
                if (parts.length < 5) continue;

                String id      = parts[0];
                String name    = parts[1];
                double balance = Double.parseDouble(parts[2]);
                String type    = parts[3];
                int pin = Integer.parseInt(parts[4]);

                if (type.equals("SAVINGS")) {
                    accounts.add(new SavingsAccount(id, name, balance, pin));
                } else {
                    accounts.add(new CurrentAccount(id, name, balance, pin));
                }
            }
            System.out.println("Accounts loaded from file.");
        } catch (IOException e) {
            System.out.println("File Error (load): " + e.getMessage());
        }
    }
}
