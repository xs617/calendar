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

public class MainActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }


}
