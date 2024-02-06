package Banksim.Screen;

import Banksim.Back.assets.Bank;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BankScreen {
    private Stage bankStage;
    private Bank bank;

    public BankScreen(Bank bank) {
        this.bank = bank;
    }

    public void start() {
        bankStage = new Stage();
        bankStage.setTitle(bank.getName() + " Details");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label bankNameLabel = new Label("Bank ID:");
        bankNameLabel.setFont(Font.font("Arial", 14));
        GridPane.setConstraints(bankNameLabel, 0, 0);

        Label bankNameValue = new Label(bank.getID());
        bankNameValue.setFont(Font.font("Arial", 14));
        GridPane.setConstraints(bankNameValue, 1, 0);

        Label databasePathLabel = new Label("Database Path:");
        databasePathLabel.setFont(Font.font("Arial", 14));
        GridPane.setConstraints(databasePathLabel, 0, 1);

        Label databasePathValue = new Label(bank.getDatabasePath());
        databasePathValue.setFont(Font.font("Arial", 14));
        GridPane.setConstraints(databasePathValue, 1, 1);

        grid.getChildren().addAll(bankNameLabel, bankNameValue, databasePathLabel, databasePathValue);

        Scene scene = new Scene(grid, 500, 150);
        bankStage.setScene(scene);
        bankStage.show();
    }
}
