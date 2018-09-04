package com.zulfian.immscheduler;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ScheduleList extends ArrayAdapter<Schedule> {
    private Activity context;
    List<Schedule> schedules;

    public ScheduleList(Activity context, List<Schedule> schedules) {
        super(context, R.layout.layout_schedule_list, schedules);

        this.context = context;
        this.schedules = schedules;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_schedule_list, null, true);

        TextView textName = (TextView) listViewItem.findViewById(R.id.textName);
        TextView textLocation = (TextView) listViewItem.findViewById(R.id.textLocation);
        TextView textNote = (TextView) listViewItem.findViewById(R.id.textNote);
        TextView textDate = (TextView) listViewItem.findViewById(R.id.textDate);
        TextView textHour = (TextView) listViewItem.findViewById(R.id.textHour);

        Schedule schedule = schedules.get(position);
        textName.setText(schedule.getName());
        textLocation.setText(schedule.getLocation());
        textNote.setText(schedule.getNote());
        textDate.setText(schedule.getDate());
        textHour.setText(schedule.getHour());

        return listViewItem;
    }
}
