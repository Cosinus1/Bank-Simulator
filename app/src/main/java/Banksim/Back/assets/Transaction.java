package Banksim.Back.assets;

import java.time.LocalDate;

public class Transaction {
    private int amount;
    private String tag;
    private LocalDate date;

    public Transaction(int amount, String tag) {
        this.amount = amount;
        this.tag = tag;
        this.date = LocalDate.now(); // Capture the current date
    }

    // Getter methods for amount, receiver, and date
    public int getAmount() {
        return amount;
    }

    public  String getTag(){
        return tag;
    }

    public LocalDate getDate() {
        return date;
    }

    public String show(){
        if (amount<0) return String.format("Amount: %d, Destination: %s, Date: %s", amount, tag, date);
        else return String.format("Amount: %d, Source: %s, Date: %s", amount, tag, date);
    }
}
