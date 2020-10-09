package breakthecode.com.clickandgo.entity;

import java.io.Serializable;

public class RideResponse implements Serializable {

    private Ride ride;
    private City cityFrom;
    private City cityTo;

    public RideResponse(){

    }

    public RideResponse(Ride ride, City cityFrom, City cityTo) {
        this.ride = ride;
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
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
}
