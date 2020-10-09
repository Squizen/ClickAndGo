package breakthecode.com.clickandgo.entity;

import java.io.Serializable;
import java.sql.Time;

public class Ride implements Serializable {

    private int id;
    private int idCityFrom;
    private int idCityTo;
    private double price;
    private Time rideTime;

    public Ride(){}

    public Ride(int id, int idCityFrom, int idCityTo, double price, Time rideTime) {
        this.id = id;
        this.idCityFrom = idCityFrom;
        this.idCityTo = idCityTo;
        this.price = price;
        this.rideTime = rideTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCityFrom() {
        return idCityFrom;
    }

    public void setIdCityFrom(int idCityFrom) {
        this.idCityFrom = idCityFrom;
    }

    public int getIdCityTo() {
        return idCityTo;
    }

    public void setIdCityTo(int idCityTo) {
        this.idCityTo = idCityTo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Time getRideTime() {
        return rideTime;
    }

    public void setRideTime(Time rideTime) {
        this.rideTime = rideTime;
    }
}
