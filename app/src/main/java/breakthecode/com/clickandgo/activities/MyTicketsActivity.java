package breakthecode.com.clickandgo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;

import java.util.ArrayList;

import breakthecode.com.clickandgo.R;
import breakthecode.com.clickandgo.classes.AppSharedPreferencesHelper;
import breakthecode.com.clickandgo.entity.UserTicket;
import breakthecode.com.clickandgo.recyclerviews.TicketCallBack;
import breakthecode.com.clickandgo.recyclerviews.TicketsRecyclerViewAdapter;

public class MyTicketsActivity extends AppCompatActivity implements TicketCallBack {

    private AppSharedPreferencesHelper sharedPreferencesHelper;

    private ArrayList<UserTicket> listOfUserTickets;

    private TicketsRecyclerViewAdapter ticketsRecyclerViewAdapter;

    private RecyclerView myTicketsActivity_rideRecView;

    private Button myTicketsActivity_deleteAllTickets;

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

    private void loadData(){
        sharedPreferencesHelper = new AppSharedPreferencesHelper(this, "Shared_Preferences");
        listOfUserTickets = sharedPreferencesHelper.getListOfUsersTickets();
    }

    private void initWidgets(){
        myTicketsActivity_rideRecView = (RecyclerView) findViewById(R.id.myTicketsActivity_rideRecView);
        myTicketsActivity_deleteAllTickets = (Button) findViewById(R.id.myTicketsActivity_deleteAllTickets);

    }

    private void initWidgetsData(){}

    private void setOnClickListeners(){
        myTicketsActivity_deleteAllTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOfUserTickets.clear();
                sharedPreferencesHelper.setListOfUsersTickets(listOfUserTickets);
                setRecyclerView();
            }
        });
    }

    private void setRecyclerView(){
        ticketsRecyclerViewAdapter = new TicketsRecyclerViewAdapter(getApplicationContext(), MyTicketsActivity.this);
        myTicketsActivity_rideRecView.setAdapter(ticketsRecyclerViewAdapter);
        myTicketsActivity_rideRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        myTicketsActivity_rideRecView.setItemAnimator(new DefaultItemAnimator());
        layoutAnimation(myTicketsActivity_rideRecView);
        ticketsRecyclerViewAdapter.setListOfuserTickets(listOfUserTickets);
        ticketsRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTicketItemClick(int position) {

    }
    private void layoutAnimation(RecyclerView recView) {
        Context context = recView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        recView.setLayoutAnimation(layoutAnimationController);
        recView.getAdapter().notifyDataSetChanged();
        recView.scheduleLayoutAnimation();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyTicketsActivity.this, MainPanelActivity.class));
    }
}