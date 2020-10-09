package breakthecode.com.clickandgo.entity;

import java.io.Serializable;

public class City implements Serializable {

    private int id;
    private String cityName;
    private String busStopName;

    public City(){

    }

    public City(int id, String cityName, String busStopName) {
        this.id = id;
        this.cityName = cityName;
        this.busStopName = busStopName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", busStopName='" + busStopName + '\'' +
                '}';
    }
}
