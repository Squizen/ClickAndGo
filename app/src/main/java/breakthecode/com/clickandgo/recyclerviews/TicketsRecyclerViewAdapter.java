package breakthecode.com.clickandgo.recyclerviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.List;

import breakthecode.com.clickandgo.R;
import breakthecode.com.clickandgo.entity.UserTicket;
import breakthecode.com.clickandgo.resthelpers.RideRequestParameters;

public class TicketsRecyclerViewAdapter extends RecyclerView.Adapter<TicketsRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "TicketsRecView myLogs";

    private List<UserTicket> listOfUserTickets = new ArrayList<>();

    private final Context context;

    private RideRequestParameters rideRequestParameters;

    private final TicketCallBack callback;

    public TicketsRecyclerViewAdapter(Context context, TicketCallBack callback){
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public TicketsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_ticket_item_for_recyclerview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.single_ticket_item_QRIcon.setImageBitmap(generateQRcode(listOfUserTickets.get(position).getTicket().getTicketNumber()));
        String rideRelation = listOfUserTickets.get(position).getCityFrom().getCityName() + " - " + listOfUserTickets.get(position).getCityTo().getCityName();
        holder.single_ticket_item_LastTicketRideCitiesNamesTxt.setText(rideRelation);
        holder.single_ticket_item_LastTicketRideDateTxt.setText(listOfUserTickets.get(position).getTicket().getRideDate().toString());
        holder.single_ticket_item_LastTicketRideTimeTxt.setText(listOfUserTickets.get(position).getTicket().getExpiringTime().toString());
    }

    @Override
    public int getItemCount() {
        return listOfUserTickets.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView single_ticket_item_QRIcon;
        TextView single_ticket_item_LastTicketRideCitiesNamesTxt;
        TextView single_ticket_item_LastTicketRideDateTxt;
        TextView single_ticket_item_LastTicketRideTimeTxt;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            single_ticket_item_QRIcon = itemView.findViewById(R.id.single_ticket_item_QRIcon);
            single_ticket_item_LastTicketRideCitiesNamesTxt = itemView.findViewById(R.id.single_ticket_item_LastTicketRideCitiesNamesTxt);
            single_ticket_item_LastTicketRideDateTxt = itemView.findViewById(R.id.single_ticket_item_LastTicketRideDateTxt);
            single_ticket_item_LastTicketRideTimeTxt = itemView.findViewById(R.id.single_ticket_item_LastTicketRideTimeTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onTicketItemClick(getAdapterPosition());
                }
            });
        }
    }

    public void setListOfuserTickets(ArrayList<UserTicket> listOfUserTickets){
        this.listOfUserTickets = listOfUserTickets;
        notifyDataSetChanged();
    }

    private Bitmap generateQRcode(String ticketNumber){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Bitmap bitmap = Bitmap.createBitmap(100,100, Bitmap.Config.RGB_565);
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(ticketNumber, BarcodeFormat.QR_CODE, 100, 100);
            bitmap = Bitmap.createBitmap(100,100, Bitmap.Config.RGB_565);
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
