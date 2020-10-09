package breakthecode.com.clickandgo.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import breakthecode.com.clickandgo.R;
import breakthecode.com.clickandgo.classes.AppSharedPreferencesHelper;
import breakthecode.com.clickandgo.classes.DatePickerFragment;
import breakthecode.com.clickandgo.classes.TimePickerFragment;
import breakthecode.com.clickandgo.entity.City;
import breakthecode.com.clickandgo.entity.UserTicket;
import breakthecode.com.clickandgo.resthelpers.RideRequestParameters;

public class MainPanelActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private static final String TAG = "myLogs MainPanel";


    private CardView mainPanelChooseFromPanel, mainPanelChooseToPanel, mainPanelChooseWhenPanel,
                        mainPanelChooseTimePanel, mainPanelActivityCardWithTicket, mainpanelActivityCardWithoutTicket;
    private TextView mainPanelChooseFromTxt, mainPanelChooseToTxt, mainPanelChooseWhenTxt, mainPanelChooseTimeTxt, mainPanelActivity_LastTicketRideCitiesNamesTxt,
            mainPanelActivity_LastTicketRideDateTxt, mainPanelActivity_LastTicketRideTimeTxt;

    private boolean isCityFromPicked, isCityToPicked;

    private AppSharedPreferencesHelper sharedPrefs;

    private Calendar date, time;
    private boolean dateIsPicked = false;

    private Button mainPanelSendRequest;

    private RideRequestParameters rideRequestParameters;

    private List<City> listOfCities;

    private ImageView mainPanelActivity_QRIcon;


    private ArrayList<UserTicket> listOfUsersTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);

        settings();
        loadData();
        initWidgets();
        loadWidgetsData();
        setOnClickListeners();
    }

    private void settings(){
        getSupportActionBar().hide();
    }

    private void initWidgets(){
        mainPanelChooseFromPanel = (CardView) findViewById(R.id.mainPanelChooseFromPanel);
        mainPanelChooseToPanel = (CardView) findViewById(R.id.mainPanelChooseToPanel);
        mainPanelChooseWhenPanel = (CardView) findViewById(R.id.mainPanelChooseWhenPanel);
        mainPanelChooseTimePanel = (CardView) findViewById(R.id.mainPanelChooseTimePanel);
        mainPanelActivityCardWithTicket = (CardView) findViewById(R.id.mainPanelActivityCardWithTicket);
        mainpanelActivityCardWithoutTicket = (CardView) findViewById(R.id.mainpanelActivityCardWithoutTicket);

        mainPanelChooseFromTxt = (TextView) findViewById(R.id.mainPanelChooseFromTxt);
        mainPanelChooseToTxt = (TextView) findViewById(R.id.mainPanelChooseToTxt);
        mainPanelChooseWhenTxt = (TextView) findViewById(R.id.mainPanelChooseWhenTxt);
        mainPanelChooseTimeTxt = (TextView) findViewById(R.id.mainPanelChooseTimeTxt);
        mainPanelActivity_LastTicketRideCitiesNamesTxt = (TextView) findViewById(R.id.mainPanelActivity_LastTicketRideCitiesNamesTxt);
        mainPanelActivity_LastTicketRideDateTxt = (TextView) findViewById(R.id.mainPanelActivity_LastTicketRideDateTxt);
        mainPanelActivity_LastTicketRideTimeTxt = (TextView) findViewById(R.id.mainPanelActivity_LastTicketRideTimeTxt);

        mainPanelSendRequest = (Button) findViewById(R.id.mainPanelSendRequest);

        mainPanelActivity_QRIcon = (ImageView) findViewById(R.id.mainPanelActivity_QRIcon);
    }

    private void setOnClickListeners(){
        mainPanelChooseFromPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPanelActivity.this, ChooseCityFromActivity.class);
                startActivity(intent);
            }
        });
        mainPanelChooseToPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPanelActivity.this, ChooseCityToActivity.class);
                startActivity(intent);
            }
        });
        mainPanelChooseWhenPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        mainPanelChooseTimePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        mainPanelSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPanelActivity.this, ChooseRideActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadData(){
        sharedPrefs = new AppSharedPreferencesHelper(this, "Shared_Preferences");
        rideRequestParameters = RideRequestParameters.getInstance();
        listOfUsersTickets = sharedPrefs.getListOfUsersTickets();
    }

    private void loadWidgetsData(){
        if(rideRequestParameters.isCityFromPicked()){
            mainPanelChooseFromTxt.setText(rideRequestParameters.getCityFrom().getCityName());
            mainPanelChooseFromTxt.setTextColor(Color.BLACK);
        }
        if(rideRequestParameters.isCityToPicked()){
            mainPanelChooseToTxt.setText(rideRequestParameters.getCityTo().getCityName());
            mainPanelChooseToTxt.setTextColor(Color.BLACK);
        }
        Intent intent = getIntent();
        int positionFrom = intent.getIntExtra("CITY_FROM_PICKED", -1);
        if(positionFrom >= 0){
            listOfCities = sharedPrefs.getListOfCitiesFromRequest();
            City city = listOfCities.get(positionFrom);
            rideRequestParameters.setCityFrom(city);
            rideRequestParameters.setCityFromPicked(true);
            mainPanelChooseFromTxt.setText(rideRequestParameters.getCityFrom().getCityName());
            mainPanelChooseFromTxt.setTextColor(Color.BLACK);
        }
        Intent intent2 = getIntent();
        int positionTo = intent2.getIntExtra("CITY_TO_PICKED", -1);
        if(positionTo >= 0){
            listOfCities = sharedPrefs.getListOfCitiesFromRequest();
            City city = listOfCities.get(positionTo);
            rideRequestParameters.setCityTo(city);
            rideRequestParameters.setCityToPicked(true);
            mainPanelChooseToTxt.setText(rideRequestParameters.getCityTo().getCityName());
            mainPanelChooseToTxt.setTextColor(Color.BLACK);
        }
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String minuteString;
        if(minute < 10){
            minuteString = "0" + minute;
        } else {
            minuteString = String.valueOf(minute);
        }
        mainPanelChooseTimeTxt.setText(hour+":"+minuteString);

        if(listOfUsersTickets.size() == 0){
            mainPanelActivityCardWithTicket.setVisibility(View.INVISIBLE);
            mainpanelActivityCardWithoutTicket.setVisibility(View.VISIBLE);
        } else {
            mainPanelActivityCardWithTicket.setVisibility(View.VISIBLE);
            mainpanelActivityCardWithoutTicket.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "loadData: isCityFromPicked = " + isCityFromPicked);
        Log.d(TAG, "loadData: isCityToPicked = " + isCityToPicked);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(date.getTime());
        mainPanelChooseWhenTxt.setText(currentDateString);
        Date dateOfRide = new java.sql.Date(date.getTimeInMillis());
        rideRequestParameters.setDateOfRide(dateOfRide);
        dateIsPicked = true;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, hour);
        time.set(Calendar.MINUTE, minute);
        String minuteString;
        if(minute < 10){
            minuteString = "0"+minute;
        } else {
            minuteString = String.valueOf(minute);
        }
        mainPanelChooseTimeTxt.setText(hour + ":" + minuteString);
        Time timeOfRide = Time.valueOf(hour+":"+minuteString+":"+"00");
        rideRequestParameters.setTimeOfRide(timeOfRide);
    }

}