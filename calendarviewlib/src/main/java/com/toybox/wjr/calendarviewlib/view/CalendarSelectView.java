package com.toybox.wjr.calendarviewlib.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.toybox.wjr.calendarviewlib.CalendarSelectObserver;
import com.toybox.wjr.calendarviewlib.ImpDayEntityBuilder;
import com.toybox.wjr.calendarviewlib.R;
import com.toybox.wjr.calendarviewlib.adapter.MonthAdapter;
import com.toybox.wjr.calendarviewlib.holder.TopSequenceViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2018/6/25 0025.
 */

public class CalendarSelectView extends ConstraintLayout implements CalendarSelectObserver,View.OnClickListener {
    RecyclerView calendarList;
    MonthAdapter calendarAdapter;
    SwipeRefreshLayout refreshLayout;
    TextView selectDate;
    CheckBox isSelectAll;
    View selectAllLayout;
    TextView selectDateLabel;
    public CalendarSelectView(Context context) {
        super(context);
        init(context);
    }

    public CalendarSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.calendar_select_view, this, true);
        calendarList = findViewById(R.id.calendar_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        calendarList.setLayoutManager(linearLayoutManager);
        calendarAdapter = new MonthAdapter(new ImpDayEntityBuilder());
        calendarAdapter.setCalendarSelectObserver(this);
        calendarList.setAdapter(calendarAdapter);
        calendarList.scrollToPosition(calendarAdapter.getItemCount() - 1);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                calendarAdapter.getMoreDate();
                calendarAdapter.notifyDataSetChanged();
                linearLayoutManager.scrollToPositionWithOffset(12, calendarList.getMeasuredHeight() / 8);
                refreshLayout.setRefreshing(false);
            }
        });
        selectDate = findViewById(R.id.select_date);
        new TopSequenceViewHolder(findViewById(R.id.top_sequence)).build();

        selectAllLayout = findViewById(R.id.select_all_layout);
        selectAllLayout.setOnClickListener(this);
        selectDateLabel = findViewById(R.id.select_date_label);
        isSelectAll = findViewById(R.id.is_select_all);
    }

    SimpleDateFormat yearMonthDayFormat = new SimpleDateFormat("YYYY年M月d日", Locale.CHINA);
    SimpleDateFormat monthDayFormat = new SimpleDateFormat("M月d日", Locale.CHINA);

    @Override
    public void onCalendarSelectChange(long fromDate, long toDate, boolean isSelectAll) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentYear = calendar.get(Calendar.YEAR);
        if (fromDate > 0 && toDate > 0) {
            long tempDate;
            if (fromDate > toDate) {
                tempDate = toDate;
                toDate = fromDate;
                fromDate = tempDate;
            }
            calendar.setTimeInMillis(fromDate);
            int fromYear = calendar.get(Calendar.YEAR);
            calendar.setTimeInMillis(toDate);
            int toYear = calendar.get(Calendar.YEAR);
            if (fromYear == toYear && fromYear == currentYear) {
                selectDate.setText(monthDayFormat.format(fromDate) + "至" + monthDayFormat.format(toDate));
            } else {
                selectDate.setText(yearMonthDayFormat.format(fromDate) + "至" + yearMonthDayFormat.format(toDate));
            }
            selectDate.setVisibility(View.VISIBLE);
        } else if (fromDate > 0) {
            calendar.setTimeInMillis(fromDate);
            int fromYear = calendar.get(Calendar.YEAR);
            if (fromYear == currentYear) {
                selectDate.setText(monthDayFormat.format(fromDate));
            } else {
                selectDate.setText(yearMonthDayFormat.format(fromDate));
            }
            selectDate.setVisibility(View.VISIBLE);
        } else {
            selectDate.setVisibility(View.GONE);
        }
        if (isSelectAll){
            selectDateLabel.setText(R.string.selected_all);
            selectDate.setVisibility(GONE);
        }else if (fromDate > 0 || toDate > 0){
            selectDate.setVisibility(VISIBLE);
            selectDateLabel.setText("");
        }else{
            selectDateLabel.setText(R.string.selected_date_label);
            selectDate.setVisibility(GONE);
        }

        this.isSelectAll.setChecked(isSelectAll);
        if (customCalendarSelectObserver != null){
            customCalendarSelectObserver.onCalendarSelectChange(fromDate,toDate, isSelectAll);
        }
    }

    private CalendarSelectObserver customCalendarSelectObserver;
    public void setCalendarSelectObserver(CalendarSelectObserver calendarSelectObserver){
        customCalendarSelectObserver = calendarSelectObserver;
    }

    public void resetSelected() {
        if (calendarAdapter != null){
            calendarAdapter.resetSelected();
        }
    }

    public void setIsSelectAll(boolean isSelectAll){
        if (calendarAdapter != null){
            calendarAdapter.setIsSelectAll(isSelectAll);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.select_all_layout){
            isSelectAll.setChecked(!isSelectAll.isChecked());
            calendarAdapter.setIsSelectAll(isSelectAll.isChecked());
        }
    }

}
