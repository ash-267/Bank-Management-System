# Bank Management System — Java Mini Project

A robust, console-based Java application designed to simulate modern banking operations. This project was developed to manage accounts, transactions, and data persistence while implementing core principles of Object-Oriented Programming and Multithreading.

## 🎓 Academic Context
* **Institution:** Marathwada Mitramandal's College of Engineering (MMCOE), Karvenagar, Pune
* **Course:** BTech Computer Engineering (2nd Year)
* **Subject:** Principles of Programming Languages (PPL) Laboratory

## 👥 Project Team
* **Shambhavi Vaishampayan**
* **Swaroopa Dhepe**
* **Yash Adhav**
* **Ayush Sadavarte**

---

## 🚀 Key Features

* **Dual Account Management**: 
    * **Savings Account**: Features a minimum balance requirement (Rs. 500).
    * **Current Account**: Supports overdraft facilities up to a limit (Rs. 2000).
* **Automated ID Generation**: Unique ID sequences for Savings (starting 1001) and Current (starting 2001) accounts that persist across sessions.
* **Security**: All account operations (Deposit, Withdrawal, Transfer, View) are protected by a user-defined 4-digit PIN.
* **Transaction History**: Real-time logging of all activities with timestamps, viewable per account.
* **Data Persistence**: Automatic saving and loading of account states and transaction logs using a Text-based file handling.

## 🛠️ Technical Implementation

This project implements several advanced Java concepts:

1. **Inheritance & Polymorphism**: Uses a base `Account` class with specialized logic for `Savings` and `Current` subclasses, utilizing method overriding for withdrawal rules.
2. **Multithreading & Synchronization**: The Transfer feature uses separate threads for the sender and receiver, utilizing the `synchronized` keyword to ensure data integrity during concurrent operations.
3. **Constructor Overloading**: Implements multiple constructors to distinguish between creating a brand-new account (ID generation) and restoring an existing account from the database.
4. **Custom Exception Handling**: Developed `InsufficientBalanceException` to handle financial logic errors gracefully.
5. **Encapsulation**: Strict use of access modifiers and getters/setters to protect sensitive data like balances and PINs.

---

## 📂 File Structure

* `Main.java` — The entry point and user interface.
* `Bank.java` — Management logic, account searching, and File I/O.
* `Account.java` — Base model for banking logic and ID counters.
* `SavingsAccount.java` / `CurrentAccount.java` — Specialized account behaviors.
* `Transaction.java` — Data model for logging financial history.
* `InsufficientBalanceException.java` — Custom error handling for withdrawals.

---

## ⚙️ How to Use

1. **Clone the repository**:
   ```bash
   git clone [https://github.com/](https://github.com/)[your-username]/bank-management-system.git
