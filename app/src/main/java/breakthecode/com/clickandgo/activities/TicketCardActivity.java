package breakthecode.com.clickandgo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import breakthecode.com.clickandgo.R;

public class TicketCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_card);

        settings();

    }
    private void settings(){
        getSupportActionBar().hide();
    }

    private void loadData(){}

    private void initWidgets(){}

    private void initWidgetsData(){}

    private void setOnClickListeners(){}
}