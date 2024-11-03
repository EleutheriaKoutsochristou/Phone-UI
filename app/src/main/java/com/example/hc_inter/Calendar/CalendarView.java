package com.example.hc_inter.Calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hc_inter.R;

import java.time.LocalDate;
import java.util.ArrayList;


public class CalendarView extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final TextView dayOfMonth;
    private final ArrayList<LocalDate> days;
    public final View parentView;
    private final CalendarAdapter.OnItemListener onItemListener;


    //handling the calendar view
    public CalendarView(@NonNull View itemView, ArrayList<LocalDate> days, CalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.calendarDaysView);
        this.days = days;
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    //handles the clicking of a date
    @Override
    public void onClick(View v) {
        onItemListener.onItemClick(getAdapterPosition(),  days.get(getAdapterPosition()));
    }

}
