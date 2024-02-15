package Banksim.Back.assets;

public class Card {
    private String cardNumber;
    private String expirationDate;
    private String pin;
    private double transaction;

    public Card(String cardNumber, String expirationDate, String pin) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.pin = pin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getPin() {
        return pin;
    }
    
    public double getTransaction(){
        return transaction;
    }

    public void processTransaction(double amount){
        transaction = amount;
    }
}