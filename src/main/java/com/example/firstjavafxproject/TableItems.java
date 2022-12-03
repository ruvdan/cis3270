package com.example.firstjavafxproject;

public class TableItems {
    String Departure, Arrival, DepartureTime, Duration, Price, DepartingDate, ReturnDate;

    public TableItems(String Departure, String Arrival, String DepartureTime, String Duration, String Price, String DepartingDate, String ReturnDate)
    {
        this.Departure = Departure;
        this.Arrival = Arrival;
        this.DepartureTime = DepartureTime;
        this.Duration = Duration;
        this.Price = Price;
        this.DepartingDate = DepartingDate;
        this.ReturnDate = ReturnDate;
    }

    public String getDeparture(){
        return Departure;
    }
    public void setDeparture(){
        this.Departure = Departure;
    }

    public String getArrival(){
        return Arrival;
    }
    public void setArrival(){
        this.Arrival = Arrival;
    }

    public String getDepartureTime(){
        return DepartureTime;
    }
    public void setDepartureTime(){
        this.DepartureTime = DepartureTime;
    }

    public String getDuration(){
        return Duration;
    }
    public void setDuration(){
        this.Duration = Duration;
    }

    public String getPrice(){
        return Price;
    }
    public void setPrice(){
        this.Price = Price;
    }

    public String getDepartingDate(){
        return DepartingDate;
    }
    public void setDepartingDate(){
        this.DepartingDate = DepartingDate;
    }
    public String getReturnDate(){
        return ReturnDate;
    }
    public void setReturnDate(){
        this.ReturnDate = ReturnDate;
    }
}
