package sample;

import com.sun.corba.se.spi.activation.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sun.util.logging.PlatformLogger;

import java.util.Scanner;


public class Main extends Application {

    public static Client client;
    int portNumber = 9000;
    String serverAddress = "localhost";
    public static Controller mainController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        //there might be a runtime error where the port is already running, thats why use try and catch
        try {//class + object name that will be passed later into the scene
            client = new Client(serverAddress, portNumber);
            // try to connect to the server and return if not connected

            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            Parent root = loader.load();
            mainController = (Controller)loader.getController();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Client Chat");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
        if (!client.start())
            return;
    }


    public static void main(String[] args) {
        launch(args);
        Scanner scan = new Scanner(System.in);

        System.out.println("\nEnter username:");

        while (scan.hasNextLine()) {

            String msg = scan.nextLine();
            client.sendOverConnection(msg);

        }
        scan.close();
    }

}
