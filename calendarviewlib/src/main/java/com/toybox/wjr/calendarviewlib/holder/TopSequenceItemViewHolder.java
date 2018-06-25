package com.toybox.wjr.calendarviewlib.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toybox.wjr.calendarviewlib.R;


/**
 * Created by Administrator on 2018/6/21 0021.
 */

public class TopSequenceItemViewHolder extends RecyclerView.ViewHolder {
    TextView itemCalendar;

    public TopSequenceItemViewHolder(View itemView, int width) {
        super(itemView);
        itemCalendar = itemView.findViewById(R.id.item_calendar);
        ViewGroup.LayoutParams layoutParams = itemCalendar.getLayoutParams();
        layoutParams.width = width;
        itemCalendar.setLayoutParams(layoutParams);
    }

    public void onBind(String data) {
        itemCalendar.setText(data);
    }

    static public TopSequenceItemViewHolder build(@NonNull ViewGroup parent, int width) {
        return new TopSequenceItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_top_text, parent, false), width);
    }
}
