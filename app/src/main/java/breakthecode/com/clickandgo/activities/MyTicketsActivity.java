package breakthecode.com.clickandgo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import breakthecode.com.clickandgo.R;

public class MyTicketsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        settings();
        loadData();
        initWidgets();
        initWidgetsData();
        setOnClickListeners();
        setRecyclerView();
    }

    private void settings(){
        getSupportActionBar().hide();
    }

    private void loadData(){}

    private void initWidgets(){}

    private void initWidgetsData(){}

    private void setOnClickListeners(){}

    private void setRecyclerView(){}
}