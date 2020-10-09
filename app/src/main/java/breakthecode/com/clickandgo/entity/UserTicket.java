package breakthecode.com.clickandgo.entity;

public class UserTicket {

    private City cityFrom;
    private City cityTo;
    private Ticket ticket;

    public UserTicket(){

    }

    public UserTicket(City cityFrom, City cityTo, Ticket ticket) {
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.ticket = ticket;
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

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
