package com.example.firstjavafxproject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ForgotPasswordController implements Initializable {

    //buttons
    @FXML
    private Button button_login;

    @FXML
    private Button button_security_question;

    @FXML
    private Button button_password;

    //labels
    @FXML
    private Label lb_password_result;

    @FXML
    private Label lb_security_question;

    //text fields
    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_answer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_security_question.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_email.getText().trim().isEmpty() && !tf_username.getText().trim().isEmpty())
                {
                    String retrievedQuestion = DBUtils.getSecurityQuestion(event, tf_username.getText(),tf_email.getText());
                    lb_security_question.setText(retrievedQuestion);
                }
                else
                {
                    System.out.println("The entered email/username is incorrect");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("The entered email/username is incorrect");
                    alert.show();
                }
            }

        });

        button_password.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_answer.getText().trim().isEmpty())
                {
                    String retrievedAnswer = DBUtils.getPassword( event, tf_answer.getText(),tf_username.getText(),tf_email.getText() );
                    lb_password_result.setText(retrievedAnswer);
                }
                else
                {
                    System.out.println("The entered answer is incorrect");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("The entered answer is incorrect");
                    alert.show();
                }
            }

        });

        button_login.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "hello-view.fxml", "Client log in!", null, null );
            }

        });

    }
}
