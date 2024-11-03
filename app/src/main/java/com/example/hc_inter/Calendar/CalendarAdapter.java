package com.example.hc_inter.Calendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hc_inter.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarView> {

    private final ArrayList<LocalDate> daysOfMonth;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<LocalDate> daysOfMonth, OnItemListener onItemListener) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    //method that handles the visual representation of the dates in calendar setting them up in squares
    @NonNull
    @Override
    public CalendarView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_dates, parent,false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.16666666);
        return new CalendarView(view, daysOfMonth, onItemListener);
    }


    //if the date that appears on the calendar belongs to the previous or the next month, the color will be gray.
    //Else, the color will be black
    @Override
    public void onBindViewHolder(@NonNull CalendarView holder, int position) {
        final LocalDate date = daysOfMonth.get(position);

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

        if(date.equals(CalendarUtils.selectedDate))
            holder.parentView.setBackgroundColor(Color.LTGRAY);

        if(date.getMonth().equals(CalendarUtils.selectedDate.getMonth())) {

            holder.dayOfMonth.setTextColor(Color.BLACK);

//            for (Event event : Event.eventsList) {
//                if (holder.dayOfMonth.equals(event.getDate()))
//                    holder.dayOfMonth.setTextColor(Color.RED);
//            }
        }
        else
            holder.dayOfMonth.setTextColor(Color.LTGRAY);
    }

    //gets the number of days in a month
    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    //interface of the on click method for dates that is implemented in CalendarActivity.java
    public interface OnItemListener {
        void onItemClick(int position, LocalDate date);
    }
}