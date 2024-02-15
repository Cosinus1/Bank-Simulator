package Banksim.Back.assets;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class BankAccount {
    private static final Map<String, BankAccount> accountCache = new HashMap<>();

    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private ArrayList<Transaction> paymentHistory;

    private BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
        this.paymentHistory = new ArrayList<>();
    }

    public static BankAccount getAccount(String ID){
        if (accountCache.containsKey(ID)) {
            return accountCache.get(ID);
        }
        // If not in the cache, create a new instance, cache it, and return it
        BankAccount newBankAccount = new BankAccount(ID);
        accountCache.put(ID, newBankAccount);
        return newBankAccount;
    }
    //==============GETTERS================
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<Transaction> getPaymentHistory(){
        return paymentHistory;
    }
    //=============SETTERS================
    public void setAccountHolderName(String Name){
        accountHolderName = Name;
    }

    public void setBalance(double balance){
        this.balance = balance;
    }

    public void addPayment(double amount,  String tag){
        paymentHistory.add(new Transaction(amount, tag));
    }
}
