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

public class AdminLoggedInController implements Initializable
{
    private final String [] stateNames = {"Alaska", "Alabama", "Arkansas", "American Samoa", "Arizona", "California", "Colorado", "Connecticut", "District of Columbia", "Delaware", "Florida", "Georgia", "Guam", "Hawaii", "Iowa", "Idaho", "Illinois", "Indiana", "Kansas", "Kentucky", "Louisiana", "Massachusetts", "Maryland", "Maine", "Michigan", "Minnesota", "Missouri", "Mississippi", "Montana", "North Carolina", "North Dakota", "Nebraska", "New Hampshire", "New Jersey", "New Mexico", "Nevada", "New York", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Puerto Rico", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Virginia", "Virgin Islands", "Vermont", "Washington", "Wisconsin", "West Virginia", "Wyoming"};

    //Buttons
    @FXML
    private Button button_logout;
    @FXML
    private Button button_search;
    @FXML
    private Button button_delete;
    @FXML
    private Button button_update;

    //Label
    @FXML
    private Label label_welcome;

    //choiceBox
    @FXML
    private ChoiceBox<String> cb_leaving_from;
    @FXML
    private ChoiceBox<String> cb_going_to;
    @FXML
    private ChoiceBox<String> cb_client_name;

    //datePicker
    @FXML
    private DatePicker dp_departing;

    @FXML
    private DatePicker dp_returning;

    //tableColumn
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
    private TableColumn<AdminTableItems, String> cl_client_name;

    @FXML
    private TableColumn<AdminTableItems, String> cl_user_flight_id;

    @FXML
    private TableColumn<AdminTableItems, String> cl_flight_id;

    //table
    @FXML
    private TableView<AdminTableItems> tb_flights;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String adminUserName = DBUtils.getAdminUserName();
        label_welcome.setText("Welcome to the profile of  "+adminUserName);

        String [] distinctUsers = DBUtils.getClientUser();
        cb_client_name.getItems().addAll(distinctUsers);

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

                ObservableList<AdminTableItems> listItem = FXCollections.observableArrayList();

                String leavingFrom = cb_leaving_from.getValue();
                String goingTo = cb_going_to.getValue();
                String departingDate = dp_departing.getValue().toString();
                String returnDate = dp_returning.getValue().toString();
                String selectedClient = cb_client_name.getValue();

                if(!leavingFrom.trim().isEmpty() && !goingTo.trim().isEmpty()
                        && !departingDate.trim().isEmpty() && !returnDate.trim().isEmpty()
                        && !selectedClient.trim().isEmpty())
                {
                    try
                    {
                        connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
                        preparedStatement = connection.prepareStatement("SELECT Departure, Arrival, DepartureTime, Duration, Price, DepartingDate, ReturnDate, User_Name, User_Flight_id, Flight_id FROM userselectedflightdetails WHERE Departure = ? and Arrival = ? and DepartingDate = ? and ReturnDate = ? and User_Name = ?");
                        preparedStatement.setString(1, leavingFrom);
                        preparedStatement.setString(2, goingTo);
                        preparedStatement.setString(3, departingDate);
                        preparedStatement.setString(4, returnDate);
                        preparedStatement.setString(5, selectedClient);
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.isBeforeFirst()) {
                            System.out.println("No flights found in the database");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("No flights found for the entered information");
                            alert.show();
                        } else {
                            while (resultSet.next()) {
                                listItem.add(new AdminTableItems(resultSet.getString("Departure"),
                                        resultSet.getString("Arrival"),
                                        resultSet.getString("DepartureTime"),
                                        resultSet.getString("Duration"),
                                        resultSet.getString("Price"),
                                        resultSet.getString("DepartingDate"),
                                        resultSet.getString("ReturnDate"),
                                        resultSet.getString("User_Name"),
                                        resultSet.getString("User_Flight_id"),
                                        resultSet.getString("Flight_id")));
                            }
                            cl_arrival.setCellValueFactory(new PropertyValueFactory<>("Arrival"));
                            cl_departure.setCellValueFactory(new PropertyValueFactory<>("Departure"));
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

        button_update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "AdminFlightsLookUp.fxml", "Admin insert/view/update flight!", null, null );
            }

        });


        button_delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AdminTableItems selectedList = tb_flights.getItems().get(tb_flights.getSelectionModel().getSelectedIndex());
                if (!selectedList.User_Flight_id.isEmpty()) {
                    DBUtils.deleteUserFlightDetails(event, selectedList);
                    tb_flights.getItems().remove(selectedList);
                }
            }

        });

        button_logout.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "hello-view.fxml", " Client log in!", null, null );
            }

        });

    }


}
