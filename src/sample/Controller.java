package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import java.io.IOException;

public class Controller {

    @FXML   //connects scene builder fxml file to controller.java
    public TextArea messages_console;
    @FXML
    public TextArea userInput;
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


    public void textConsole(ActionEvent event) {
        while (true) {
            if (Client.display != null) {
                messages_console.setText(Client.display);
                Client.display = null;
                break;
            }
        }
    }



//        else if (keyEvent.getCode() == KeyCode.SHIFT)
//        {
//            if (keyEvent.getCode() == KeyCode.ENTER)
//            {
//                StringBuilder sb = new StringBuilder("");
//                sb.append(userInput.getText());
//                sb.append(("\n"));
//            }
//          }

//        if(keyEvent.getCode() == KeyCode.DOWN){
//            String currentInput = userInput.getText();
//        }


//    @FXML
//    public void nextLinebyShift(KeyEvent keyEvent)
//    {
//        if (keyEvent.getCode() == KeyCode.SHIFT)
//        {
//            if (keyEvent.getCode() == KeyCode.ENTER)
//            {
//                StringBuilder sb = new StringBuilder("");
//                sb.append(userInput.getText());
//                sb.append(("\n"));
//                userInput.setText(sb.toString());
//            }
//        }
//    }

    @FXML
    public void sendButton( ) throws IOException {

        String currentInput = userInput.getText();

//        if(currentInput == "QUIT"){
//            Client.getSocket().close();
//        }

        Main.client.sendOverConnection(currentInput);
        userInput.setText("");

    }
}




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

