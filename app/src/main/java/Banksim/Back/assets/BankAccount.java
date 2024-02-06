package Banksim.Back.assets;

public class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    private double balance;

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public boolean processTransaction(double amount) {
        // Simulate processing a transaction
        if (balance >= amount) {
            balance -= amount;
            return true; // Transaction successful
        } else {
            return false; // Insufficient funds
        }
    }
}
