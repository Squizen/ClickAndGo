package breakthecode.com.clickandgo.interfaces;

import java.util.ArrayList;
import java.util.List;

import breakthecode.com.clickandgo.entity.City;
import breakthecode.com.clickandgo.entity.UserTicket;

public interface SharedPreferencesHelper {

    boolean isCityFromPicked();

    void setCityFromPicked(boolean isPicked);

    boolean isCityToPicked();

    void setCityToPicked(boolean isPicked);

    void setListOfCitiesFromRequest(List<City> listOfCities);

    List<City> getListOfCitiesFromRequest();

    ArrayList<UserTicket> getListOfUsersTickets();

    void setListOfUsersTickets(ArrayList<UserTicket> listOfUsersTickets);

}
