package Banksim.Screen;

import Banksim.Back.assets.Bank;
import Banksim.Back.servers.InterbankNetwork;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TransactionRouteScreen {
    InterbankNetwork GieCB = InterbankNetwork.getInstance();
    Bank bankA = GieCB.retrieveBankFromDatabase("001");
    Bank bankB = GieCB.retrieveBankFromDatabase("002");
    
    public void start() {
        Stage stage = new Stage();

        stage.setTitle("Transaction Route");

        // Creating images
        ImageView terminalImageView = new ImageView(new Image("file:src/main/resources/png/terminal.png"));
        ImageView bankAImageView = new ImageView(new Image("file:src/main/resources/png/bank.png"));
        ImageView bankBImageView = new ImageView(new Image("file:src/main/resources/png/bank.png"));
        ImageView giecbImageView = new ImageView(new Image("file:src/main/resources/png/giecb.png"));
        //Arrows symbolizing routing path
        ImageView arrowBAImageView = new ImageView(new Image("file:src/main/resources/png/arrow_right.png"));
        ImageView arrowBBImageView = new ImageView(new Image("file:src/main/resources/png/arrow_right.png"));
        ImageView arrowGIECBImageView = new ImageView(new Image("file:src/main/resources/png/arrow_right.png"));


        // Resizing images
            //Terminal
            terminalImageView.setFitHeight(150);
            terminalImageView.setFitWidth(150);
            //GiecB
            giecbImageView.setFitHeight(150);
            giecbImageView.setFitWidth(150);
            //BankA
            bankAImageView.setFitHeight(150);
            bankAImageView.setFitWidth(150);
            //BankB
            bankBImageView.setFitHeight(150);
            bankBImageView.setFitWidth(150);
            //Arrow terminal => Bank B
            arrowBBImageView.setFitHeight(150);
            arrowBBImageView.setFitWidth(150);
            //Arrow Bank B => GiecB
            arrowGIECBImageView.setFitHeight(150);
            arrowGIECBImageView.setFitWidth(150);
            //Arrow GiecB => Bank A
            arrowBAImageView.setFitHeight(150);
            arrowBAImageView.setFitWidth(150);

        // Creating buttons
        Button bankAButton = new Button("View Bank A Details");
        Button bankBButton = new Button("View Bank B Details");
        Button giecbButton = new Button("View GIECB Details");

        Button backButton = new Button("Back");

        // Setting button actions
        bankAButton.setOnAction(e -> {
            BankScreen bankScreenA = new BankScreen(bankA);
            bankScreenA.start();
        });
        bankBButton.setOnAction(e -> {
            BankScreen bankScreenB = new BankScreen(bankB);
            bankScreenB.start();
        });

        giecbButton.setOnAction(e -> {
            GieCBScreen giecbScreen = new GieCBScreen(GieCB);
            giecbScreen.start();
        });

        backButton.setOnAction(e -> {
            AccountScreen.setScreen(null, null).showAccount();
            stage.close();
        });

        // Creating HBox for the full Path
        HBox routeBox = new HBox(50, terminalImageView, arrowBBImageView, bankBImageView, arrowGIECBImageView, giecbImageView, arrowBAImageView, bankAImageView);
        routeBox.setPadding(new Insets(50, 0, 0, 5)); 

        // Creating HBox for banks & giecb buttons
        HBox buttonBox = new HBox(250, bankBButton,giecbButton, bankAButton);
        buttonBox.setPadding(new Insets(200, 0, 0, 400));
        
        HBox backBox = new HBox(0, backButton);
        backBox.setPadding(new Insets(15, 0, 0, 0));

        // Creating VBox to stack all HBoxes
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(routeBox, buttonBox, backBox);

        // Set spacing between VBox children
        StackPane.setMargin(buttonBox, new Insets(50, 0, 0, 0)); 
        // Set padding for the entire layout
        stackPane.setPadding(new Insets(10));

        // Set scene
        Scene scene = new Scene(stackPane, 1400, 400);
        stage.setScene(scene);
        stage.show();
    }
}
