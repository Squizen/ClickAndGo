package breakthecode.com.clickandgo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
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

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import breakthecode.com.clickandgo.R;
import breakthecode.com.clickandgo.classes.AppSharedPreferencesHelper;
import breakthecode.com.clickandgo.classes.MailSupport;
import breakthecode.com.clickandgo.classes.StringValidation;
import breakthecode.com.clickandgo.classes.TicketNumberGenerator;
import breakthecode.com.clickandgo.classes.TransactionDialog;
import breakthecode.com.clickandgo.entity.Ride;
import breakthecode.com.clickandgo.entity.Ticket;
import breakthecode.com.clickandgo.entity.UserTicket;
import breakthecode.com.clickandgo.resthelpers.RideRequestParameters;

public class TicketBuyingActivity extends AppCompatActivity {
    private static final String TAG = "TicketBuying myLogs";
    private static final String SERVER_URL = "http://ssh-vps.nazwa.pl:8084";

    private TextView ticketBuingRideCityFromName, ticketBuingRideCityToName, ticketBuingRideCityRideDate,
            ticketBuingRideCityRideTime, ticketBuingRideCityRidePrice;

    private Ride ride;
    private RideRequestParameters rideRequestParameters;

    private Button ticketBuyingActivity_buyTicketButton;

    private TransactionDialog transactionDialog;

    private RequestQueue requestQueue;

    private ArrayList<Boolean> isDataCorrect;
    private ArrayList<String> userData;
    private ArrayList<UserTicket> listOfUserTickets;

    private EditText ticketBuyingNameEditText, ticketBuyingSurnameEditText, ticketBuyingFirstEmailEditText, ticketBuyingSecondEmailEditText;

    private AppSharedPreferencesHelper sharedPrefs;

    private OnEmailSendListener onEmailSendListener;

    public interface OnEmailSendListener{
        void onEmailSend();
    }

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
        sharedPrefs = new AppSharedPreferencesHelper(this, "Shared_Preferences");
        userData = new ArrayList<>();
        listOfUserTickets = sharedPrefs.getListOfUsersTickets();
        transactionDialog = new TransactionDialog(this);
    }

    private void initWidgets(){
        ticketBuingRideCityFromName = findViewById(R.id.ticketBuingRideCityFromName);
        ticketBuingRideCityToName = findViewById(R.id.ticketBuingRideCityToName);
        ticketBuingRideCityRideDate = findViewById(R.id.ticketBuingRideCityRideDate);
        ticketBuingRideCityRideTime = findViewById(R.id.ticketBuingRideCityRideTime);
        ticketBuingRideCityRidePrice = findViewById(R.id.ticketBuingRideCityRidePrice);

        ticketBuyingActivity_buyTicketButton = findViewById(R.id.ticketBuyingActivity_buyTicketButton);

        ticketBuyingNameEditText = findViewById(R.id.ticketBuyingNameEditText);
        ticketBuyingSurnameEditText = findViewById(R.id.ticketBuyingSurnameEditText);
        ticketBuyingFirstEmailEditText = findViewById(R.id.ticketBuyingFirstEmailEditText);
        ticketBuyingSecondEmailEditText = findViewById(R.id.ticketBuyingSecondEmailEditText);
    }
    private void initWidgetsData(){
        loadRideResponseObject();
        if(ride != null){
            ticketBuingRideCityFromName.setText(ride.getCityFrom().getCityName());
            ticketBuingRideCityToName.setText(ride.getCityTo().getCityName());

            int length = ride.getRideTime().toString().length();
            String time = ride.getRideTime().toString().substring(0, length-3);
            ticketBuingRideCityRideTime.setText(time);

            ticketBuingRideCityRideDate.setText(DateFormat.getDateInstance().format(rideRequestParameters.getDateOfRide().getTime()));
            String price = String.format("%.2f", ride.getPrice());
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
                // TODO incorrect second email
                isDataCorrect.add(StringValidation.isFirstEmailIdenticalAsSecondOne(ticketBuyingFirstEmailEditText.getText().toString(), ticketBuyingSecondEmailEditText.getText().toString()));
                if(isDataCorrect.contains(false)){
                    // TODO dont do anything
                } else {
                    buyTicket(ride, userData);
                }
            }
        });
    }
    private void loadRideResponseObject(){
        ride = (Ride) getIntent().getExtras().getSerializable("rideObject");
    }

    private void buyTicket(Ride ride, ArrayList<String> userData){
        Ticket ticket = new Ticket();

        ticket.setIdRide(ride.getId());
        ticket.setTicketNumber(TicketNumberGenerator.generateTicket());

        Calendar calendar = Calendar.getInstance();
        ticket.setPurchaseDate(Date.valueOf(new java.sql.Date(calendar.getTimeInMillis()).toString()));
        ticket.setPurchaseTime(Time.valueOf(ride.getRideTime().toString()));

        ticket.setRideDate(rideRequestParameters.getDateOfRide());
        ticket.setOwnerName(userData.get(0));
        ticket.setOwnerSurname(userData.get(1));
        ticket.setOwnerEmail(userData.get(2));

        long dateInMilis = calendar.getTimeInMillis() + 86400000L;
        Date newDate = new java.sql.Date(dateInMilis);

        ticket.setExpiringDate(newDate);

        ticket.setExpiringTime(ride.getRideTime());

        String jsonWithData = "{"+
                "\"idRide\""+" : "+ticket.getIdRide()+","+
                "\"ticketNumber\""+" : "+"\""+ticket.getTicketNumber()+"\", "+
                "\"purchaseDate\""+" : "+"\""+ticket.getPurchaseDate()+"\", "+
                "\"purchaseTime\""+" : "+"\""+ticket.getPurchaseTime()+"\", "+
                "\"rideDate\""+" : "+"\""+ticket.getRideDate()+"\", "+
                "\"expiringDate\""+" : "+"\""+ticket.getExpiringDate()+"\", "+
                "\"expiringTime\""+" : "+"\""+ticket.getExpiringTime()+"\", "+
                "\"passenger_name\""+" : "+"\""+ticket.getOwnerName()+"\", "+
                "\"passenger_surname\""+" : "+"\""+ticket.getOwnerSurname()+"\", "+
                "\"passenger_email\""+" : "+"\""+ticket.getOwnerEmail()+"\"}";

        ticket.getIdRide();
        Log.d(TAG, "buyTicket: " + jsonWithData);
        addTicketToDatabase(jsonWithData, ticket);

        //TODO if response TRUE - dodany do bazy poprawnie

    }
    private void addTicketToDatabase(String jsonWithData, final Ticket ticket){
        final String saveData = jsonWithData;
        String serverURL = SERVER_URL + "/api/tickets";
        transactionDialog.startTransactionDialog();
        onEmailSendListener = new OnEmailSendListener() {
            @Override
            public void onEmailSend() {
                transactionDialog.dismissDialog();
            }
        };
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                UserTicket userTicket = new UserTicket();
                userTicket.setCityFrom(rideRequestParameters.getCityFrom());
                userTicket.setCityTo(rideRequestParameters.getCityTo());
                userTicket.setTicket(ticket);
                listOfUserTickets.add(userTicket);
                sharedPrefs.setListOfUsersTickets(listOfUserTickets);

                MailSupport mailSupport = new MailSupport();
                mailSupport.setCityFrom(rideRequestParameters.getCityFrom().getCityName());
                mailSupport.setCityTo(rideRequestParameters.getCityTo().getCityName());
                mailSupport.setOwnerName(ticket.getOwnerName());
                mailSupport.setOwnerSurname(ticket.getOwnerSurname());
                mailSupport.setEmailAddress(ticket.getOwnerEmail());
                mailSupport.setDateOfRide(ticket.getRideDate().toString());
                mailSupport.setTimeOfRide(ticket.getExpiringTime().toString());
                mailSupport.setTicketNumber(ticket.getTicketNumber());
                sendMail(mailSupport);
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
                return saveData == null ? null : saveData.getBytes(StandardCharsets.UTF_8);
            }
        };
        requestQueue.add(stringRequest);
    }

    private void sendMail(MailSupport mailSupport){
        String serverURL = SERVER_URL + "/api/sendMail";

        String jsonWithData = "{"+
                "\"cityFrom\""+" : "+"\""+mailSupport.getCityFrom()+"\","+
                "\"cityTo\""+" : "+"\""+mailSupport.getCityTo()+"\", "+
                "\"emailAddress\""+" : "+"\""+mailSupport.getEmailAddress()+"\", "+
                "\"ownerName\""+" : "+"\""+mailSupport.getOwnerName()+"\", "+
                "\"ownerSurname\""+" : "+"\""+mailSupport.getOwnerSurname()+"\", "+
                "\"dateOfRide\""+" : "+"\""+mailSupport.getDateOfRide()+"\", "+
                "\"timeOfRide\""+" : "+"\""+mailSupport.getTimeOfRide()+"\", "+
                "\"ticketNumber\""+" : "+"\""+mailSupport.getTicketNumber()+"\"}";

        final String saveData = jsonWithData;
        Log.d(TAG, "sendMail: " + jsonWithData);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onEmailSendListener.onEmailSend();
                Dialog dialog = new Dialog(TicketBuyingActivity.this);
                dialog.setContentView(R.layout.buying_successful);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(TicketBuyingActivity.this, MainPanelActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
                Intent intent = new Intent(TicketBuyingActivity.this, MainPanelActivity.class);
                startActivity(intent);
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return saveData == null ? null : saveData.getBytes(StandardCharsets.UTF_8);
            }
        };
        requestQueue.add(stringRequest);
    }
}