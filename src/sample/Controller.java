package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Controller {

    @FXML   //connects scene builder fxml file to controller.java
    public TextArea messages_console;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendBtn;

    StringBuilder input = new StringBuilder("");
//    private void createContent(ActionEvent event) {
//
//        //TextField input = new TextField();
////        VBox root = new VBox(20, messages_console, input);
//////        root.setPrefSize(600, 600);
//////        return root;
//
//        //create text field with text and no. of column initialised
//        JTextField input = new JTextField("Type a message..",20);
//        String content = input.getText();       //getting all content from text field
//
//        //this set the input focus automatically at any time
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                input.requestFocusInWindow();
//            }
//        });
//
//        input.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(java.awt.event.ActionEvent e) {
//                System.out.println("The entered text is: " + input.getText());
//            }
//        });
//    }


    private void textConsole(ActionEvent event) {
        while (true) {
            if (Client.display != null) {
                messages_console.setText(Client.display);
                Client.display = null;
                break;
            }
        }
    }


    @FXML
    public void sendButton( ) {

        String currentInput = userInput.getText();
        Main.client.sendOverConnection(currentInput);
        userInput.setText("");

    }
}


        //create object
//        Random rand = new Random();
//        int myrand = rand.nextInt(50) + 1;

        //print on Label
      //  myMessage.setText(Integer.toString(myrand));

        //print on console
        //System.out.println(Integer.toString(myrand));







//    private Client createClient() {
//        return new Client("localhost", 9000, data -> {
//            Platform.runLater(() -> {
//                messages_console.appendText(messages.toString() + "\n");
//            });
//        });
//    }



//    @FXML
//    public TextArea myMessage;

//    public void generateRandom(ActionEvent event) {
//        while (true) {
//            if (Client.returnMsg != null) {
//                myMessage.setText(Client.returnMsg);
//                Client.returnMsg = null;
//                break;
//            }
//        }
//    }

