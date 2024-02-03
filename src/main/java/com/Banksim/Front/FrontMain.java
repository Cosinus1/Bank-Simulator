import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FrontMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Credit Card Simulator Front-End");

        // Create buttons for actions
        Button simulateTransactionButton = new Button("Simulate Transaction");
        Button exitButton = new Button("Exit");

        // Set actions for the buttons
        simulateTransactionButton.setOnAction(event -> simulateTransaction());
        exitButton.setOnAction(event -> primaryStage.close());

        // Create a layout
        VBox vbox = new VBox(simulateTransactionButton, exitButton);
        vbox.setSpacing(10);

        // Set the scene
        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    // Method to simulate a transaction (you can modify this according to your needs)
    private void simulateTransaction() {
        System.out.println("Simulating Transaction...");
        // Add code to interact with your back-end and display results in the front-end
    }

    public static void main(String[] args) {
        launch(args);
    }
}
