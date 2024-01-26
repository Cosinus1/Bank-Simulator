package main.java.com.Banksim.tpe;

import main.java.com.Banksim.assets.Bank;
import main.java.com.Banksim.assets.Card;

import java.util.Scanner;

public class Terminal {

    private Bank bank;

    public Terminal(Bank bank) {
        this.bank = bank;
    }

    public void processTransaction(Card card) {
        if (card != null) {
            // Display transaction details to the user
            System.out.println("Transaction details:");
            System.out.println("Card Number: " + card.getCardNumber());
            System.out.println("Expiration Date: " + card.getExpirationDate());
            System.out.println("Amount: " + getTransactionAmount());

            // Simulate PIN entry
            boolean pinCheckResult = enterPin(card);

            if (pinCheckResult) {
                // Send authorization request to the merchant's bank via the network
                boolean authorizationResult = bank.routeRequest(card);

                // Process the authorization result
                if (authorizationResult) {
                    System.out.println("Transaction authorized. Payment successful!");
                } else {
                    System.out.println("Transaction declined. Payment unsuccessful.");
                }
            } else {
                System.out.println("Incorrect PIN. Transaction declined.");
            }
        } else {
            System.out.println("Invalid card. Please try again with a valid card.");
        }
    }

    private double getTransactionAmount() {
        // For simplicity, assume a fixed amount for the transaction
        return 100.0;
    }

    private boolean enterPin(Card card) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter PIN for card ending in " + card.getCardNumber().substring(12) + ": ");
        String enteredPin = scanner.nextLine();

        // Simulate comparing entered PIN with the actual PIN (assume PIN is "1234" for demonstration)
        return enteredPin.equals("1234");
    }
}
