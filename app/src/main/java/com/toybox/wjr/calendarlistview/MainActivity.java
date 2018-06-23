package com.toybox.wjr.calendarlistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.toybox.wjr.calendarlistview.adapter.MonthAdapter;
import com.toybox.wjr.calendarlistview.holder.MonthViewHolder;
import com.toybox.wjr.calendarlistview.holder.TopSequenceItemViewHolder;
import com.toybox.wjr.calendarlistview.holder.TopSequenceViewHolder;

public class MainActivity extends AppCompatActivity {
    RecyclerView calendarList;
    MonthAdapter calendarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarList = findViewById(R.id.calendar_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        calendarList.setLayoutManager(linearLayoutManager);
        calendarAdapter = new MonthAdapter(new ImpCalendarItemEntityBuilder());
        calendarList.setAdapter(calendarAdapter);

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
//
//        new TopSequenceViewHolder(findViewById(R.id.top_sequence)).build();
    }


}
