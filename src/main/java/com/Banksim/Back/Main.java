package main.java.com.Banksim.Back;

import main.java.com.Banksim.Back.assets.Bank;
import main.java.com.Banksim.Back.assets.Card;
import main.java.com.Banksim.Back.servers.InterbankNetwork;
import main.java.com.Banksim.Back.tpe.Terminal;
public class Main{
public static void main(String[] args) {
        //Create the GIECB
        InterbankNetwork GieCB = InterbankNetwork.getInstance();
        // Create a source bank
        Bank sourceBank = GieCB.createBank("Banque A");
        // Create a destination bank
        Bank destinationBank = GieCB.createBank("Banque B");

        // Create a card associated with the source bank
        Card card = sourceBank.getRandomCard();
        
        // Create a terminal with the source bank
            //Gt receiver's ID
            String accountID = destinationBank.getRandomAccountID();
        Terminal terminal = new Terminal(destinationBank, accountID);

        terminal.processTransaction(card);
    }
}


