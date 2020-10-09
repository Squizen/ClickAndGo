package breakthecode.com.clickandgo.entity;

import java.sql.Date;
import java.sql.Time;

public class Ticket {

    private int idTicket;
    private int idRide;
    private String ticketNumber;
    private Date purchaseDate;
    private Time purchaseTime;
    private Date rideDate;
    private String ownerName;
    private String ownerSurname;
    private String ownerEmail;
    private Date expiringDate;
    private Time expiringTime;

    public Ticket(){

    }

    public Ticket(int idTicket, int idRide, String ticketNumber, Date purchaseDate, Time purchaseTime, Date rideDate, String ownerName, String ownerSurname, String ownerEmail, Date expiringDate, Time expiringTime) {
        this.idTicket = idTicket;
        this.idRide = idRide;
        this.ticketNumber = ticketNumber;
        this.purchaseDate = purchaseDate;
        this.purchaseTime = purchaseTime;
        this.rideDate = rideDate;
        this.ownerName = ownerName;
        this.ownerSurname = ownerSurname;
        this.ownerEmail = ownerEmail;
        this.expiringDate = expiringDate;
        this.expiringTime = expiringTime;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdRide() {
        return idRide;
    }

    public void setIdRide(int idRide) {
        this.idRide = idRide;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Time getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Time purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public Date getRideDate() {
        return rideDate;
    }

    public void setRideDate(Date rideDate) {
        this.rideDate = rideDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerSurname() {
        return ownerSurname;
    }

    public void setOwnerSurname(String ownerSurname) {
        this.ownerSurname = ownerSurname;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public Date getExpiringDate() {
        return expiringDate;
    }

    public void setExpiringDate(Date expiringDate) {
        this.expiringDate = expiringDate;
    }

    public Time getExpiringTime() {
        return expiringTime;
    }

    public void setExpiringTime(Time expiringTime) {
        this.expiringTime = expiringTime;
    }
}
