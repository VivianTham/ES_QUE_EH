package sample;


import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;


public class Controller {

    @FXML   //connects scene builder fxml file to controller.java
    public TextArea messages_console;
    @FXML
    public TextArea userInput;
    @FXML
    public Button sendBtn;
    @FXML
    public Button pmBtn;

    StringBuilder input = new StringBuilder("");


    @FXML
    public void setUsername(){

        String currentInput = userInput.getText().trim();

        if (!currentInput.equals("")) {
            Main.client.sendOverConnection("IDEN " + currentInput);
        }
        userInput.setText("");
        userInput.requestFocus();

    }

    @FXML
    public void sendBroadcast(){

        String currentInput = userInput.getText().trim();
        String[] inputData = currentInput.split("\n");

        //handling multiple lines input
        if (!currentInput.equals("")) {

            for (int i = 0; i < inputData.length; i++) {
                if (!inputData[i].trim().equals("")) {
                    Main.client.sendOverConnection("HAIL " + inputData[i].trim());
                }
            }
        }
        userInput.setText("");
        userInput.requestFocus();

    }
    @FXML
    public void sendPrivateMsg(){

        String currentInput = userInput.getText().trim();
        String[] inputData = currentInput.split(" ");
        String username = inputData[0];
        String msg = "";

        //handling multiple lines input
        for (int i = 1; i < inputData.length; i++) {
            msg = msg + inputData[i] + " ";
        }

        msg = msg.trim();

        String[] inputMsg = msg.split("\n");

        if (!username.equals("") && !msg.equals("")) {

            for (int i = 0; i < inputMsg.length; i++) {
                if (!inputMsg[i].trim().equals("")) {
                    Main.client.sendOverConnection("MESG " + username + " " + inputMsg[i].trim());
                }
            }
        }

        userInput.setText("");
        userInput.requestFocus();

    }

    @FXML
    public void sendStat(){

        Main.client.sendOverConnection("STAT" );
        userInput.setText("");
        userInput.requestFocus();
    }

    @FXML
    public void sendList(){

        Main.client.sendOverConnection("LIST" );
        userInput.setText("");
        userInput.requestFocus();
    }
}



