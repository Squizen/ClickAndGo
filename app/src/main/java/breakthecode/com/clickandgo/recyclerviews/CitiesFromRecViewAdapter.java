package breakthecode.com.clickandgo.recyclerviews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import breakthecode.com.clickandgo.R;
import breakthecode.com.clickandgo.activities.MainPanelActivity;
import breakthecode.com.clickandgo.entity.City;
import breakthecode.com.clickandgo.resthelpers.RideRequestParameters;

public class CitiesFromRecViewAdapter extends RecyclerView.Adapter<CitiesFromRecViewAdapter.ViewHolder>{

    private static final String TAG = "myLogs CitiesRecV";
    private List<City> listOfCities = new ArrayList<City>();
    private final Context context;
    private final RideRequestParameters rideRequestParameters = RideRequestParameters.getInstance();
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public CitiesFromRecViewAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public CitiesFromRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_city_item_for_recyclerview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesFromRecViewAdapter.ViewHolder holder, final int position) {
        holder.cityName.setText(listOfCities.get(position).getCityName());
        holder.busStopName.setText(listOfCities.get(position).getBusStopName());
    }

    @Override
    public int getItemCount() {
        return listOfCities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView cityImg;
        private final TextView cityName;
        private final TextView busStopName;
        private final RelativeLayout itemPanel;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cityImg = itemView.findViewById(R.id.single_city_item_icon);
            cityName = itemView.findViewById(R.id.single_city_item_cityName);
            busStopName = itemView.findViewById(R.id.single_city_item_busStopName);
            itemPanel = itemView.findViewById(R.id.single_item_itemPanel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    public void setListOfCities(List<City> listOfCities){
        this.listOfCities = listOfCities;
        notifyDataSetChanged();
    }
}
