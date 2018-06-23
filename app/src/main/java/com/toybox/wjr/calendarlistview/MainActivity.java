package com.toybox.wjr.calendarlistview;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;

import com.toybox.wjr.calendarlistview.adapter.MonthAdapter;
import com.toybox.wjr.calendarlistview.holder.TopSequenceViewHolder;

public class MainActivity extends AppCompatActivity {
    RecyclerView calendarList;
    MonthAdapter calendarAdapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarList = findViewById(R.id.calendar_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        calendarList.setLayoutManager(linearLayoutManager);
        calendarAdapter = new MonthAdapter(new ImpDayEntityBuilder());
        calendarList.setAdapter(calendarAdapter);
        calendarList.scrollToPosition(calendarAdapter.getItemCount()-1);
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


}
