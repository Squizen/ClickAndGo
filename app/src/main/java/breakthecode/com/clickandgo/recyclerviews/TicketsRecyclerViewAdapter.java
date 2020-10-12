package breakthecode.com.clickandgo.recyclerviews;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import breakthecode.com.clickandgo.entity.UserTicket;
import breakthecode.com.clickandgo.resthelpers.RideRequestParameters;

public class TicketsRecyclerViewAdapter extends RecyclerView.Adapter<TicketsRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "TicketsRecView myLogs";

    private List<UserTicket> listOfUserTickets = new ArrayList<>();

    private Context context;

    private RideRequestParameters rideRequestParameters;

    private TicketCallBack callback;

    public TicketsRecyclerViewAdapter(Context context, TicketCallBack callback){
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public TicketsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }
}
