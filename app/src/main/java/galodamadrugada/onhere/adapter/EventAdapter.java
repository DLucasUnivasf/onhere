package galodamadrugada.onhere.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import galodamadrugada.onhere.R;
import galodamadrugada.onhere.domain.Event;

/**
 * Created by GACCI on 30/03/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    private List<Event> eventList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id, description;

        public MyViewHolder(View view) {
            super(view);
            name        = (TextView) view.findViewById(R.id.name);
            id          = (TextView) view.findViewById(R.id.id);
            description = (TextView) view.findViewById(R.id.description);
        }
    }

    public EventAdapter (List<Event> events) {
        eventList = events;
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
        holder.description.setText(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


}

