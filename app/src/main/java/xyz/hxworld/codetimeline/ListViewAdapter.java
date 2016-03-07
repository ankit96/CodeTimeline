package xyz.hxworld.codetimeline;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kshitij Nagvekar on 3/7/2016.
 */
public class ListViewAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<EventModel> events;

    public ListViewAdapter(Activity activity, ArrayList<EventModel> eventModelArrayList) {
        this.activity = activity;
        this.events = eventModelArrayList;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) convertView = inflater.inflate(R.layout.list_row, null);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView url = (TextView) convertView.findViewById(R.id.url);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        EventModel e = events.get(position);
        title.setText(e.getTitle());
        url.setText(e.getUrl());
        date.setText(e.getStartTime() + " - " + e.getEndTime());

        return convertView;
    }
}
