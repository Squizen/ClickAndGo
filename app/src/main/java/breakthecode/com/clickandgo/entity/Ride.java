package breakthecode.com.clickandgo.entity;

import java.io.Serializable;
import java.sql.Time;

public class Ride implements Serializable {

    private int id;
    private City cityFrom;
    private City cityTo;
    private double price;
    private Time rideTime;

    public Ride(){}

    public Ride(int id, City cityFrom, City cityTo, double price, Time rideTime) {
        this.id = id;
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.price = price;
        this.rideTime = rideTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public City getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(City cityFrom) {
        this.cityFrom = cityFrom;
    }

    public City getCityTo() {
        return cityTo;
    }

    public void setCityTo(City cityTo) {
        this.cityTo = cityTo;
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
