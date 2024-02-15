package Banksim.Screen;

import Banksim.Back.assets.Bank;
import Banksim.Back.assets.Card;
import Banksim.Back.servers.InterbankNetwork;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeScreen {

    private static HomeScreen instance;
    private Text text;
    private Button RandomAccount;
    private Button CreateAccount;
    private Button KeepAccount;

    Bank sourceBank;

    private Stage homeStage;

    private HomeScreen() {
        super();
        text = new Text("Welcome to the Bank Simulator!\nPlease choose your Bank Account:");


        // Create the GIECB
        InterbankNetwork GieCB = InterbankNetwork.getInstance();

        // Create a source bank
        sourceBank = GieCB.createBank("Banque A");

        // Set a more appealing font
        text.setFont(Font.font("Arial", 18));

        RandomAccount = new Button("Random Account");
        CreateAccount = new Button("Create Account");
        KeepAccount = new Button("Keep Account");
    }

    public static HomeScreen setScreen() {
        if (instance == null) {
            instance = new HomeScreen();
        }
        return instance;
    }

    public void start() {
        homeStage = new Stage();
        homeStage.setTitle("Banksim");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Add the Text node to the grid and center it
        GridPane.setConstraints(text, 1, 1, 3, 1);
        GridPane.setHalignment(text, HPos.CENTER);

        // Add the Buttons and center them below the text
        RandomAccount.setOnAction(e -> processChoice(1));
        GridPane.setConstraints(RandomAccount, 1, 2);
        GridPane.setHalignment(RandomAccount, HPos.CENTER);

        CreateAccount.setOnAction(e -> processChoice(2));
        GridPane.setConstraints(CreateAccount, 2, 2);
        GridPane.setHalignment(CreateAccount, HPos.CENTER);

        KeepAccount.setOnAction(e -> processChoice(3));
        GridPane.setConstraints(KeepAccount, 3, 2);
        GridPane.setHalignment(KeepAccount, HPos.CENTER);

        grid.getChildren().addAll(text, RandomAccount, CreateAccount, KeepAccount);

        Scene scene = new Scene(grid);
        homeStage.setScene(scene);

        // Set the scene width based on the buttons and gap
        double gap = grid.getHgap() * 2; 
        double sceneWidth = 3 * (125 + gap); 
        homeStage.setWidth(sceneWidth);

        homeStage.show();
    }

    private void processChoice(int choice) {
        switch (choice) {
            case 1:
                Card card = sourceBank.getRandomCard();
                AccountScreen.setNewScreen(card, sourceBank).showAccount();
                homeStage.close();
                break;

            case 3 ://"Keep account" : no variables needed => already in instance
                if(AccountScreen.getInstance()!=null){
                    AccountScreen.setScreen(null, null).showAccount();
                    homeStage.close();
                }
                break;
        }
    }

    public void show(){
        homeStage.show();
    }
        

}