package Banksim.Screen;

import Banksim.Back.assets.Bank;
import Banksim.Back.assets.Card;
import Banksim.Back.servers.InterbankNetwork;
import Banksim.Back.tpe.Terminal;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TerminalScreen {

    private static TerminalScreen instance;

    private Stage terminalStage;
    private TextField amountField;
    private Button validateAmountButton;
    private PasswordField pinField;
    private Button processButton;

    private Terminal terminal;
    private Card card;
    private int transactionCode; //Error Code for TransactionScreen

    private Bank sourceBank;
    private Bank destinationBank;
    private String accountID;

    private TerminalScreen() {
        // Create the GIECB
        InterbankNetwork GieCB = InterbankNetwork.getInstance();

        // Create a source bank
        sourceBank = GieCB.createBank("Banque A");

        // Create a destination bank
        destinationBank = GieCB.createBank("Banque B");

        // Create a card associated with the source bank
        card = sourceBank.getRandomCard();

        // Create a terminal with the destination bank
        accountID = destinationBank.getRandomAccountID();
        terminal = new Terminal(destinationBank, accountID);
    }

    public static TerminalScreen setScreen() {
        if (instance == null) {
            instance = new TerminalScreen();
        }
        return instance;
    }

    public void start() {
        terminalStage = new Stage();
        terminalStage.setTitle("Terminal");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Text text = new Text("Select an Amount");
        text.setFont(Font.font("Arial", 18));

        // Add the Text node to the grid and center it
        GridPane.setConstraints(text, 1, 1, 3, 1);
        GridPane.setHalignment(text, HPos.CENTER);

        amountField = new TextField();
        amountField.setPromptText("Enter the Amount");
        GridPane.setConstraints(amountField, 1, 2);

        // Add action for Enter key in amountField
        amountField.setOnAction(e -> validateAmount());

        validateAmountButton = new Button("Validate Amount");
        validateAmountButton.setOnAction(e -> validateAmount());
        GridPane.setConstraints(validateAmountButton, 1, 3);

        pinField = new PasswordField();
        pinField.setPromptText("Enter PIN");
        pinField.setDisable(true);
        GridPane.setConstraints(pinField, 1, 4);

        // Add action for Enter key in pinField
        pinField.setOnAction(e -> processTransaction());

        processButton = new Button("Process Transaction");
        processButton.setOnAction(e -> processTransaction());
        processButton.setDisable(true);
        GridPane.setConstraints(processButton, 1, 5);

        grid.getChildren().addAll(text, amountField, validateAmountButton, pinField, processButton);

        Scene scene = new Scene(grid, 300, 200);
        terminalStage.setScene(scene);
        terminalStage.show();
    }

    private void validateAmount() {
        if (!amountField.getText().isEmpty()) {
            //Store in Terminal
            terminal.setAmount(Integer.parseInt(amountField.getText()));
            // Amount is valid, enable PIN entry
            amountField.setDisable(true);
            validateAmountButton.setDisable(true);
            pinField.setDisable(false);
            processButton.setDisable(false);
        }
    }

    private void processTransaction() {
        if (pinField.getText().equals(card.getPin())) {
            //Store amount in the card
            card.processTransaction(terminal.getAmount());
            transactionCode = terminal.processTransaction(card);
            TransactionScreen transactionScreen = new TransactionScreen(transactionCode, card);
            transactionScreen.start();

        }
    
    }

    }
