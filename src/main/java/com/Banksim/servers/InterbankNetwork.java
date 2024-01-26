package main.java.com.Banksim.servers;

import main.java.com.Banksim.assets.Bank;
import main.java.com.Banksim.assets.Card;

public class InterbankNetwork {
    // Singleton instance for the interbank network
    private static InterbankNetwork instance;

    // Private constructor to enforce singleton pattern
    private InterbankNetwork() {
        //DataBase of Banks to be added
    }

    // Method to get the singleton instance of InterbankNetwork
    public static InterbankNetwork getInstance() {
        if (instance == null) {
            instance = new InterbankNetwork();
        }
        return instance;
    }
    public boolean routeRequest(Card card) {
        // Extract buyer's bank information from the card
        String buyerBankCode = extractBuyerBankCode(card);

        // Find the buyer's bank based on the bank code
        Bank buyerBank = findBuyerBank(buyerBankCode);

        if (buyerBank != null) {
            // Redirect the transaction request to the buyer's bank
            return buyerBank.processTransaction(card);
        } else {
            System.out.println("Error: Buyer's bank not found.");
            return false;
        }
    }

    private String extractBuyerBankCode(Card card) {
        // Extract the buyer's bank code from the card number (assumed format)
        // For example, assuming the bank code is the first three digits of the card number
        return card.getCardNumber().substring(0, 3);
    }

    private Bank findBuyerBank(String bankCode) {
        // In a real-world scenario, you would have a mechanism to look up banks based on their codes
        // For simplicity, let's assume a hardcoded list of banks
        if ("123".equals(bankCode)) {
            return new Bank(); // Replace with the actual buyer's bank instance
        } else {
            return null; // Buyer's bank not found
        }
    }
}
