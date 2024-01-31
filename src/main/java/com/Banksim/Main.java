package main.java.com.Banksim;

import main.java.com.Banksim.assets.Bank;
import main.java.com.Banksim.assets.Card;
import main.java.com.Banksim.servers.InterbankNetwork;
import main.java.com.Banksim.tpe.Terminal;
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
            String accountID = sourceBank.getRandomAccountID();
        Terminal terminal = new Terminal(destinationBank, accountID);

        terminal.processTransaction(card);
    }
}


