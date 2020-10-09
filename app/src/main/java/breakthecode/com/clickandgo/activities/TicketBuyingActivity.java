package breakthecode.com.clickandgo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import breakthecode.com.clickandgo.R;
import breakthecode.com.clickandgo.classes.StringValidation;
import breakthecode.com.clickandgo.classes.TicketNumberGenerator;
import breakthecode.com.clickandgo.entity.Ride;
import breakthecode.com.clickandgo.entity.RideResponse;
import breakthecode.com.clickandgo.entity.Ticket;
import breakthecode.com.clickandgo.resthelpers.RideRequestParameters;

public class TicketBuyingActivity extends AppCompatActivity {
    private static final String TAG = "TicketBuying myLogs";
    private static String SERVER_URL = "http://ssh-vps.nazwa.pl:8080";

    private TextView ticketBuingRideCityFromName, ticketBuingRideCityToName, ticketBuingRideCityRideDate,
            ticketBuingRideCityRideTime, ticketBuingRideCityRidePrice;

    private RideResponse rideResponse;
    private RideRequestParameters rideRequestParameters;

    private Button ticketBuyingActivity_buyTicketButton;

    private RequestQueue requestQueue;

    private ArrayList<Boolean> isDataCorrect;
    private ArrayList<String> userData;

    private EditText ticketBuyingNameEditText, ticketBuyingSurnameEditText, ticketBuyingFirstEmailEditText, ticketBuyingSecondEmailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_buying);

        settings();
        loadData();
        initWidgets();
        initWidgetsData();
        setOnClickListeners();

    }
    private void settings(){
        getSupportActionBar().hide();
    }
    private void loadData(){
        rideRequestParameters = RideRequestParameters.getInstance();
        userData = new ArrayList<>();
    }
    private void initWidgets(){
        ticketBuingRideCityFromName = (TextView) findViewById(R.id.ticketBuingRideCityFromName);
        ticketBuingRideCityToName = (TextView) findViewById(R.id.ticketBuingRideCityToName);
        ticketBuingRideCityRideDate = (TextView) findViewById(R.id.ticketBuingRideCityRideDate);
        ticketBuingRideCityRideTime = (TextView) findViewById(R.id.ticketBuingRideCityRideTime);
        ticketBuingRideCityRidePrice = (TextView) findViewById(R.id.ticketBuingRideCityRidePrice);

        ticketBuyingActivity_buyTicketButton = (Button) findViewById(R.id.ticketBuyingActivity_buyTicketButton);

        ticketBuyingNameEditText = (EditText) findViewById(R.id.ticketBuyingNameEditText);
        ticketBuyingSurnameEditText = (EditText) findViewById(R.id.ticketBuyingSurnameEditText);
        ticketBuyingFirstEmailEditText = (EditText) findViewById(R.id.ticketBuyingSurnameEditText);
        ticketBuyingSecondEmailEditText = (EditText) findViewById(R.id.ticketBuyingSecondEmailEditText);
    }
    private void initWidgetsData(){
        loadRideResponseObject();
        if(rideResponse != null){
            ticketBuingRideCityFromName.setText(rideResponse.getCityFrom().getCityName());
            ticketBuingRideCityToName.setText(rideResponse.getCityTo().getCityName());

            int length = rideResponse.getRide().getRideTime().toString().length();
            String time = rideResponse.getRide().getRideTime().toString().substring(0, length-3);
            ticketBuingRideCityRideTime.setText(time);

            ticketBuingRideCityRideDate.setText(DateFormat.getDateInstance().format(rideRequestParameters.getDateOfRide().getTime()));
            String price = String.format("%.2f", rideResponse.getRide().getPrice());
            ticketBuingRideCityRidePrice.setText(price + " zł");
        }
    }
    private void setOnClickListeners(){
        ticketBuyingActivity_buyTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDataCorrect = new ArrayList<>();
                userData = new ArrayList<>();
                // TODO wyciagnij potencjalne dane z sharedPreferences, jesli ich tam nie ma, to new Arraylist
                if(StringValidation.isCorrectNameOrSurname(ticketBuyingNameEditText.getText().toString())){
                    isDataCorrect.add(true); // number 1
                    userData.add(ticketBuyingNameEditText.getText().toString());
                } else {
                    isDataCorrect.add(false);
                    // TODO alertDialog name incorrect
                }
                if(StringValidation.isCorrectNameOrSurname(ticketBuyingSurnameEditText.getText().toString())){
                    isDataCorrect.add(true);
                    userData.add(ticketBuyingSurnameEditText.getText().toString());
                } else {
                    isDataCorrect.add(false);
                    // TODO alertDialog surname incorrect
                }
                if(StringValidation.isCorrectEmail(ticketBuyingFirstEmailEditText.getText().toString())){
                    isDataCorrect.add(true);
                    userData.add(ticketBuyingFirstEmailEditText.getText().toString());
                } else {
                    isDataCorrect.add(false);
                    // TODO incorrect email
                }
                if(StringValidation.isFirstEmailIdenticalAsSecondOne(ticketBuyingFirstEmailEditText.getText().toString(), ticketBuyingSecondEmailEditText.getText().toString())){
                    isDataCorrect.add(true);
                } else {
                    isDataCorrect.add(false);
                    // TODO incorrect second email
                }
                if(isDataCorrect.contains(false)){
                    // TODO dont do anything
                } else {
                    buyTicket(rideResponse, userData);
                }
            }
        });
    }
    private void loadRideResponseObject(){
        rideResponse = (RideResponse) getIntent().getExtras().getSerializable("rideObject");
    }

    private void buyTicket(RideResponse rideResponse, ArrayList<String> userData){
        Ticket ticket = new Ticket();

        ticket.setIdRide(rideResponse.getRide().getId());
        ticket.setTicketNumber(TicketNumberGenerator.generateTicket());

        Calendar calendar = Calendar.getInstance();
        ticket.setPurchaseDate(Date.valueOf(new java.sql.Date(calendar.getTimeInMillis()).toString()));
        ticket.setPurchaseTime(Time.valueOf(rideResponse.getRide().getRideTime().toString()));

        ticket.setRideDate(rideRequestParameters.getDateOfRide());
        ticket.setOwnerName(userData.get(0));
        ticket.setOwnerSurname(userData.get(1));
        ticket.setOwnerEmail(userData.get(2));

        long dateInMilis = calendar.getTimeInMillis() + 86400000L;
        Date newDate = new java.sql.Date(dateInMilis);

        ticket.setExpiringDate(newDate);

        ticket.setExpiringTime(rideResponse.getRide().getRideTime());

        String jsonWithData = "{"+
                "\"rideId\""+" : "+ticket.getIdRide()+","+
                "\"ticketNumber\""+" : "+"\""+ticket.getTicketNumber()+"\", "+
                "\"purchaseDate\""+" : "+"\""+ticket.getPurchaseDate()+"\", "+
                "\"purchaseTime\""+" : "+"\""+ticket.getPurchaseTime()+"\", "+
                "\"rideDate\""+" : "+"\""+ticket.getRideDate()+"\", "+
                "\"ownerName\""+" : "+"\""+ticket.getOwnerName()+"\", "+
                "\"owmerSurname\""+" : "+"\""+ticket.getOwnerSurname()+"\", "+
                "\"ownerEmail\""+ " : "+"\""+ticket.getOwnerEmail()+"\", "+
                "\"expiringDate\""+" : "+"\""+ticket.getExpiringDate()+"\", "+
                "\"expiringTime\""+" : "+"\""+ticket.getExpiringTime()+"\"}";

        ticket.getIdRide();
        Log.d(TAG, "buyTicket: " + jsonWithData);
        addTicketToDatabase(jsonWithData);

        //TODO if response TRUE - dodany do bazy poprawnie

    }
    private void addTicketToDatabase(String jsonWithData){
        final String saveData = jsonWithData;
        String serverURL = SERVER_URL + "/api/tickets";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try{
                    return saveData == null ? null : saveData.getBytes("utf-8");
                }catch(UnsupportedEncodingException uee){
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }
}