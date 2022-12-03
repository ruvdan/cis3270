package com.example.firstjavafxproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable{
    private final String [] stateNames = {"Alaska", "Alabama", "Arkansas", "American Samoa", "Arizona", "California", "Colorado", "Connecticut", "District of Columbia", "Delaware", "Florida", "Georgia", "Guam", "Hawaii", "Iowa", "Idaho", "Illinois", "Indiana", "Kansas", "Kentucky", "Louisiana", "Massachusetts", "Maryland", "Maine", "Michigan", "Minnesota", "Missouri", "Mississippi", "Montana", "North Carolina", "North Dakota", "Nebraska", "New Hampshire", "New Jersey", "New Mexico", "Nevada", "New York", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Puerto Rico", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Virginia", "Virgin Islands", "Vermont", "Washington", "Wisconsin", "West Virginia", "Wyoming"};

    @FXML
    private Button button_logout;

    @FXML
    private Button button_check_profile;

    @FXML
    private Button button_search;
    @FXML
    private Label label_welcome;

    // if needed un-comment and use
   /* @FXML
    private Label label_leaving_from;

    @FXML
    private Label label_going_go;*/

    @FXML
    private ChoiceBox<String> cb_leaving_from;


    @FXML
    private ChoiceBox<String> cb_going_to;

    // if needed un-comment and use
    /*@FXML
    private Label label_departing;
    @FXML
    private Label label_returning;*/

    @FXML
    private DatePicker dp_departing;

    @FXML
    private DatePicker dp_returning;

    @FXML
    private TableColumn<FlightItems, String> cl_arrival;

    @FXML
    private TableColumn<FlightItems, String> cl_departure;

    @FXML
    private TableColumn<FlightItems, String> cl_departure_time;

    @FXML
    private TableColumn<FlightItems, String> cl_duration;

    @FXML
    private TableColumn<FlightItems, String> cl_price;

    @FXML
    private TableColumn<FlightItems, String> cl_departing_date;

    @FXML
    private TableColumn<FlightItems, String> cl_return_date;

    @FXML
    private TableColumn<FlightItems, String> cl_flight_id;

    @FXML
    private Button button_add_list;

    @FXML
    private Button button_check_out;


    @FXML
    private TableView<FlightItems> tb_flights;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList <FlightItems> listItem = FXCollections.observableArrayList();

        cb_leaving_from.getItems().addAll(stateNames);
        cb_going_to.getItems().addAll(stateNames);

        button_search.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                String leavingFrom = cb_leaving_from.getValue();
                String goingTo = cb_going_to.getValue();
                String departingDate = dp_departing.getValue().toString();
                String returnDate = dp_returning.getValue().toString();

                if(!leavingFrom.trim().isEmpty() && !goingTo.trim().isEmpty()
                && !departingDate.trim().isEmpty() && !returnDate.trim().isEmpty() && !leavingFrom.equals(goingTo))
                {
                    try
                    {
                        connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
                        preparedStatement = connection.prepareStatement("SELECT * FROM flightdetails WHERE Departure = ? and Arrival = ? and DepartingDate = ? and ReturnDate = ? ");
                        preparedStatement.setString(1, leavingFrom);
                        preparedStatement.setString(2, goingTo);
                        preparedStatement.setString(3, departingDate);
                        preparedStatement.setString(4, returnDate);
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
                            cl_flight_id.setCellValueFactory(new PropertyValueFactory<>("Flight_id"));

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
                }
                else
                {
                    System.out.println("Please add all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to find flights!");
                    alert.show();
                }
            }
        });

        tb_flights.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FlightItems selectedList = tb_flights.getItems().get(tb_flights.getSelectionModel().getSelectedIndex());
                if (!selectedList.Arrival.isEmpty()) {
                    DBUtils.addFlightDetails(event, selectedList);
                }
                else
                {
                    System.out.println("No flight was selected");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please select a flight to be added!");
                    alert.show();
                }
            }
        });


        button_check_profile.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "UserProfile.fxml", "User Profile!", null, null );
            }

        });


        button_logout.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "hello-view.fxml", "Client log in!", null, null );
            }

        });


    }

    public void setUserInformation(String username)
    {
        label_welcome.setText("Welcome "+username);
    }
}
