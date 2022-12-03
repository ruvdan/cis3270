package com.example.firstjavafxproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AdminFlightsLookUpController implements Initializable {

    //Buttons
    @FXML
    private Button button_delete;
    @FXML
    private Button button_go_back;
    @FXML
    private Button button_update;

    @FXML
    private Button button_insert;

    //table columns
    @FXML
    private TableColumn<FlightItems, String> cl_arrival;

    @FXML
    private TableColumn<FlightItems, String> cl_departing_date;

    @FXML
    private TableColumn<FlightItems, String> cl_departure;

    @FXML
    private TableColumn<FlightItems, String> cl_departure_time;

    @FXML
    private TableColumn<FlightItems, String> cl_duration;

    @FXML
    private TableColumn<FlightItems, String> cl_price;

    @FXML
    private TableColumn<FlightItems, String> cl_return_date;

    @FXML
    private TableColumn<FlightItems, String> cl_Flight_id;

    //table view
    @FXML
    private TableView<FlightItems> tb_flights;

    public static String[] selectedFlightToUpdate = new String[8];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ObservableList<FlightItems> listItem = FXCollections.observableArrayList();

        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
            preparedStatement = connection.prepareStatement("SELECT * FROM flightdetails");
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No flights found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No flights found for the entered information");
                alert.show();
            } else {
                while (resultSet.next()) {
                    listItem.add(new FlightItems(
                            resultSet.getString("Departure"),
                            resultSet.getString("Arrival"),
                            resultSet.getString("DepartureTime"),
                            resultSet.getString("Duration"),
                            resultSet.getString("Price"),
                            resultSet.getString("DepartingDate"),
                            resultSet.getString("ReturnDate"),
                            resultSet.getString("Flight_id")));
                }
                cl_arrival.setCellValueFactory(new PropertyValueFactory<>("Arrival"));
                cl_departure.setCellValueFactory(new PropertyValueFactory<>("Departure"));
                cl_departure_time.setCellValueFactory(new PropertyValueFactory<>("DepartureTime"));
                cl_duration.setCellValueFactory(new PropertyValueFactory<>("Duration"));
                cl_price.setCellValueFactory(new PropertyValueFactory<>("Price"));
                cl_departing_date.setCellValueFactory(new PropertyValueFactory<>("DepartingDate"));
                cl_return_date.setCellValueFactory(new PropertyValueFactory<>("ReturnDate"));
                cl_Flight_id.setCellValueFactory(new PropertyValueFactory<>("Flight_id"));

                tb_flights.setItems(listItem);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(preparedStatement != null)
            {
                try{
                    preparedStatement.close();
                } catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }

            if(connection != null)
            {
                try
                {
                    connection.close();
                } catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }

        }

        button_insert.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "AdminInsert.fxml", "Add new flight!", null, null );
            }
        });


        button_update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlightItems selectedFlight = tb_flights.getItems().get(tb_flights.getSelectionModel().getSelectedIndex());

                selectedFlightToUpdate[0] = selectedFlight.Flight_id;
                selectedFlightToUpdate[1] = selectedFlight.Departure;
                selectedFlightToUpdate[2] = selectedFlight.Arrival;
                selectedFlightToUpdate[3] = selectedFlight.DepartureTime;
                selectedFlightToUpdate[4] = selectedFlight.Duration;
                selectedFlightToUpdate[5] = selectedFlight.Price;
                selectedFlightToUpdate[6] = selectedFlight.DepartingDate;
                selectedFlightToUpdate[7] = selectedFlight.ReturnDate;

                DBUtils.changeScene(event, "AdminUpdate.fxml", "Update selected flight!", null, null );
            }
        });

        button_delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlightItems selectedFlight = tb_flights.getItems().get(tb_flights.getSelectionModel().getSelectedIndex());
                if (!selectedFlight.Flight_id.isEmpty()) {
                    DBUtils.deleteFlightDetails(event, selectedFlight);
                    tb_flights.getItems().remove(selectedFlight);
                }
            }
        });

        button_go_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "AdminLogged-in.fxml", "user selected flight!", null, null );
            }
        });
    }

    public static String[] getSelectFlightToUpdate(){
        return selectedFlightToUpdate;
    }
}
