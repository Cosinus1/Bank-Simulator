package main.java.com.Banksim.Back.tpe;

import main.java.com.Banksim.Back.assets.Bank;
import main.java.com.Banksim.Back.assets.Card;

import java.util.Scanner;

public class Terminal {

    private Bank bank;
    private String accountID;

    private Scanner scanner;

    public Terminal(Bank bank, String accountID) {
        this.bank = bank;
        this.accountID = accountID;
        scanner = new Scanner(System.in);

    }

    public void processTransaction(Card card) {
        if (card != null) {
            // Display transaction details to the user
            System.out.println("Transaction details:");
            System.out.println("Card Number: " + card.getCardNumber());
            System.out.println("Expiration Date: " + card.getExpirationDate());

            int Amount = enterAmount();

            System.out.println("Amount: " + Amount);

            // Simulate PIN entry
            boolean pinCheckResult = enterPin(card);

            if (pinCheckResult) {
                System.out.println("PIN Code Correct");
                //Store transaction amount in the card
                card.processTransaction(Amount);
                // Send authorization request to the merchant's bank via the network
                boolean authorizationResult = bank.routeRequest(card, accountID);

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

    private boolean enterPin(Card card) {
        System.out.print("Enter PIN for card ending in " + card.getCardNumber().substring(6) + ": ");
        String enteredPin = scanner.nextLine();

        // Simulate comparing entered PIN with the actual PIN (assume PIN is "1234" for demonstration)
        return enteredPin.equals(card.getPin());
    }

    private int enterAmount() {
        System.out.print("Enter the Amount : ");
        String enteredAmount = scanner.nextLine();
        
        // Convert the enteredAmount to an integer
        int Amount = Integer.parseInt(enteredAmount);
        
        return Amount;
    }
    
    
}
