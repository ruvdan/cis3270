package com.example.firstjavafxproject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    //Buttons
    @FXML
    private Button button_login;

    @FXML
    private Button button_sign_up;

    //choice box

    @FXML
    private ChoiceBox<String> cb_security_question;

    @FXML
    private ChoiceBox<String> cb_state;


    //text fields

    @FXML
    private TextField tf_address1;

    @FXML
    private TextField tf_address2;

    @FXML
    private TextField tf_answer;

    @FXML
    private TextField tf_city;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_first_name;

    @FXML
    private TextField tf_last_name;
    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_ssn;

    @FXML
    private TextField tf_zipcode;

    private final String [] stateName = {"Alaska", "Alabama", "Arkansas", "American Samoa", "Arizona", "California", "Colorado", "Connecticut", "District of Columbia", "Delaware", "Florida", "Georgia", "Guam", "Hawaii", "Iowa", "Idaho", "Illinois", "Indiana", "Kansas", "Kentucky", "Louisiana", "Massachusetts", "Maryland", "Maine", "Michigan", "Minnesota", "Missouri", "Mississippi", "Montana", "North Carolina", "North Dakota", "Nebraska", "New Hampshire", "New Jersey", "New Mexico", "Nevada", "New York", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Puerto Rico", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Virginia", "Virgin Islands", "Vermont", "Washington", "Wisconsin", "West Virginia", "Wyoming"};

    private final String[] securityQuestions = {"In what city were you born?",
            "What is the name of your favorite pet?",
            "What is your mother's maiden name?",
            "What high school did you attend?",
            "What was the name of your elementary school?"};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cb_state.getItems().addAll(stateName);
        cb_security_question.getItems().addAll(securityQuestions);

        button_sign_up.setOnAction(new EventHandler<ActionEvent>()
        {
            String[] signUpValues = new String[14];
            @Override
            public void handle(ActionEvent event) {

                String stateValue = cb_state.getValue();

                String security_questionValue = cb_security_question.getValue();

                if(!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()
                && !tf_first_name.getText().trim().isEmpty() && !tf_last_name.getText().trim().isEmpty()
                && !tf_email.getText().trim().isEmpty() && !tf_ssn.getText().trim().isEmpty()
                && !tf_address1.getText().trim().isEmpty() && !tf_city.getText().trim().isEmpty()
                && !stateValue.isEmpty() && !tf_zipcode.getText().trim().isEmpty()
                && !security_questionValue.isEmpty() && !tf_answer.getText().trim().isEmpty())
                {
                    signUpValues[0] = tf_username.getText();
                    signUpValues[1] = tf_password.getText();
                    signUpValues[2] = "Air Fair";
                    signUpValues[3] = tf_first_name.getText();
                    signUpValues[4] = tf_last_name.getText();
                    signUpValues[5] = tf_address1.getText();
                    signUpValues[6] = tf_address2.getText();
                    signUpValues[7] = tf_city.getText();
                    signUpValues[8] = stateValue;
                    signUpValues[9] = tf_zipcode.getText();
                    signUpValues[10] = tf_ssn.getText();
                    signUpValues[11] = security_questionValue;
                    signUpValues[12] = tf_password.getText();
                    signUpValues[13] = tf_email.getText();

                    DBUtils.signUpUser(event, signUpValues);
                }
                else
                {
                    System.out.println("Please file in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!");
                    alert.show();
                }
            }
        });

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "hello-view.fxml", "Log In!", null, null );
            }
        });

    }

}
