package galodamadrugada.onhere.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import galodamadrugada.onhere.R;
import galodamadrugada.onhere.domain.Event;

/**
 * Created by GACCI on 30/03/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    private Context mContext;
    private List<Event> eventList;
    private EventAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id;
        public ImageView butonDelete;
        public RelativeLayout eventContainer;

        public MyViewHolder(View view) {
            super(view);
            name        = (TextView) view.findViewById(R.id.name);
            id          = (TextView) view.findViewById(R.id.id);
            butonDelete = (ImageView) view.findViewById(R.id.button_delete_event);
            eventContainer  = (RelativeLayout) view.findViewById(R.id.event_list_row);
        }
    }

    public EventAdapter (Context mContext, List<Event> events, EventAdapterListener listener) {
        this.mContext   = mContext;
        this.listener   = listener;
        eventList       = events;
    }

    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventAdapter.MyViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.name.setText(event.getName());
        holder.id.setText(event.getId());
        holder.butonDelete.findViewById(R.id.button_delete_event);

        applyClickEvents(holder, position);
    }

    private void applyClickEvents(MyViewHolder holder, final int position) {


        holder.eventContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEventAdapterRowClicked(position);
            }
        });

        holder.butonDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                listener.onEventDelete(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public interface EventAdapterListener{
        void onEventAdapterRowClicked(int position);

        void onEventDelete(int position);
    }


}

