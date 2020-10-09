package breakthecode.com.clickandgo.classes;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import breakthecode.com.clickandgo.entity.City;
import breakthecode.com.clickandgo.entity.UserTicket;
import breakthecode.com.clickandgo.interfaces.SharedPreferencesHelper;

public class AppSharedPreferencesHelper implements SharedPreferencesHelper {

    private static final String PREF_CITY_FROM = "PREF_CITY_FROM";
    private static final String PREF_CITY_TO = "PREF_CITY_TO";
    private static final String LIST_OF_CITIES = "LIST_OF_CITIES";
    private static final String LIST_OF_ALL_CITIES = "LIST_OF_ALL_CITIES";
    private static final String LIST_OF_USERS_TICKETS = "LIST_OF_USERS_TICKETS";

    private final SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;

    public AppSharedPreferencesHelper(Context context, String prefFileName){
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isCityFromPicked() {
        return mPrefs.getBoolean(PREF_CITY_FROM, false);
    }

    @Override
    public void setCityFromPicked(boolean isPicked) {
        mPrefs.edit().putBoolean(PREF_CITY_FROM, isPicked);
    }

    @Override
    public boolean isCityToPicked() {
        return mPrefs.getBoolean(PREF_CITY_TO, false);
    }

    @Override
    public void setCityToPicked(boolean isPicked) {
        mPrefs.edit().putBoolean(PREF_CITY_TO, isPicked);
    }

    @Override
    public void setListOfCitiesFromRequest(List<City> listOfCities) {
        editor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listOfCities);
        editor.putString(LIST_OF_CITIES, json);
        editor.apply();
    }

    @Override
    public List<City> getListOfCitiesFromRequest() {
        Gson gson = new Gson();
        String json = mPrefs.getString(LIST_OF_CITIES, null);
        Type type = new TypeToken<List<City>>() {}.getType();
        List<City> listOfCities = gson.fromJson(json, type);
        if(listOfCities == null){
            listOfCities = new ArrayList<City>();
        }
        return listOfCities;
    }

    @Override
    public ArrayList<UserTicket> getListOfUsersTickets() {
        Gson gson = new Gson();
        String json = mPrefs.getString(LIST_OF_USERS_TICKETS, null);
        Type type = new TypeToken<List<UserTicket>>() {}.getType();
        ArrayList<UserTicket> listOfUserTickets = gson.fromJson(json, type);
        if(listOfUserTickets == null){
            listOfUserTickets = new ArrayList<UserTicket>();
        }
        return listOfUserTickets;
    }

    @Override
    public void setListOfUsersTickets(ArrayList<UserTicket> listOfUsersTickets) {
        editor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listOfUsersTickets);
        editor.putString(LIST_OF_USERS_TICKETS, json);
        editor.apply();
    }

}
