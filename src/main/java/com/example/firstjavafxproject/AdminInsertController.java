package com.example.firstjavafxproject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminInsertController implements Initializable {

    //buttons
    @FXML
    private Button button_back_search;

    @FXML
    private Button button_submit;

    //choice box
    @FXML
    private ChoiceBox<String> cb_arrival;

    @FXML
    private ChoiceBox<String> cb_departure;

    //datepicker
    @FXML
    private DatePicker dp_departing_date;

    @FXML
    private DatePicker dp_return_date;

    //text field
    @FXML
    private TextField tf_departing_time;

    @FXML
    private TextField tf_duration;

    @FXML
    private TextField tf_price;

    private final String [] stateName = {"Alaska", "Alabama", "Arkansas", "American Samoa", "Arizona", "California", "Colorado", "Connecticut", "District of Columbia", "Delaware", "Florida", "Georgia", "Guam", "Hawaii", "Iowa", "Idaho", "Illinois", "Indiana", "Kansas", "Kentucky", "Louisiana", "Massachusetts", "Maryland", "Maine", "Michigan", "Minnesota", "Missouri", "Mississippi", "Montana", "North Carolina", "North Dakota", "Nebraska", "New Hampshire", "New Jersey", "New Mexico", "Nevada", "New York", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Puerto Rico", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Virginia", "Virgin Islands", "Vermont", "Washington", "Wisconsin", "West Virginia", "Wyoming"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cb_arrival.getItems().addAll(stateName);
        cb_departure.getItems().addAll(stateName);

        button_submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!cb_departure.getValue().isEmpty() && !cb_arrival.getValue().isEmpty() &&
                !tf_departing_time.getText().isEmpty() && !tf_duration.getText().isEmpty() &&
                !tf_price.getText().isEmpty() && !dp_departing_date.getValue().toString().isEmpty() &&
                !dp_return_date.getValue().toString().isEmpty())
                {
                    String[] flightItem = new String[7];
                    flightItem[0] = cb_departure.getValue();
                    flightItem[1] = cb_arrival.getValue();
                    flightItem[2] = tf_departing_time.getText();
                    flightItem[3] = tf_duration.getText();
                    flightItem[4] = tf_price.getText();
                    flightItem[5] = dp_departing_date.getValue().toString();
                    flightItem[6] = dp_return_date.getValue().toString();

                    DBUtils.addNewFlightDetails(event, flightItem);
                    DBUtils.changeScene(event, "AdminFlightsLookUp.fxml", "Admin insert/view/update flight!", null, null );
                }
                else
                {
                    System.out.println("data is missing. Please check the data entered again");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Provided data is incomplete");
                    alert.show();
                }

            }
        });

        button_back_search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "AdminFlightsLookUp.fxml", "Admin insert/view/update flight!", null, null );
            }
        });


    }
}
