package Banksim.Back.tpe;

import Banksim.Back.assets.Bank;
import Banksim.Back.assets.Card;

public class Terminal {

    private Bank bank;
    private String accountID;
    private int amount;


    public Terminal(Bank bank, String accountID) {

        this.bank = bank;
        this.accountID = accountID;
        this.amount = 0;

    }

    public int getAmount(){
        return amount;
    }
    public void setAmount(int amount){
        this.amount = amount;
    }

    public int processTransaction(Card card) {
        if (card != null) {
            // Display transaction details to the user
            System.out.println("Transaction details:");
            System.out.println("Card Number: " + card.getCardNumber());
            System.out.println("Expiration Date: " + card.getExpirationDate());
            System.out.println("Transaction Amount :" + card.getTransaction());            


            // Send authorization request to the merchant's bank via the network
            int authorizationResult = bank.routeRequest(card, accountID);

            // Process the authorization result
            return authorizationResult;
        } else {
            System.out.println("Invalid card. Please try again with a valid card.");
            return -1;
        }
    }

 }
