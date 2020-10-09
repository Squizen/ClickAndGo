package breakthecode.com.clickandgo.resthelpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import breakthecode.com.clickandgo.classes.AppSharedPreferencesHelper;
import breakthecode.com.clickandgo.entity.City;

public class RequestService {
    private static final String TAG = "myLogs RequestServ";

    private static String SERVER_URL = "http://ssh-vps.nazwa.pl:8080";
    private static final String CHARSET = "UTF-8";
    private AppSharedPreferencesHelper sharedPrefs;
    private List<City> listOfCities;

    private Context context;

    public RequestService(Context context){
        this.context = context;
    }

    // Method is sending request for all cities and saving result in sharedpreferences
    public void requestListOfAllCities(){
        SERVER_URL = SERVER_URL + "/api/cities";
        listOfCities = new ArrayList<>();
        sharedPrefs = new AppSharedPreferencesHelper(context, "Shared_Preferences");
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, SERVER_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject songObject = response.getJSONObject(i);
                        City city = new City();
                        city.setId(songObject.getInt("id"));
                        city.setCityName(songObject.getString("cityName"));
                        city.setBusStopName(songObject.getString("busStopName"));

                        listOfCities.add(city);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                sharedPrefs.setListOfCitiesFromRequest(listOfCities);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);
    }

    // Method is sending request for all cities and saving result in sharedpreferences
    public void requestListOfCitiesThatRidesGoesFrom(){
        SERVER_URL = SERVER_URL + "/api/cities/from";
        listOfCities = new ArrayList<>();
        sharedPrefs = new AppSharedPreferencesHelper(context, "Shared_Preferences");
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, SERVER_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject songObject = response.getJSONObject(i);
                        City city = new City();
                        city.setId(songObject.getInt("id"));
                        city.setCityName(songObject.getString("cityName"));
                        city.setBusStopName(songObject.getString("busStopName"));

                        listOfCities.add(city);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                sharedPrefs.setListOfCitiesFromRequest(listOfCities);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);
    }
}
