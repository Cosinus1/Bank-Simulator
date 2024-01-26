package main.java.com.Banksim;

import main.java.com.Banksim.assets.Bank;
import main.java.com.Banksim.assets.Card;
import main.java.com.Banksim.tpe.Terminal;
public class Main{
public static void main(String[] args) {
        // Create a source bank
        Bank sourceBank = new Bank();

        // Create a destination bank
        Bank destinationBank = new Bank();

        // Create a card associated with the source bank
        Card card = new Card("1234567890123456", "09/24", "0123");
        
        // Create a terminal with the source bank
        Terminal terminal = new Terminal(destinationBank);

        terminal.processTransaction(card);
    }
}


