package com.example.firstjavafxproject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;



public class AdminSignInController  implements Initializable {
    @FXML
    private Button button_login;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_username;

    @FXML
    private Button button_client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_client.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "hello-view.fxml", "Client Sign Up!", null, null);
            }
        });


        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()) {
                    DBUtils.AdminlogInUser(event, tf_username.getText(), tf_password.getText());
                }
                else
                {
                    System.out.println("Please file in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to Login!");
                    alert.show();
                }
            }
        });

    }
}
