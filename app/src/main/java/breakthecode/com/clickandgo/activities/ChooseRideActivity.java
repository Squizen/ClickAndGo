package breakthecode.com.clickandgo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import breakthecode.com.clickandgo.R;
import breakthecode.com.clickandgo.classes.LoadingDialog;
import breakthecode.com.clickandgo.entity.City;
import breakthecode.com.clickandgo.entity.Ride;
import breakthecode.com.clickandgo.entity.RideResponse;
import breakthecode.com.clickandgo.recyclerviews.CitiesToRecViewAdapter;
import breakthecode.com.clickandgo.recyclerviews.RideCallback;
import breakthecode.com.clickandgo.recyclerviews.RidesRecyclerViewAdapter;
import breakthecode.com.clickandgo.resthelpers.RideRequestParameters;

public class ChooseRideActivity extends AppCompatActivity implements RideCallback {
    private static final String TAG = "myLogs";
    private static String SERVER_URL = "http://ssh-vps.nazwa.pl:8080";

    private RideRequestParameters rideRequestParameters;
    private List<RideResponse> listOfRides;
    private RecyclerView rideRecView;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_ride);

        settings();
        loadData();
        initWidgets();
        loadWidgetsData();
        setUpRecyclerView();

    }

    private void settings() {
        getSupportActionBar().hide();
    }

    private void loadData() {
        rideRequestParameters = RideRequestParameters.getInstance();

    }

    private void initWidgets() {
        rideRecView = findViewById(R.id.choose_ride_rec_view);
    }

    private void loadWidgetsData() {

    }

    private String constructRequestURL(RideRequestParameters rideRequestParamteres) {
        StringBuilder sb = new StringBuilder();
        sb = sb.append(SERVER_URL + "/api/rides");
        if (rideRequestParameters.isCityFromPicked() && rideRequestParamteres.isCityToPicked()) {
            sb.append("/bothParams?cityFrom=" + rideRequestParamteres.getCityFrom().getId());
            sb.append("&cityTo=" + rideRequestParamteres.getCityTo().getId());
            sb.append("&timeStr=" + rideRequestParamteres.getTimeOfRide().toString());
            return sb.toString();
        } else if (rideRequestParameters.isCityFromPicked() && !rideRequestParamteres.isCityToPicked()) {
            sb.append("/fromParam?timeStr=" + rideRequestParamteres.getTimeOfRide().toString());
            sb.append("&cityFrom=" + rideRequestParamteres.getCityFrom().getId());
            return sb.toString();
        } else if (!rideRequestParameters.isCityFromPicked() && rideRequestParamteres.isCityToPicked()) {
            sb.append("/toParam?cityTo=" + rideRequestParamteres.getCityTo().getId());
            sb.append("&timeStr=" + rideRequestParamteres.getTimeOfRide().toString());
            return sb.toString();
        } else if (!rideRequestParameters.isCityFromPicked() && !rideRequestParamteres.isCityToPicked()) {
            sb.append("/timeParam?timeStr=" + rideRequestParamteres.getTimeOfRide().toString());
            return sb.toString();
        } else {
            return sb.toString();
        }
    }

    private void setUpRecyclerView(){
        if(isNetworkAvailable()){
            requestRides();
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
                    Intent intent = new Intent(ChooseRideActivity.this, MainPanelActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void requestRides() {
        Log.d(TAG, "requestRides: ");
        String connection_address = constructRequestURL(rideRequestParameters);

        listOfRides = new ArrayList<>();
        loadingDialog = new LoadingDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d(TAG, "requestRides: SERVER URL - " + SERVER_URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, connection_address, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: ");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObject = response.getJSONObject(i);
                        JSONObject singleRide = responseObject.getJSONObject("ride");
                        Ride ride = new Ride();
                        ride.setId(singleRide.getInt("id"));
                        ride.setIdCityFrom(singleRide.getInt("idCityFrom"));
                        ride.setIdCityTo(singleRide.getInt("idCityTo"));
                        ride.setPrice(singleRide.getDouble("price"));
                        Time time = Time.valueOf(singleRide.getString("rideTime"));
                        ride.setRideTime(time);

                        RideResponse rideResponse = new RideResponse();
                        rideResponse.setRide(ride);

                        JSONObject responseObjectCityFrom = responseObject.getJSONObject("cityFrom");
                        City city = new City();
                        city.setId(responseObjectCityFrom.getInt("id"));
                        city.setCityName(responseObjectCityFrom.getString("cityName"));
                        city.setBusStopName(responseObjectCityFrom.getString("busStopName"));
                        rideResponse.setCityFrom(city);

                        JSONObject responseObjectCityTo = responseObject.getJSONObject("cityTo");
                        City cityTo = new City();
                        cityTo.setId(responseObjectCityTo.getInt("id"));
                        cityTo.setCityName(responseObjectCityTo.getString("cityName"));
                        cityTo.setBusStopName(responseObjectCityTo.getString("busStopName"));
                        rideResponse.setCityTo(cityTo);

                        listOfRides.add(rideResponse);

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
                        RidesRecyclerViewAdapter adapter = new RidesRecyclerViewAdapter(getApplicationContext(), ChooseRideActivity.this);
                        rideRecView.setAdapter(adapter);
                        rideRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rideRecView.setItemAnimator(new DefaultItemAnimator());
                        layoutAnimation(rideRecView);
                        adapter.setListOfRides(listOfRides);
                        adapter.notifyDataSetChanged();
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

    private void layoutAnimation(RecyclerView recView) {
        Log.d(TAG, "layoutAnimation: ");
        Context context = recView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        recView.setLayoutAnimation(layoutAnimationController);
        recView.getAdapter().notifyDataSetChanged();
        recView.scheduleLayoutAnimation();
    }

//    @Override
//    public void onItemClick(int position) {
//
//    }

    @Override
    public void onRideItemClick(int position, TextView single_ride_item_for_rec_view_rideCityFrom,
                                TextView single_ride_item_for_rec_view_rideCityTo,
                                TextView single_ride_item_for_rec_view_rideDate,
                                TextView single_ride_item_for_rec_view_rideTime,
                                TextView single_ride_item_for_rec_view_ridePrice) {
        Intent intent = new Intent(this, RideCardActivity.class);
        intent.putExtra("rideObject", listOfRides.get(position));

        Pair<View,String> p1 = Pair.create((View)single_ride_item_for_rec_view_rideCityFrom, "rideCardRideCityFromNameTN");
        Pair<View,String> p2 = Pair.create((View)single_ride_item_for_rec_view_rideCityTo, "rideCardRideCityToNameTN");
        Pair<View,String> p3 = Pair.create((View)single_ride_item_for_rec_view_rideDate, "rideCardRideDateTN");
        Pair<View,String> p4 = Pair.create((View)single_ride_item_for_rec_view_rideTime, "rideCardRideTimeTN");
        Pair<View,String> p5 = Pair.create((View)single_ride_item_for_rec_view_ridePrice, "rideCardRidePriceTN");


        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1,p2,p3,p4,p5);
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
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