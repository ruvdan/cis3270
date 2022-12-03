package com.example.firstjavafxproject;

public class AdminTableItems {
    String Departure, Arrival, DepartureTime, Duration, Price, DepartingDate, ReturnDate, User_Name, User_Flight_id, Flight_id;

    public AdminTableItems(String Departure, String Arrival, String DepartureTime, String Duration, String Price, String DepartingDate, String ReturnDate, String User_Name, String User_Flight_id, String Flight_id) {
        this.Departure = Departure;
        this.Arrival = Arrival;
        this.DepartureTime = DepartureTime;
        this.Duration = Duration;
        this.Price = Price;
        this.DepartingDate = DepartingDate;
        this.ReturnDate = ReturnDate;
        this.User_Name = User_Name;
        this.User_Flight_id = User_Flight_id;
        this.Flight_id = Flight_id;
    }

    public String getDeparture() {
        return Departure;
    }

    public void setDeparture() {
        this.Departure = Departure;
    }

    public String getArrival() {
        return Arrival;
    }

    public void setArrival() {
        this.Arrival = Arrival;
    }

    public String getDepartureTime() {
        return DepartureTime;
    }

    public void setDepartureTime() {
        this.DepartureTime = DepartureTime;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration() {
        this.Duration = Duration;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice() {
        this.Price = Price;
    }

    public String getDepartingDate() {
        return DepartingDate;
    }

    public void setDepartingDate() {
        this.DepartingDate = DepartingDate;
    }

    public String getReturnDate() {
        return ReturnDate;
    }

    public void setReturnDate() {
        this.ReturnDate = ReturnDate;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name() {
        this.User_Name = User_Name;
    }

    public String getUser_Flight_id() {
        return User_Flight_id;
    }

    public void setUser_Flight_id() {
        this.User_Flight_id = User_Flight_id;
    }


    public String getFlight_id() {
        return Flight_id;
    }

    public void setFlight_id() {
        this.Flight_id = Flight_id ;
    }



}
