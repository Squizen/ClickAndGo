package breakthecode.com.clickandgo.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

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
            mainPanelActivity_LastTicketRideDateTxt, mainPanelActivity_LastTicketRideTimeTxt, mainPanelActivity_clickToIncreaseSizeTxt;
    private boolean isCityFromPicked, isCityToPicked;
    private AppSharedPreferencesHelper sharedPrefs;
    private Calendar date, time;
    private boolean dateIsPicked = false;
    private Button mainPanelSendRequest;
    private RideRequestParameters rideRequestParameters;
    private List<City> listOfCities;
    private ArrayList<UserTicket> listOfUsersTickets;
    private ImageView mainPanelActivity_QRIcon, mainPanelLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);

        settings();
        loadData();
        initWidgets();
        loadWidgetsData();
        setOnClickListeners();
        loadLastTicket();
    }

    private void settings(){
        getSupportActionBar().hide();
    }

    private void initWidgets(){
        mainPanelChooseFromPanel = findViewById(R.id.mainPanelChooseFromPanel);
        mainPanelChooseToPanel = findViewById(R.id.mainPanelChooseToPanel);
        mainPanelChooseWhenPanel = findViewById(R.id.mainPanelChooseWhenPanel);
        mainPanelChooseTimePanel = findViewById(R.id.mainPanelChooseTimePanel);
        mainPanelActivityCardWithTicket = findViewById(R.id.mainPanelActivityCardWithTicket);
        mainpanelActivityCardWithoutTicket = findViewById(R.id.mainpanelActivityCardWithoutTicket);

        mainPanelChooseFromTxt = findViewById(R.id.mainPanelChooseFromTxt);
        mainPanelChooseToTxt = findViewById(R.id.mainPanelChooseToTxt);
        mainPanelChooseWhenTxt = findViewById(R.id.mainPanelChooseWhenTxt);
        mainPanelChooseTimeTxt = findViewById(R.id.mainPanelChooseTimeTxt);
        mainPanelActivity_clickToIncreaseSizeTxt = (TextView) findViewById(R.id.mainPanelActivity_clickToIncreaseSizeTxt);

        mainPanelActivity_LastTicketRideCitiesNamesTxt = findViewById(R.id.mainPanelActivity_LastTicketRideCitiesNamesTxt);
        mainPanelActivity_LastTicketRideDateTxt = findViewById(R.id.mainPanelActivity_LastTicketRideDateTxt);
        mainPanelActivity_LastTicketRideTimeTxt = findViewById(R.id.mainPanelActivity_LastTicketRideTimeTxt);

        mainPanelSendRequest = findViewById(R.id.mainPanelSendRequest);

        mainPanelActivity_QRIcon = findViewById(R.id.mainPanelActivity_QRIcon);
        mainPanelLogo = (ImageView) findViewById(R.id.mainPanelLogo);
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
        mainPanelActivityCardWithTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTicketDialog();
            }
        });
        mainPanelActivity_clickToIncreaseSizeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTicketDialog();
            }
        });
        mainPanelLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPanelActivity.this, MyTicketsActivity.class);
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

    private void loadLastTicket(){
        if(listOfUsersTickets.size() == 0){
            mainPanelActivityCardWithTicket.setVisibility(View.GONE);
            mainpanelActivityCardWithoutTicket.setVisibility(View.VISIBLE);
            mainPanelActivity_clickToIncreaseSizeTxt.setVisibility(View.GONE);
        } else {
            mainPanelActivityCardWithTicket.setVisibility(View.VISIBLE);
            mainpanelActivityCardWithoutTicket.setVisibility(View.GONE);
            mainPanelActivity_clickToIncreaseSizeTxt.setVisibility(View.VISIBLE);

            generateQRcode(listOfUsersTickets.get(listOfUsersTickets.size()-1).getTicket().getTicketNumber());
            String rideRelation = listOfUsersTickets.get(listOfUsersTickets.size()-1).getCityFrom().getCityName() + " - " + listOfUsersTickets.get(listOfUsersTickets.size()-1).getCityTo().getCityName();
            mainPanelActivity_LastTicketRideCitiesNamesTxt.setText(rideRelation);
            mainPanelActivity_LastTicketRideDateTxt.setText(listOfUsersTickets.get(listOfUsersTickets.size()-1).getTicket().getRideDate().toString());
            mainPanelActivity_LastTicketRideTimeTxt.setText(listOfUsersTickets.get(listOfUsersTickets.size()-1).getTicket().getExpiringTime().toString());
        }
    }

    private void generateQRcode(String ticketNumber){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(ticketNumber, BarcodeFormat.QR_CODE, 100, 100);
            Bitmap bitmap = Bitmap.createBitmap(100,100, Bitmap.Config.RGB_565);
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j) ? Color.BLACK : Color.WHITE);
                }
            }
            mainPanelActivity_QRIcon.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void showTicketDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainPanelActivity.this);
        View view = getLayoutInflater().inflate(R.layout.ticket_dialog, null);
        ImageView qrCodeInTicketDialog = view.findViewById(R.id.ticketDialog_qrCode);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(listOfUsersTickets.get(listOfUsersTickets.size()-1).getTicket().getTicketNumber(), BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200,200, Bitmap.Config.RGB_565);
            for (int i = 0; i < 200; i++) {
                for (int j = 0; j < 200; j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCodeInTicketDialog.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        TextView ticketDialog_cityNames = view.findViewById(R.id.ticketDialog_cityNames);
        String rideRelation = listOfUsersTickets.get(listOfUsersTickets.size()-1).getCityFrom().getCityName() + " - " + listOfUsersTickets.get(listOfUsersTickets.size()-1).getCityTo().getCityName();
        ticketDialog_cityNames.setText(rideRelation);
        TextView ticketDialog_rideDate = view.findViewById(R.id.ticketDialog_rideDate);
        ticketDialog_rideDate.setText(listOfUsersTickets.get(listOfUsersTickets.size()-1).getTicket().getRideDate().toString());
        TextView ticketDialog_rideTime = view.findViewById(R.id.ticketDialog_rideTime);
        ticketDialog_rideTime.setText(listOfUsersTickets.get(listOfUsersTickets.size()-1).getTicket().getExpiringTime().toString());
        TextView ticketDialog_ownerName = view.findViewById(R.id.ticketDialog_ownerName);
        ticketDialog_ownerName.setText(listOfUsersTickets.get(listOfUsersTickets.size()-1).getTicket().getOwnerName() + " " + listOfUsersTickets.get(listOfUsersTickets.size()-1).getTicket().getOwnerSurname());
        TextView ticketDialog_ownerEmail = view.findViewById(R.id.ticketDialog_ownerEmail);
        ticketDialog_ownerEmail.setText(listOfUsersTickets.get(listOfUsersTickets.size()-1).getTicket().getOwnerEmail());
        alertDialog.setView(view);
        AlertDialog alert = alertDialog.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.show();

    }
}