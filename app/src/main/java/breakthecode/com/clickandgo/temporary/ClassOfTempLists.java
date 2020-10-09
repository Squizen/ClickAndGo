package breakthecode.com.clickandgo.temporary;

import java.util.ArrayList;
import java.util.List;

import breakthecode.com.clickandgo.entity.City;
import breakthecode.com.clickandgo.entity.Ride;

public class ClassOfTempLists {

    private List<Ride> listOfRides;
    private List<City> listOfCities;

    public List<Ride> generateFictionalListOfRides(){
        listOfRides = new ArrayList<>();
        return listOfRides;
    }
    public List<City> generateFictionalListOfCities(){
        /*
            City
                private int id;
                private String cityName;
                private String busStopName;
         */
        listOfCities = new ArrayList<>();
        listOfCities.add(new City(0, "Rzeszów", "ul. Dupa 27 - Dworzec Autobusowy"));
        listOfCities.add(new City(1, "Mielec", "ul. Zamachu na Kurczaczka - Dworzec Autobusowy"));
        listOfCities.add(new City(2, "Tarnobrzeg", "ul. S. Suczkowskiej 12 - Dworzec Autobusowy"));
        listOfCities.add(new City(3, "Stalowa Wola", "ul.Okulickiego 3 - Dworzec Autobusowy"));
        listOfCities.add(new City(4, "Mielec", "ul. Jagielończyka 7 - Dworzec autobusowy"));
        listOfCities.add(new City(5, "Dębica", "ul. Głowackiego 14 - Dworzec Autobusowy"));
        listOfCities.add(new City(6, "Leżajsk", "ul. Siala 23 - Dworzec Autobusowy"));
        listOfCities.add(new City(7, "San Francisco", "ul. Generała Franco 2 - Dworzec Autobusowy"));
        listOfCities.add(new City(8, "Jedlicze", "ul. Jagielońska 5 - Dworzec Autobusowy"));
        listOfCities.add(new City(9, "Pilzno", "Rynek - Dworzec Autobusowy"));
        return listOfCities;
    }
}
