package Banksim.Screen;
    
import java.util.ArrayList;

import Banksim.Back.assets.Log;
import Banksim.Back.database.DatabaseManager;
import Banksim.Back.servers.InterbankNetwork;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GieCBScreen {
    private Stage bankStage;
    private InterbankNetwork GieCB;

    public GieCBScreen(InterbankNetwork GieCB) {
        this.GieCB = GieCB;
    }

    public void start() {
        bankStage = new Stage();
        bankStage.setTitle("GieCB Details");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        //Labels
            Label databasePathLabel = new Label("Database Path:" + GieCB.getDatabasePath());
            databasePathLabel.setFont(Font.font("Arial", 12));
            GridPane.setConstraints(databasePathLabel, 0, 1);
        //Database to String
        Text Database = new Text(DatabaseManager.printGieCBDatabase(DatabaseManager.connect(GieCB.getDatabasePath())));
        GridPane.setConstraints(Database, 0, 3);
        //Logs to String

        // Display Payment History
        ArrayList<Log> Logs = GieCB.getLogs();
        StringBuilder LogsString = new StringBuilder("-------Logs------- :\n");
        for (Log log : Logs) {
            LogsString.append("| " + log.show() + " | \n");
        }
        Text LogsText = new Text(LogsString.toString());
        GridPane.setConstraints(LogsText, 3, 3);
        //Buttons
        Button closeButton = new Button("Close");
        GridPane.setConstraints(closeButton, 0, 0);
        closeButton.setOnAction( e-> {
            bankStage.close();
        });

        grid.getChildren().addAll(closeButton, databasePathLabel, Database, LogsText);

        Scene scene = new Scene(grid, 1200, 500);
        bankStage.setScene(scene);
        bankStage.show();
    }

    public void stop(){
        bankStage.close();
    }
}

