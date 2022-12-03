package com.example.firstjavafxproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.sql.*;

import java.io.IOException;

public class DBUtils {

   private static String UNAME;

    private static String ADMINUNAME;

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String favChannel)
    {
        Parent root = null;
        if(username != null && favChannel != null)
        {
            UNAME = username;
            try
            {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root  = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(username);

            } catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try{
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch(IOException e )
            {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);

        switch(fxmlFile)
        {
            case "Sign-up.fxml":
                stage.setScene(new Scene(root, 600, 725));
                break;

            case "Logged-in.fxml":
                stage.setScene(new Scene(root, 910, 527));
                break;

            case "UserProfile.fxml":
                stage.setScene(new Scene(root, 1019, 406));
                break;

            case "AdminLogged-in.fxml":
                stage.setScene(new Scene(root, 1034, 496));
                break;

            case "AdminFlightsLookUp.fxml":
                stage.setScene(new Scene(root, 815, 366));
                break;

            case "AdminInsert.fxml":
            case "AdminUpdate.fxml":
                stage.setScene(new Scene(root, 690, 384));
                break;

            default:
                stage.setScene(new Scene(root, 600, 400));
                break;
        }

        stage.show();
    }

    public static void signUpUser(ActionEvent event, String[] dbInsertValues)
    {
        Connection connection = null;
        PreparedStatement  psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultset = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ? and email = ?");
            psCheckUserExists.setString(1, dbInsertValues[0]);// username
            psCheckUserExists.setString(2, dbInsertValues[13]);//email
            resultset = psCheckUserExists.executeQuery();

            if(resultset.isBeforeFirst())
            {
                System.out.println("User already Exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("This user already exists!");
                alert.show();
            }
            else
            {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, favChannel, firstName, lastName, addressLine_1, addressLine_2, city, state, zipcode, LastFourOfSSN, securityQuestion, securityAnswer, email ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, dbInsertValues[0]);//username
                psInsert.setString(2, dbInsertValues[1]); //password
                psInsert.setString(3, dbInsertValues[2]); //favChannel
                psInsert.setString(4, dbInsertValues[3]); // first name
                psInsert.setString(5, dbInsertValues[4]); // last name
                psInsert.setString(6, dbInsertValues[5]); // address1
                psInsert.setString(7, dbInsertValues[6]); //address2
                psInsert.setString(8, dbInsertValues[7]); // city
                psInsert.setString(9, dbInsertValues[8]); // state
                psInsert.setString(10, dbInsertValues[9]); // zipcode
                psInsert.setString(11, dbInsertValues[10]); // ssn
                psInsert.setString(12, dbInsertValues[11]); //securityQuestion
                psInsert.setString(13, dbInsertValues[12] ); //answer
                psInsert.setString(14, dbInsertValues[13] ); //email

                psInsert.executeUpdate();

                System.out.println("New user Profile is created");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("New user Profile is created");
                alert.show();
                changeScene(event, "Logged-in.fxml", "Flight Details!", dbInsertValues[0], dbInsertValues[2]);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }finally{
            if(resultset != null)
            {
                try
                {
                    resultset.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }

            if(psCheckUserExists != null)
            {
                try{
                    psCheckUserExists.close();
                } catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }

            if(psInsert != null)
            {
                try{
                    psInsert.close();
                }catch(SQLException e)
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

    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
            preparedStatement = connection.prepareStatement("SELECT password, favChannel FROM users WHERE username = ? ");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    String retrievedChannel = resultSet.getString("favChannel");
                    if (retrievedPassword.equals(password)) {
                        changeScene(event, "Logged-in.fxml", "Flight Details!", username, retrievedChannel);
                    } else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect!");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

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

    public static void AdminlogInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
            preparedStatement = connection.prepareStatement("SELECT password FROM adminuser WHERE username = ? ");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            } else {
                while (resultSet.next()) {
                    ADMINUNAME = username;
                    String retrievedPassword = resultSet.getString("password");
                    if (retrievedPassword.equals(password)) {
                        changeScene(event, "AdminLogged-in.fxml", "Admin flight Details!", username, null);
                    } else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect!");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

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

    public static void addFlightDetails(MouseEvent event, FlightItems selectedList)
    {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckFlightsExists = null;
        ResultSet resultSet = null;


            try {
                connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
                psCheckFlightsExists = connection.prepareStatement("SELECT * FROM userselectedflightdetails WHERE User_Name = ? and Flight_id = ?");
                psCheckFlightsExists.setString(1, UNAME);
                psCheckFlightsExists.setString(2, selectedList.Flight_id);
                resultSet = psCheckFlightsExists.executeQuery();

                if(resultSet.isBeforeFirst())
                {
                    System.out.println("The selected flight was already added!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("The selected flight was already added!");
                    alert.show();
                }
                else
                {
                    psInsert = connection.prepareStatement("INSERT INTO userselectedflightdetails (Departure, Arrival, DepartureTime, Duration, Price, DepartingDate, ReturnDate, User_Name, Flight_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    psInsert.setString(1, selectedList.Departure);
                    psInsert.setString(2, selectedList.Arrival);
                    psInsert.setString(3, selectedList.DepartureTime);
                    psInsert.setString(4, selectedList.Duration);
                    psInsert.setString(5, selectedList.Price);
                    psInsert.setString(6, selectedList.DepartingDate);
                    psInsert.setString(7, selectedList.ReturnDate);
                    psInsert.setString(8, UNAME);
                    psInsert.setString(9, selectedList.Flight_id);
                    psInsert.executeUpdate();

                    System.out.println("The selected flight was added");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("The selected flight was added");
                    alert.show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally
            {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(psCheckFlightsExists != null)
                {
                    try{
                        psCheckFlightsExists.close();
                    }catch(SQLException e)
                    {
                        e.printStackTrace();
                    }
                }

                if(psInsert != null)
                {
                    try{
                        psInsert.close();
                    }catch(SQLException e)
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

    public static String[] getClientUser()
    {
            String[] distinctUsers = new String[10];
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultset = null;
            int count =0;

            try{
                connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
                preparedStatement = connection.prepareStatement("SELECT DISTINCT username FROM users");
                resultset = preparedStatement.executeQuery();

                while (resultset.next()) {
                    distinctUsers[count] = resultset.getString("username");
                    count++;
                }
            }catch(SQLException e) {
                e.printStackTrace();
            }finally {
                if (resultset != null) {
                    try {
                        resultset.close();
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
            return distinctUsers;
    }

    /*public static ObservableList<AdminTableItems> getAdminTableItems (ActionEvent event, String leavingFrom,String goingTo,String departingDate, String returnDate, String selectedClient) {
        ObservableList<AdminTableItems> listItem = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
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
                return listItem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
        return null;
    }*/

    public static void deleteUserFlightDetails(ActionEvent event, AdminTableItems selectedList)
    {
        Connection connection = null;
        PreparedStatement psDelete = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
            psDelete = connection.prepareStatement("DELETE FROM userselectedflightdetails WHERE User_Flight_id = ? ");
            psDelete.setString(1, selectedList.User_Flight_id);
            psDelete.executeUpdate();

            System.out.println("The selected flight was deleted");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("The selected flight was deleted");
            alert.show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally
        {
            if(psDelete != null)
            {
                try{
                    psDelete.close();
                }catch(SQLException e)
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

    public static String getSecurityQuestion(ActionEvent event, String username, String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String retrievedQuestion = "";
        try {

            connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
            preparedStatement = connection.prepareStatement("SELECT securityQuestion FROM users WHERE username = ? and email = ? ");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No security question found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided user details are incorrect");
                alert.show();
            } else {
                while (resultSet.next()) {
                    retrievedQuestion  = resultSet.getString("securityQuestion");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

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
        return retrievedQuestion;
    }


    public static String getPassword(ActionEvent event, String answer, String username, String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String retrievedAnswer = "";
        try {

            connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
            preparedStatement = connection.prepareStatement("SELECT password FROM users WHERE securityAnswer = ? and username = ? and email = ? ");
            preparedStatement.setString(1, answer);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, email);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No security answer found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided answer is incorrect");
                alert.show();
            } else {
                while (resultSet.next()) {
                    retrievedAnswer  = resultSet.getString("password");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

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
        return retrievedAnswer;
    }

    public static void deleteFlightDetails(ActionEvent event, FlightItems selectedList)
    {
        Connection connection = null;
        PreparedStatement psDelete = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
            psDelete = connection.prepareStatement("DELETE FROM flightdetails WHERE Flight_id = ? ");
            psDelete.setString(1, selectedList.Flight_id);
            psDelete.executeUpdate();

            System.out.println("The selected flight was deleted");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("The selected flight was deleted");
            alert.show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally
        {
            if(psDelete != null)
            {
                try{
                    psDelete.close();
                }catch(SQLException e)
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

    public static void addNewFlightDetails(ActionEvent event, String[] newFlight)
    {
        Connection connection = null;
        PreparedStatement psInsert = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
            psInsert = connection.prepareStatement("INSERT INTO flightdetails (Departure, Arrival, DepartureTime, Duration, Price, DepartingDate, ReturnDate) VALUES (?, ?, ?, ?, ?, ?, ?)");
            psInsert.setString(1, newFlight[0]);
            psInsert.setString(2, newFlight[1]);
            psInsert.setString(3, newFlight[2]);
            psInsert.setString(4, newFlight[3]);
            psInsert.setString(5, "$"+newFlight[4]);
            psInsert.setString(6, newFlight[5]);
            psInsert.setString(7, newFlight[6]);
            psInsert.executeUpdate();

            System.out.println("The new flight was added");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("The new flight was added");
            alert.show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally
        {
            if(psInsert != null)
            {
                try{
                    psInsert.close();
                }catch(SQLException e)
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

    public static void updatedByAdminFlightDetails(ActionEvent event, String[]  updatedFlight)
    {
        Connection connection = null;
        PreparedStatement psUpdate = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-06.cleardb.net:3306/heroku_01516907bacf734", "bf3a104e8e9f60", "d1fb75c2");
            psUpdate = connection.prepareStatement("UPDATE flightdetails SET Departure= ?, Arrival= ? , DepartureTime= ? , Duration= ?, Price = ?, DepartingDate = ?, ReturnDate = ? WHERE Flight_id = ?");
            psUpdate.setString(1, updatedFlight[0]);
            psUpdate.setString(2, updatedFlight[1]);
            psUpdate.setString(3, updatedFlight[2]);
            psUpdate.setString(4, updatedFlight[3]);
            psUpdate.setString(5, updatedFlight[4]);
            psUpdate.setString(6, updatedFlight[5]);
            psUpdate.setString(7, updatedFlight[6]);
            psUpdate.setString(8, updatedFlight[7]);
            psUpdate.executeUpdate();

            System.out.println("The flight is updated");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("The flight is updated ");
            alert.show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally
        {
            if(psUpdate != null)
            {
                try{
                    psUpdate.close();
                }catch(SQLException e)
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
    public static String getUserName ()
    {
        if(!UNAME.isEmpty())
            return UNAME;
        return null;
    }

    public static String getAdminUserName ()
    {
        if(!ADMINUNAME.isEmpty())
            return ADMINUNAME;
        return null;
    }

}
