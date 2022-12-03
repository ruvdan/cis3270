package com.example.firstjavafxproject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    //buttons
    @FXML
    private Button button_admin;
    @FXML
    private Button button_login;

    @FXML
    private Button button_forgot;

    @FXML
    private Button button_sign_up;

    //text field
    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_username;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_admin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "AdminSignIn.fxml", "Admin Sign Up!", null, null);
            }
        });

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()) {
                    DBUtils.logInUser(event, tf_username.getText(), tf_password.getText());
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

        button_forgot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "ForgotPassword.fxml", "Password Recovery!", null, null);
            }
        });

        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "Sign-up.fxml", "Client Sign Up!", null, null);
            }
        });
    }

}
//    @FXML
//    private Label welcomeText;
//
//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }


