package breakthecode.com.clickandgo.resthelpers;

import java.sql.Date;
import java.sql.Time;

import breakthecode.com.clickandgo.entity.City;

public class RideRequestParameters {

    private static RideRequestParameters instanceOfRideRequestParameter = null;

    private boolean isCityFromPicked;
    private City cityFrom;

    private boolean isCityToPicked;
    private City cityTo;

    private Time timeOfRide;
    private Date dateOfRide;

    private RideRequestParameters(){

    }

    public static RideRequestParameters getInstance(){
        if(instanceOfRideRequestParameter == null){
            instanceOfRideRequestParameter = new RideRequestParameters();
        }
        return instanceOfRideRequestParameter;
    }

    public static void setInstanceOfRideRequestParameter(RideRequestParameters instanceOfRideRequestParameter) {
        RideRequestParameters.instanceOfRideRequestParameter = instanceOfRideRequestParameter;
    }

    public boolean isCityFromPicked() {
        return isCityFromPicked;
    }

    public void setCityFromPicked(boolean cityFromPicked) {
        isCityFromPicked = cityFromPicked;
    }

    public City getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(City cityFrom) {
        this.cityFrom = cityFrom;
    }

    public boolean isCityToPicked() {
        return isCityToPicked;
    }

    public void setCityToPicked(boolean cityToPicked) {
        isCityToPicked = cityToPicked;
    }

    public City getCityTo() {
        return cityTo;
    }

    public void setCityTo(City cityTo) {
        this.cityTo = cityTo;
    }

    public Time getTimeOfRide() {
        return timeOfRide;
    }

    public void setTimeOfRide(Time timeOfRide) {
        this.timeOfRide = timeOfRide;
    }

    public Date getDateOfRide() {
        return dateOfRide;
    }

    public void setDateOfRide(Date dateOfRide) {
        this.dateOfRide = dateOfRide;
    }
}
