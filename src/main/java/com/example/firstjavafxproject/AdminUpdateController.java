package com.example.firstjavafxproject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AdminUpdateController implements Initializable {

    //buttons
    @FXML
    private Button button_back_search;

    @FXML
    private Button button_update;

    //choicebox
    @FXML
    private ChoiceBox<String> cb_arrival;

    @FXML
    private ChoiceBox<String> cb_departure;

    //datepicker
    @FXML
    private DatePicker dp_departing_date;

    @FXML
    private DatePicker dp_return_date;

    //label
    @FXML
    private Label lb_selected_flight;

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
        String[] selectedFlight = AdminFlightsLookUpController.getSelectFlightToUpdate();

        lb_selected_flight.setText("Update selected flight details for flight Id: "+selectedFlight[0]);
        cb_departure.setValue(selectedFlight[1]);
        cb_arrival.setValue(selectedFlight[2]);
        tf_departing_time.setText(selectedFlight[3]);
        tf_duration.setText(selectedFlight[4]);
        tf_price.setText(selectedFlight[5]);

        DateTimeFormatter customDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate departingDate = LocalDate.parse(selectedFlight[6], customDateTimeFormatter);
        LocalDate returningDate = LocalDate.parse(selectedFlight[7], customDateTimeFormatter);

        dp_departing_date.setValue(departingDate);
        dp_return_date.setValue(returningDate);

        button_update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {

                if(!cb_departure.getValue().isEmpty() && !cb_arrival.getValue().isEmpty() &&
                        !tf_departing_time.getText().isEmpty() && !tf_duration.getText().isEmpty() &&
                        !tf_price.getText().isEmpty() && !dp_departing_date.getValue().toString().isEmpty() &&
                        !dp_return_date.getValue().toString().isEmpty())
                {
                    String[] flightItem = new String[8];
                    flightItem[0] = cb_departure.getValue();
                    flightItem[1] = cb_arrival.getValue();
                    flightItem[2] = tf_departing_time.getText();
                    flightItem[3] = tf_duration.getText();
                    flightItem[4] = tf_price.getText();
                    flightItem[5] = dp_departing_date.getValue().toString();
                    flightItem[6] = dp_return_date.getValue().toString();
                    flightItem[7] = selectedFlight[0];

                    DBUtils.updatedByAdminFlightDetails(event, flightItem);

                    DBUtils.changeScene(event, "AdminFlightsLookUp.fxml", "Admin insert/view/update flight!", null, null );
                }

                else {
                    System.out.println("Couldn't update the provided flight details");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Couldn't update the provided flight details");
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
