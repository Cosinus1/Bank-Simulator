package Banksim.Screen;

import Banksim.Back.assets.Card;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TransactionScreen {
    Card card;
    int code;

    public TransactionScreen(int code, Card card) {
        this.card = card;
        this.code = code;
    }

    public void start() {
        switch (code) {
            case 0:
                showTransactionSuccessScreen(card);
                break;
            case 1: // In GieCB: Error: Buyer's bank not found.
                showTransactionFailScreen("Error: Buyer's bank not found.");
                break;
            case 2: // In buyer's bank database: Error retrieving bank account from database
                showTransactionFailScreen("Error retrieving bank account from database");
                break;
            case 3: // In buyer's bank database: Transaction failed: Insufficient balance
                showTransactionFailScreen("Transaction failed: Insufficient balance");
                break;
            case 4: // In buyer's bank database: Error updating balance in the database
                showTransactionFailScreen("Error updating balance in the database");
                break;
            case 5: // In receiver's bank database: Error retrieving receiver's bank account from the database
                showTransactionFailScreen("Error retrieving receiver's bank account from the database");
                break;
            case 6: // In receiver's bank database: Error Updating the Balance
                showTransactionFailScreen("Error Updating the Balance");
                break;
        }
    }

    private void showTransactionSuccessScreen(Card card) {
        // Create a new stage for transaction success message
        Stage successStage = new Stage();
        successStage.setTitle("Transaction Successful");

        // Create a grid layout for the success screen
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Add success message
        Text successText = new Text("Transaction Successful!");
        successText.setFont(Font.font("Arial", 18));
        GridPane.setConstraints(successText, 1, 1, 3, 1);
        GridPane.setHalignment(successText, HPos.CENTER);

        // Add button to see transaction details
        Button seeDetailsButton = new Button("See Transaction Details");
        seeDetailsButton.setOnAction(e -> {
            // Close the success stage after showing details
            successStage.close();
        });
        GridPane.setConstraints(seeDetailsButton, 1, 2);
        GridPane.setHalignment(seeDetailsButton, HPos.CENTER);

        grid.getChildren().addAll(successText, seeDetailsButton);

        Scene successScene = new Scene(grid, 300, 100);
        successStage.setScene(successScene);
        successStage.show();
    }

    private void showTransactionFailScreen(String errorMessage) {
        // Create a new stage for transaction failure message
        Stage failStage = new Stage();
        failStage.setTitle("Transaction Failed");

        // Create a grid layout for the failure screen
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Add failure message
        Text failText = new Text(errorMessage);
        failText.setFont(Font.font("Arial", 18));
        GridPane.setConstraints(failText, 1, 1, 3, 1);
        GridPane.setHalignment(failText, HPos.CENTER);

        grid.getChildren().addAll(failText);

        Scene failScene = new Scene(grid, 300, 100);
        failStage.setScene(failScene);
        failStage.show();
    }
}
