package com.toybox.wjr.calendarlistview;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.toybox.wjr.calendarlistview.adapter.MonthAdapter;
import com.toybox.wjr.calendarlistview.holder.TopSequenceViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements CalendarSelectObserver {
    RecyclerView calendarList;
    MonthAdapter calendarAdapter;
    SwipeRefreshLayout refreshLayout;
    TextView selectDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarList = findViewById(R.id.calendar_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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
//        final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                if (calendarList != null){
//                    View child =  calendarList.findChildViewUnder(e.getX(),e.getY());
//                    RecyclerView.ViewHolder viewHolder = calendarList.getChildViewHolder(child);
//                    if (viewHolder instanceof MonthViewHolder){
//                        ((MonthViewHolder) viewHolder).disposeTouchEvent(e);
//                    }
//                }
//                return true;
//            }
//        });
//        calendarList.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                return gestureDetector.onTouchEvent(e);
//            }
//        });

        new TopSequenceViewHolder(findViewById(R.id.top_sequence)).build();
    }

    SimpleDateFormat yearMonthDayFormat = new SimpleDateFormat("YYYY年M月d日", Locale.CHINA);
    SimpleDateFormat monthDayFormat = new SimpleDateFormat("M月d日", Locale.CHINA);

    @Override
    public void onCalendarSelectChange(long fromDate, long toDate) {
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
            }else{
                selectDate.setText(yearMonthDayFormat.format(fromDate));
            }
            selectDate.setVisibility(View.VISIBLE);
        }else{
            selectDate.setVisibility(View.GONE);
        }
    }
}
