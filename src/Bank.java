import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Bank {

    private List<Account>     accounts     = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    private static final String ACCOUNTS_FILE    = "accounts.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    public void createAccount(String name, double balance, String type, int pin) {
        Account account;
        if (type.equalsIgnoreCase("SAVINGS")) {
            account = new SavingsAccount(name, type, balance, pin);
        } else {
            account = new CurrentAccount(name, type, balance, pin);
        }
        accounts.add(account);
        saveAccountsToFile();
        System.out.println("Account created successfully! ID: " + account.accountId);
    }

    public void deposit(int id, double amount, int pin) {
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

    public void withdraw(int id, double amount, int pin) {
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

    public void transfer(int fromId, int toId, double amount, int pin) {
        Account from = findAccount(fromId);
        Account to   = findAccount(toId);

        if (from == null || to == null) {
            System.out.println("Error: One or both accounts not found.");
            return;
        }        
        if (pin != from.accountPin) {
        	System.out.println("Incorrect Pin!");
    		return;
        }

        //Thread 1 where withdrawal from from sender account takes place
        Thread withdrawThread = new Thread(() -> {
            try {
                System.out.println("Thread-1: Withdrawing Rs." + amount + " from " + fromId + "...");
                from.withdraw(amount);
                recordTransaction("TRANSFER-OUT", amount, fromId);
            } catch (InsufficientBalanceException | IllegalArgumentException e) {
                System.out.println("Transfer Error: " + e.getMessage());
            }
        });

        //Thread 2 where the amount is deposited into receiver account
        Thread depositThread = new Thread(() -> {
            try {
                System.out.println("Thread-2: Depositing Rs." + amount + " into " + toId + "...");
                to.deposit(amount);
                recordTransaction("TRANSFER-IN", amount, toId);
            } catch (IllegalArgumentException e) {
                System.out.println("Transfer Error: " + e.getMessage());
            }
        });

        withdrawThread.start();
        try {
            withdrawThread.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
            return;
        }
        depositThread.start();
        try {
            depositThread.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
            return;
        }

        System.out.println("Transfer successful!");
    }

    public void viewAccount(int id, int pin) {
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

    public void viewTransactions(int id, int pin) {
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
                    if (t.getAccountId() == account.accountId) {
                        System.out.println(t);
                        found = true;
                    }
                }
                if (!found) System.out.println("No transactions found.");
        	}
        }
    }

    private Account findAccount(int id) {
        for (Account a : accounts) {
            if (a.getAccountId() == id) {
            	return a;
            }
        }
        return null;
    }

    private void recordTransaction(String type, double amount, int id) {
        String date = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Transaction t = new Transaction(type, amount, id, date);
        transactions.add(t);
        saveTransactionToFile(t);
        saveAccountsToFile(); // update balance in file
    }

    public void saveAccountsToFile() {
        try (FileWriter fw = new FileWriter(ACCOUNTS_FILE)) {
            for (Account a : accounts) {
                fw.write(a.toCSV() + "\n");
            }
        } catch (IOException e) {
            System.out.println("File Error (accounts): " + e.getMessage());
        }
    }

    private void saveTransactionToFile(Transaction t) {
        try (FileWriter fw = new FileWriter(TRANSACTIONS_FILE, true)) { // append=true
            fw.write(t.toCSV() + "\n");
        } catch (IOException e) {
            System.out.println("File Error (transactions): " + e.getMessage());
        }
    }

    public void loadAccountsFromFile() {
        File file = new File(ACCOUNTS_FILE);
        if (!file.exists()) return; // no file yet, skip

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                int id         = Integer.parseInt(parts[0]);
                String name    = parts[1];
                double balance = Double.parseDouble(parts[2]);
                int pin        = Integer.parseInt(parts[3]);
                String type    = parts[4];

                if (type.equals("SAVINGS")) {
                    accounts.add(new SavingsAccount(id, name, type, balance, pin));
                } else {
                    accounts.add(new CurrentAccount(id, name, type, balance, pin));
                }
            }
            System.out.println("Accounts loaded from file.");
        } catch (IOException e) {
            System.out.println("File Error (load): " + e.getMessage());
        }
    }
}
