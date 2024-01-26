package main.java.com.Banksim.assets;

import java.util.Date;

public class Transaction {
    private String transactionId;
    private Date timestamp;
    private String cardNumber;
    private double amount;
    private boolean status; // Success or failure

    public Transaction(String transactionId, Date timestamp, String cardNumber, double amount, boolean status) {
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.status = status;
    }

    // Getters for accessing transaction information
    // ...

    // Additional methods or features related to transactions
    // ...
}
