package breakthecode.com.clickandgo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import breakthecode.com.clickandgo.R;
import breakthecode.com.clickandgo.classes.AppSharedPreferencesHelper;
import breakthecode.com.clickandgo.classes.LoadingDialog;
import breakthecode.com.clickandgo.entity.City;
import breakthecode.com.clickandgo.recyclerviews.CitiesFromRecViewAdapter;
import breakthecode.com.clickandgo.resthelpers.RideRequestParameters;


public class ChooseCityFromActivity extends AppCompatActivity implements CitiesFromRecViewAdapter.OnItemClickListener {
    private static final String TAG = "myLogs ChooseCity";

    private static final String SERVER_URL = "http://ssh-vps.nazwa.pl:8084";
    public static final String CITY_PICKED = "CITY_FROM_PICKED";

    private AppSharedPreferencesHelper sharedPrefs;

    private RecyclerView cityRecView;
    private EditText chooseCityFromSearchEditText;

    private List<City> listOfCities;
    private LoadingDialog loadingDialog;


    private RideRequestParameters rideRequestParameters;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city_from);

        settings();
        loadData();
        initWidgets();
        loadWidgetsData();
        setOnClickListeners();
        setUpRecyclerView();

    }
    private void settings(){
        Log.d(TAG, "settings: ");
        getSupportActionBar().hide();
    }

    private void loadData(){
        Log.d(TAG, "loadData: ");
        sharedPrefs = new AppSharedPreferencesHelper(this, "Shared_Preferences");
        rideRequestParameters = RideRequestParameters.getInstance();
    }

    private void initWidgets(){
        Log.d(TAG, "initWidgets: ");
        cityRecView = findViewById(R.id.choose_cityFrom_recView);
        chooseCityFromSearchEditText = findViewById(R.id.chooseCityFromSearchEditText);

        loadingDialog = new LoadingDialog(this);
    }
    private void loadWidgetsData(){
        Log.d(TAG, "loadWidgetsData: ");
    }

    private void setOnClickListeners(){
        Log.d(TAG, "setOnClickListeners: ");

    }

    private void setUpRecyclerView(){
        Log.d(TAG, "setUpRecyclerView: ");
        listOfCities = new ArrayList<>();
        if(isNetworkAvailable()){
            requestListOfAllCities();
        } else {
            Dialog dialog = new Dialog(this);
            Window window = dialog.getWindow();
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.no_internet_dialog);
            dialog.setCancelable(true);
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                        Intent intent = new Intent(ChooseCityFromActivity.this, MainPanelActivity.class);
                        startActivity(intent);
                }
            });
        }
    }

    public void requestListOfAllCities(){
        Log.d(TAG, "requestListOfAllCities: ");
        String connection_address;
        if(!rideRequestParameters.isCityToPicked()){
            connection_address = SERVER_URL + "/api/cities/from";
        } else {
            connection_address = SERVER_URL + "/api/cities/from/to/" + rideRequestParameters.getCityTo().getId()+"";
        }
        listOfCities = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d(TAG, "requestListOfAllCities: SERVER URL - " + SERVER_URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, connection_address, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: ");
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
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                        sharedPrefs.setListOfCitiesFromRequest(listOfCities);
                        CitiesFromRecViewAdapter adapter = new CitiesFromRecViewAdapter(getApplicationContext());
                        cityRecView.setAdapter(adapter);
                        cityRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        cityRecView.setItemAnimator(new DefaultItemAnimator());
                        layoutAnimation(cityRecView);
                        adapter.setListOfCities(listOfCities);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemClickListener(ChooseCityFromActivity.this);
                    }
                }, 1500);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.networkResponse.statusCode);
                error.printStackTrace();


            }
        });
        queue.add(jsonArrayRequest);
    }
    private void layoutAnimation(RecyclerView recView){
        Log.d(TAG, "layoutAnimation: ");
        Context context = recView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        recView.setLayoutAnimation(layoutAnimationController);
        recView.getAdapter().notifyDataSetChanged();
        recView.scheduleLayoutAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        chooseCityFromSearchEditText.clearFocus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
        Intent intent = new Intent(ChooseCityFromActivity.this, MainPanelActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, MainPanelActivity.class);
        detailIntent.putExtra(CITY_PICKED, position);
        startActivity(detailIntent);
    }
    private boolean isNetworkAvailable(){
        try{
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if(manager !=null){
                networkInfo = manager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.isConnected();
        }catch(NullPointerException e){
            return false;
        }
    }
}