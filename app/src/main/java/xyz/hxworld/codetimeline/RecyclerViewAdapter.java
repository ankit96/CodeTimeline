package xyz.hxworld.codetimeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kshitij Nagvekar on 3/7/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<EventModel> events;

    public RecyclerViewAdapter(ArrayList<EventModel> events) {
        this.events = events;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View eventView = inflater.inflate(R.layout.list_row, null);

        ViewHolder viewHolder = new ViewHolder(eventView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventModel e = events.get(position);

        TextView titleTextView = holder.titleTextView;
        titleTextView.setText(e.getTitle());

        TextView urlTextView = holder.urlTextView;
        urlTextView.setText(e.getUrl());

        TextView dateTextView = holder.dateTextView;
        dateTextView.setText(e.getStartTime() + " - " + e.getEndTime());

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView urlTextView;
        public TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            urlTextView = (TextView) itemView.findViewById(R.id.url);
            dateTextView = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
