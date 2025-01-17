package breakthecode.com.clickandgo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import breakthecode.com.clickandgo.R;
import breakthecode.com.clickandgo.classes.AppSharedPreferencesHelper;
import breakthecode.com.clickandgo.entity.City;
import breakthecode.com.clickandgo.recyclerviews.CitiesToRecViewAdapter;
import breakthecode.com.clickandgo.resthelpers.RideRequestParameters;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myLogs MainAct";
    private RideRequestParameters rideRequestParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings();
        setUpRideRequestParameters();
        executeAfterSomeTime();
    }

    private void settings(){
        getSupportActionBar().hide();
    }

    private void executeAfterSomeTime(){
        int delay = 4000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            public void run(){
                Intent intent = new Intent(MainActivity.this, MainPanelActivity.class);
                startActivity(intent);
                Animatoo.animateCard(MainActivity.this);
            }
        }, delay);
    }

    private void setUpRideRequestParameters(){
        rideRequestParameters = RideRequestParameters.getInstance();
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String minuteString;
        if(minute < 10){
            minuteString = "0" + minute;
        } else {
            minuteString = String.valueOf(minute);
        }
        Time timeOfRide = Time.valueOf(hour+":"+minuteString+":"+"00");
        rideRequestParameters.setTimeOfRide(timeOfRide);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Date dateOfRide = Date.valueOf(""+year+"-"+(month+1)+"-"+day);
        rideRequestParameters.setDateOfRide(dateOfRide);
    }
}