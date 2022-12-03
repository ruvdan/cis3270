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

public class UserProfileController implements Initializable {

    //labels
    @FXML
    private Label label_welcome_profile;

    //buttons
    @FXML
    private Button button_logout;

    @FXML
    private Button button_logged_in;

    @FXML
    private Button button_delete;

    //table columns
    @FXML
    private TableColumn<AdminTableItems, String> cl_arrival;

    @FXML
    private TableColumn<AdminTableItems, String> cl_departure;

    @FXML
    private TableColumn<AdminTableItems, String> cl_departure_time;

    @FXML
    private TableColumn<AdminTableItems, String> cl_duration;

    @FXML
    private TableColumn<AdminTableItems, String> cl_price;

    @FXML
    private TableColumn<AdminTableItems, String> cl_departing_date;

    @FXML
    private TableColumn<AdminTableItems, String> cl_return_date;

    @FXML
    private TableColumn<AdminTableItems, String> cl_user_flight_id;

    @FXML
    private TableColumn<AdminTableItems, String> cl_client_name;

    @FXML
    private TableColumn<AdminTableItems, String> cl_flight_id;

    //table view
    @FXML
    private TableView<AdminTableItems> tb_flights;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String userName = DBUtils.getUserName();
        label_welcome_profile.setText("Welcome to the profile of "+userName);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<AdminTableItems> listItem = FXCollections.observableArrayList();

        if(!userName.isEmpty())
        {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
                preparedStatement = connection.prepareStatement("SELECT Departure, Arrival, DepartureTime, Duration, Price, DepartingDate, ReturnDate, User_Name, User_Flight_id, Flight_id FROM userselectedflightdetails WHERE User_Name = ? ");
                preparedStatement.setString(1, userName);
                resultSet = preparedStatement.executeQuery();

                if (!resultSet.isBeforeFirst())
                {
                    System.out.println("No flights found in the database");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("No flights found for the entered information");
                    alert.show();
                }
                else
                {
                    while (resultSet.next())
                    {
                        listItem.add(new AdminTableItems
                                (
                                resultSet.getString("Departure"),
                                resultSet.getString("Arrival"),
                                resultSet.getString("DepartureTime"),
                                resultSet.getString("Duration"),
                                resultSet.getString("Price"),
                                resultSet.getString("DepartingDate"),
                                resultSet.getString("ReturnDate"),
                                resultSet.getString("User_Name"),
                                resultSet.getString("User_Flight_id"),
                                resultSet.getString("Flight_id")
                                ));
                    }

                    cl_departure.setCellValueFactory(new PropertyValueFactory<>("Departure"));
                    cl_arrival.setCellValueFactory(new PropertyValueFactory<>("Arrival"));
                    cl_departure_time.setCellValueFactory(new PropertyValueFactory<>("DepartureTime"));
                    cl_duration.setCellValueFactory(new PropertyValueFactory<>("Duration"));
                    cl_price.setCellValueFactory(new PropertyValueFactory<>("Price"));
                    cl_departing_date.setCellValueFactory(new PropertyValueFactory<>("DepartingDate"));
                    cl_return_date.setCellValueFactory(new PropertyValueFactory<>("ReturnDate"));
                    cl_client_name.setCellValueFactory(new PropertyValueFactory<>("User_Name"));
                    cl_user_flight_id.setCellValueFactory(new PropertyValueFactory<>("User_Flight_id"));
                    cl_flight_id.setCellValueFactory(new PropertyValueFactory<>("Flight_id"));

                    tb_flights.setItems(listItem);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            finally {

                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        button_delete.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                AdminTableItems selectedList = tb_flights.getItems().get(tb_flights.getSelectionModel().getSelectedIndex());
                if (!selectedList.User_Flight_id.isEmpty()) {
                    DBUtils.deleteUserFlightDetails(event, selectedList);
                    tb_flights.getItems().remove(selectedList);
                }
            }

        });

        button_logged_in.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "Logged-in.fxml", "Flight Details!", null, null );
            }

        });

        button_logout.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "hello-view.fxml", "log in!", null, null );
            }

        });

    }




}
