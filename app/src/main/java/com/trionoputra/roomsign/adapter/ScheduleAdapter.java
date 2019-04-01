package com.trionoputra.roomsign.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trionoputra.roomsign.R;
import com.trionoputra.roomsign.entity.Event;

import java.util.ArrayList;
import java.util.List;


public class ScheduleAdapter extends BaseAdapter {
 
    private List<Event> dtList = new ArrayList<Event>();
    private Activity context;
    private LayoutInflater inflater;
    public ScheduleAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ScheduleAdapter(Activity context, List<Event> data) {

        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder {
        TextView text1;
        TextView text2;
        TextView text3;
        View bullet;
    }

    public int getCount() {
        return dtList.size();
    }

    public List<Event> getData() {
        return dtList;
    }

    public void set(List<Event> list) {
    	dtList = list;
        notifyDataSetChanged();
    }

    public void setOngoing(Event event) {
        for(int i = 0;i < dtList.size();i++)
        {
            Event e = dtList.get(i);
            if(event.getId() == e.getId())
            {
                e.setOngoing(true);
                dtList.set(i,e);
            }
        }
        notifyDataSetChanged();
    }

    public Event getItem(int position) {
        return dtList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
        	vi = inflater.inflate(R.layout.schedule_item, null);
            holder = new ViewHolder();
            holder.text1 = (TextView) vi.findViewById(R.id.textView1);
            holder.text2 = (TextView) vi.findViewById(R.id.textView2);
            holder.text3 = (TextView) vi.findViewById(R.id.textView3);
            holder.bullet = (View) vi.findViewById(R.id.bullet);
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }

        Event data = (Event) getItem(position);
        holder.text1.setText(data.getTitle());
        holder.text2.setText(data.getTimeFrom().toString() + " - " + data.getTimeTo());
    //    holder.text3.setText("["+data.getDivision()+"]" + data.getPic() + "/" + String.valueOf(data.getAmount()) + " person");
        holder.text3.setText("["+data.getDivision()+"]" + data.getPic());
        holder.bullet.setVisibility(View.INVISIBLE);
        if(data.isOngoing())
            holder.bullet.setVisibility(View.VISIBLE);

        return vi;
    }


}