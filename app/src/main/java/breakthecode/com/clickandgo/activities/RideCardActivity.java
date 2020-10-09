package breakthecode.com.clickandgo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;

import breakthecode.com.clickandgo.R;
import breakthecode.com.clickandgo.entity.RideResponse;
import breakthecode.com.clickandgo.resthelpers.RideRequestParameters;

public class RideCardActivity extends AppCompatActivity {

    private TextView rideCardRideCityFromName, rideCardRideCityFromBusStopName, rideCardRideCityFromBusStation,
            rideCardRideCityToName, rideCardRideCityToBusStopName, rideCardRideCityToBusStation, rideCardRideDate,
            rideCardRideTime, rideCardRidePrice;
    private RideResponse rideResponse;
    private RideRequestParameters rideRequestParameters;
    private Button rideCardBuyTicketButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_card);

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
        rideCardRideCityFromName = (TextView) findViewById(R.id.rideCardRideCityFromName);
        rideCardRideCityFromBusStopName = (TextView) findViewById(R.id.rideCardRideCityFromBusStopName);
        rideCardRideCityFromBusStation = (TextView) findViewById(R.id.rideCardRideCityFromBusStation);
        rideCardRideCityToName = (TextView) findViewById(R.id.rideCardRideCityToName);
        rideCardRideCityToBusStopName = (TextView) findViewById(R.id.rideCardRideCityToBusStopName);
        rideCardRideCityToBusStation = (TextView) findViewById(R.id.rideCardRideCityToBusStation);
        rideCardRideDate = (TextView) findViewById(R.id.rideCardRideDate);
        rideCardRideTime = (TextView) findViewById(R.id.rideCardRideTime);
        rideCardRidePrice = (TextView) findViewById(R.id.rideCardRidePrice);

        rideCardBuyTicketButton = (Button) findViewById(R.id.rideCardBuyTicketButton);
    }

    private void loadData(){
        rideRequestParameters = RideRequestParameters.getInstance();
    }

    private void loadWidgetsData(){

        loadRideObject();

        rideCardRideCityFromName.setText(rideResponse.getCityFrom().getCityName());
        String[] splitedFromBusStopName = rideResponse.getCityFrom().getBusStopName().split("-");
        if(splitedFromBusStopName.length == 2){
            rideCardRideCityFromBusStopName.setText(splitedFromBusStopName[0].trim());
            rideCardRideCityFromBusStation.setText(splitedFromBusStopName[1].trim());
        }
        rideCardRideCityToName.setText(rideResponse.getCityTo().getCityName());
        String[] splitedToBusStopName = rideResponse.getCityTo().getBusStopName().split("-");
        if(splitedToBusStopName.length == 2){
            rideCardRideCityToBusStopName.setText(splitedToBusStopName[0].trim());
            rideCardRideCityToBusStation.setText(splitedToBusStopName[1].trim());
        }
        int length = rideResponse.getRide().getRideTime().toString().length();
        String time = rideResponse.getRide().getRideTime().toString().substring(0, length-3);
        rideCardRideTime.setText(time);

        rideCardRideDate.setText(DateFormat.getDateInstance().format(rideRequestParameters.getDateOfRide().getTime()));
        String price = String.format("%.2f", rideResponse.getRide().getPrice());
        rideCardRidePrice.setText(price + " zł");
    }

    private void loadRideObject(){
        rideResponse = (RideResponse) getIntent().getExtras().getSerializable("rideObject");
    }
    private void setOnClickListeners(){
        rideCardBuyTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RideCardActivity.this, TicketBuyingActivity.class);
                intent.putExtra("rideObject", rideResponse);
                Pair<View,String> p1 = Pair.create((View)rideCardRideCityFromName, "ticketBuyingRideCityFromNameTN");
                Pair<View,String> p2 = Pair.create((View)rideCardRideCityToName, "ticketBuyingRideCityToNameTN");
                Pair<View,String> p3 = Pair.create((View)rideCardRideDate, "ticketBuyingRideCityRideDateTN");
                Pair<View,String> p4 = Pair.create((View)rideCardRideTime, "ticketBuyingRideCityRideTimeTN");
                Pair<View,String> p5 = Pair.create((View)rideCardRidePrice, "ticketBuyingRideCityRidePriceTN");


                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(RideCardActivity.this, p1,p2,p3,p4,p5);
                ActivityCompat.startActivity(RideCardActivity.this, intent, optionsCompat.toBundle());
            }
        });
    }
}