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
        this.events = new ArrayList<>(events);
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
        final EventModel e = events.get(position);

//        TextView titleTextView = holder.titleTextView;
//        titleTextView.setText(e.getTitle());
//
//        TextView urlTextView = holder.urlTextView;
//        urlTextView.setText(e.getUrl());
//
//        TextView dateTextView = holder.dateTextView;
//        dateTextView.setText(e.getStartTime() + " - " + e.getEndTime());
        holder.bind(e);

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setEvents(ArrayList<EventModel> events) {
        this.events = new ArrayList<>(events);
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

        public void bind(EventModel eventModel) {
            titleTextView.setText(eventModel.getTitle());
            urlTextView.setText(eventModel.getUrl());
            dateTextView.setText(eventModel.getStartTime() + " - " + eventModel.getEndTime());
        }
    }

    public EventModel removeItem(int position) {
        final EventModel model = events.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, EventModel eventModel) {
        events.add(position, eventModel);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final EventModel eventModel = events.remove(fromPosition);
        events.add(toPosition, eventModel);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(ArrayList<EventModel> e) {
        applyAndAnimateRemovals(e);
        applyAndAnimateAdditions(e);
        applyAndAnimateMovedItems(e);
    }

    private void applyAndAnimateRemovals(ArrayList<EventModel> newModels) {
        for (int i = events.size() - 1; i >= 0; i--) {
            final EventModel model = events.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<EventModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final EventModel model = newModels.get(i);
            if (!events.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<EventModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final EventModel model = newModels.get(toPosition);
            final int fromPosition = events.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
