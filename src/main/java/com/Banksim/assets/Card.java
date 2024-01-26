package main.java.com.Banksim.assets;

public class Card {
    private String cardNumber;
    private String expirationDate;
    private String pin;

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
}