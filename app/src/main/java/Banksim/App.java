package Banksim;

import Banksim.Screen.MainScreen;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Create an instance of MainScreen and call its start method
        MainScreen mainScreen = MainScreen.setScreen();
        mainScreen.start(primaryStage);
    }
}
