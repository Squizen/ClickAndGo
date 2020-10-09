package breakthecode.com.clickandgo.recyclerviews;

import android.widget.RelativeLayout;
import android.widget.TextView;

public interface RideCallback {

    void onRideItemClick(int position,
                         TextView single_ride_item_for_rec_view_rideCityFrom,
                         TextView single_ride_item_for_rec_view_rideCityTo,
                         TextView single_ride_item_for_rec_view_rideDate,
                         TextView single_ride_item_for_rec_view_rideTime,
                         TextView single_ride_item_for_rec_view_ridePrice);
}
