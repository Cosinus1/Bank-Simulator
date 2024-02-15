package Banksim.Back.assets;

import java.time.LocalDate;

public class Transaction {
    private double amount;
    private String tag;
    private LocalDate date;

    public Transaction(double amount, String tag) {
        this.amount = amount;
        this.tag = tag;
        this.date = LocalDate.now();
    }

//==============GETTERS===================
    public double getAmount() {
        return amount;
    }

    public  String getTag(){
        return tag;
    }

    public LocalDate getDate() {
        return date;
    }

    public String show(){
        if (amount<0) return String.format("Amount: %f, Destination: %s, Date: %s", amount, tag, date);
        else return String.format("Amount: %.2f, Source: %s, Date: %s", amount, tag, date);
    }
}
