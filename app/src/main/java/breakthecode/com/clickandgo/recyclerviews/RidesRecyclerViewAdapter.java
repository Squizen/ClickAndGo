package breakthecode.com.clickandgo.recyclerviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import breakthecode.com.clickandgo.R;
import breakthecode.com.clickandgo.entity.Ride;
import breakthecode.com.clickandgo.resthelpers.RideRequestParameters;

public class RidesRecyclerViewAdapter extends RecyclerView.Adapter<RidesRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "myLogs CitiesRecV";
    private List<Ride> listOfRides = new ArrayList<>();

    private final Context context;
//    private OnItemClickListener listener;
    private RideRequestParameters rideRequestParameters;

    private final RideCallback callback;

//    public interface OnItemClickListener{
//        void onItemClick(int position);
//    }
//    public void setOnItemClickListener(OnItemClickListener listener){
//        this.listener = listener;
//    }

    public RidesRecyclerViewAdapter(Context context, RideCallback callback){
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public RidesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_ride_item_for_rec_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RidesRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.cityFromName.setText(listOfRides.get(position).getCityFrom().getCityName());
        holder.cityToName.setText(listOfRides.get(position).getCityTo().getCityName());
        int length = listOfRides.get(position).getRideTime().toString().length();
        String time = listOfRides.get(position).getRideTime().toString().substring(0, length-3);
        holder.rideDate.setText(DateFormat.getDateInstance().format(rideRequestParameters.getDateOfRide().getTime()));
        holder.rideTime.setText(time);
        String price = String.format("%.2f", listOfRides.get(position).getPrice());
        holder.ridePrice.setText(price + " zł");
    }

    @Override
    public int getItemCount() {
        return listOfRides.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView cityFromName;
        private final TextView cityToName;
        private final TextView rideTime;
        private final TextView rideDate;
        private final TextView ridePrice;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            cityFromName = itemView.findViewById(R.id.single_ride_item_for_rec_view_rideCityFrom);
            cityToName = itemView.findViewById(R.id.single_ride_item_for_rec_view_rideCityTo);
            rideTime = itemView.findViewById(R.id.single_ride_item_for_rec_view_rideTime);
            rideDate = itemView.findViewById(R.id.single_ride_item_for_rec_view_rideDate);
            ridePrice = itemView.findViewById(R.id.single_ride_item_for_rec_view_ridePrice);

            rideRequestParameters = RideRequestParameters.getInstance();

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(listener != null){
//                        int position = getAdapterPosition();
//                        if(position != RecyclerView.NO_POSITION){
//                            listener.onItemClick(position);
//                        }
//                    }
//                }
//            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onRideItemClick(getAdapterPosition(), cityFromName, cityToName, rideDate, rideTime, ridePrice);
                }
            });
        }
    }
    public void setListOfRides(List<Ride> listOfRides){
        this.listOfRides = listOfRides;
        notifyDataSetChanged();
    }
}
