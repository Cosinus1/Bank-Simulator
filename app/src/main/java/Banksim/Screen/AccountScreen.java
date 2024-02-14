package Banksim.Screen;

import java.util.ArrayList;

import Banksim.Back.assets.Bank;
import Banksim.Back.assets.BankAccount;
import Banksim.Back.assets.Card;
import Banksim.Back.assets.Transaction;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AccountScreen {
    private static AccountScreen instance;

    private Stage accountStage;
    private Card card;
    private Bank bank;

    private AccountScreen(Card card, Bank bank){ // Create the Account Screen
        accountStage = new Stage();
        accountStage.setTitle("BankSim : Your Account");

        this.card = card;
        this.bank = bank;
    }

    //Get the Account
    public static AccountScreen setScreen(Card card, Bank bank){
        if (instance == null) instance = new AccountScreen(card, bank);
        return instance;
    }
    //Reset instance to get a new screen for "Random Account" account
    public static AccountScreen setNewScreen(Card card, Bank bank){
        instance = null;
        return setScreen(card, bank);
    }
    //Getter for "Keep Account" button
    public static AccountScreen getInstance(){
        return instance;
    }
    

     public void showAccount() {
        // Get Account information from the card :
        BankAccount bankAccount = bank.getBankAccount(card.getCardNumber());
    
        GridPane grid = new GridPane();
        int row = 8;
    
        Text text = new Text("Your account informations :");
        Text accountNumberText = new Text("Account Number: " + bankAccount.getAccountNumber());
        Text accountHolderText = new Text("Account Holder: " + bankAccount.getAccountHolderName());
        Text balanceText = new Text("Balance: " + bankAccount.getBalance());
    
        // Display Payment History
        ArrayList<Transaction> paymentHistory = bankAccount.getPaymentHistory();
        StringBuilder paymentHistoryString = new StringBuilder("Payment History:\n");
        for (Transaction payment : paymentHistory) {
            paymentHistoryString.append("| " + payment.show() + " | \n");
            row ++;
        }
        Text paymentHistoryText = new Text(paymentHistoryString.toString());
        //Buttons
        Button transactionButton = new Button("Make a payment");
        Button backButton = new Button("Back");
    
        // Set up the grid
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
    
        // Ajouter les éléments au grid
        GridPane.setConstraints(text, 1, 1, 3, 1);
        GridPane.setHalignment(text, HPos.CENTER);
        
        GridPane.setConstraints(accountNumberText, 1, 2);
        GridPane.setConstraints(accountHolderText, 1, 3);
        GridPane.setConstraints(balanceText, 1, 4);
        GridPane.setConstraints(paymentHistoryText, 1, 5); // Ajout de l'historique des paiements
        GridPane.setConstraints(transactionButton, 1, row);
        GridPane.setHalignment(transactionButton, HPos.RIGHT);
        GridPane.setConstraints(backButton, 1, row);
        GridPane.setHalignment(backButton, HPos.LEFT);
    
        // Set Button Action
        transactionButton.setOnAction(e -> {
            processTransaction(card);
        });

        backButton.setOnAction(e -> {
            HomeScreen.setScreen().show();
            hide();
        });
    
        grid.getChildren().addAll(text, accountNumberText, accountHolderText, balanceText, paymentHistoryText, transactionButton, backButton);
    
        Scene accountScene = new Scene(grid);
        accountStage.setScene(accountScene);
        accountStage.show();
    }
    
    public void processTransaction(Card card){
        TerminalScreen terminalScreen = TerminalScreen.getTerminal();
        terminalScreen.insertCard(card);
        terminalScreen.start();
        accountStage.close();
    }

    public void hide(){
        accountStage.close();
    }

}
