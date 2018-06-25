package com.toybox.wjr.calendarviewlib.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toybox.wjr.calendarviewlib.OnDayClickListener;
import com.toybox.wjr.calendarviewlib.R;
import com.toybox.wjr.calendarviewlib.entity.MonthEntity;
import com.toybox.wjr.calendarviewlib.view.CalendarMonthItemView;

import java.util.Calendar;

/**
 * 一个月的日历
 * Created by Administrator on 2018/6/21 0021.
 */

public class MonthViewHolder extends RecyclerView.ViewHolder {
    TextView monthLabel;
    MonthEntity monthEntity;
    CalendarMonthItemView calendarMonthItemView;
    Calendar calendar;

    public MonthViewHolder(View itemView) {
        super(itemView);
        calendarMonthItemView = itemView.findViewById(R.id.calendar_month_view);
        monthLabel = itemView.findViewById(R.id.month_label);
        calendar = Calendar.getInstance();
    }

    public void onBind(MonthEntity monthItemEntity) {
        this.monthEntity = monthItemEntity;
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        String yearMonth = monthItemEntity.month + "月";
        if (year != monthItemEntity.year) {
            yearMonth = String.valueOf(monthItemEntity.year) + "年" + (monthItemEntity.month + 1) + "月";
        }
        monthLabel.setText(yearMonth);
        if (calendarMonthItemView != null) {
            calendarMonthItemView.onBind(monthItemEntity);
            calendarMonthItemView.requestLayout();
        }
    }

    static public MonthViewHolder build(@NonNull ViewGroup parent) {
        return new MonthViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent, false));
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        if (calendarMonthItemView != null) {
            calendarMonthItemView.setOnDayClickListener(onDayClickListener);
        }
    }

//    Rect globalVisibleRect = new Rect();
//    int[] itemLocation = new int[2];
//    int[] calendarLocation = new int[2];

//    public void disposeTouchEvent(MotionEvent e) {
//        if (calendarMonthView == null) {
//            return;
//        }
//        calendarMonthView.getGlobalVisibleRect(globalVisibleRect);
//        itemView.getLocationOnScreen(itemLocation);
//        calendarMonthView.getLocationOnScreen(calendarLocation);
//        if (globalVisibleRect.contains((int) e.getRawX(), (int) e.getRawY())) {
//            MotionEvent disPoseEvent = MotionEvent.obtain(e);
//            disPoseEvent.offsetLocation(itemLocation[0] - calendarLocation[0], itemLocation[1] -calendarLocation[1] );
//            calendarMonthView.disposeTouchEvent(disPoseEvent);
//        }
//    }
}
