import java.util.Scanner;
// import java.io.Console;

public class Main {

    public static void main(String[] args) {
        Bank    bank    = new Bank();
        Scanner scanner = new Scanner(System.in);

        bank.loadAccountsFromFile();

        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter choice: ");

            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n");
                continue;
            }

            switch (choice) {

                //Create Account
                case 1: {
                    System.out.print("Account Type (SAVINGS/CURRENT): ");
                    String type = scanner.nextLine().trim().toUpperCase();
                    if (!type.equals("SAVINGS") && !type.equals("CURRENT")) {
                        System.out.println("Invalid type. Use SAVINGS or CURRENT.\n");
                        break;
                    }
                    System.out.print("Holder Name: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Set an account pin: ");
                    int pInit = scanner.nextInt();
                    System.out.print("Re-enter pin: ");
                    int Pin = scanner.nextInt();
                    scanner.nextLine();
                    if (pInit != Pin) {
                    	System.out.println("The re-enterred pin does not match with the initial pin!");
                    	break;
                    }
                    System.out.print("Initial Balance: ");
                    double initBal = readDouble(scanner);
                    if (initBal < 0) { 
                    	System.out.println("Balance cannot be negative.\n"); 
                    	break; 
                    }
                    bank.createAccount(name, initBal, type, Pin);
                    System.out.println();
                    break;
                }
                //Deposit
                case 2: {
                    System.out.print("Account ID: ");
                    int depId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Pin: ");
                    int pin = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Amount to Deposit: ");
                    double depAmt = readDouble(scanner);
                    bank.deposit(depId, depAmt, pin);
                    System.out.println();
                    break;
            	}
                //Withdraw
                case 3: {
                    System.out.print("Account ID: ");
                    int witId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Pin: ");
                    int pin = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Amount to Withdraw: ");
                    double witAmt = readDouble(scanner);
                    bank.withdraw(witId, witAmt, pin);
                    System.out.println();
                    break;
                }
                //Transfer
                case 4: {
                    System.out.print("From Account ID: ");
                    int fromId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Pin of Sender account: ");
                    int pin = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("To Account ID: ");
                    int toId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Amount to Transfer: ");
                    double transAmt = readDouble(scanner);
                    bank.transfer(fromId, toId, transAmt, pin);
                    System.out.println();
                    break;
                }
                //View Account
                case 5: {
                    System.out.print("Account ID: ");
                    int viewId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Pin: ");
                    int pin = scanner.nextInt();
                    scanner.nextLine();
                    bank.viewAccount(viewId, pin);
                    System.out.println();
                    break;
                }
                //View Transactions
                case 6: {
                    System.out.print("Account ID: ");
                    int txnId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Pin: ");
                    int pin = scanner.nextInt();
                    scanner.nextLine();
                    bank.viewTransactions(txnId, pin);
                    System.out.println();
                    break;
            	}
                //Exit
                case 7: {
                    System.out.println("Thank you for using the Banking System. Goodbye!");
                    running = false;
                    break;
            	}
                default: {
                    System.out.println("Invalid choice. Try 1-7.\n");
                }
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("=== Banking System Menu ===");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. View Account");
        System.out.println("6. View Transactions");
        System.out.println("7. Exit");
    }

    private static double readDouble(Scanner scanner) {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered!");
            return 0;
        }
    }
}
