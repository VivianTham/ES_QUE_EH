package sample;

import com.sun.corba.se.spi.activation.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.util.logging.PlatformLogger;

import java.util.Scanner;


public class Main extends Application {

    public static Client client;
    int portNumber = 9000;
    String serverAddress = "localhost";
    public static Controller mainController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        //there might be a runtime error where the port is already running, that's why use try and catch
        try {

            client = new Client(serverAddress, portNumber);

            //class and object name to be passed into scene later
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            Parent root = loader.load();
            mainController = loader.getController();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Client Chat");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
        // try to connect to the server and return if not connected
        if (!client.start())
            return;

        //closing client window quits the server connection
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                    Main.client.sendOverConnection("QUIT");
            }
        });
    }


    public static void main(String[] args) {
        launch(args);

        Scanner scan = new Scanner(System.in);

        while (scan.hasNextLine()) {

            String msg = scan.nextLine();
            client.sendOverConnection(msg);

        }
        scan.close();
    }

}
