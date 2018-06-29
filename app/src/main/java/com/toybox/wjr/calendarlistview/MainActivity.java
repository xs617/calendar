package com.toybox.wjr.calendarlistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.toybox.wjr.calendarviewlib.CalendarSelectObserver;
import com.toybox.wjr.calendarviewlib.view.CalendarSelectView;


public class MainActivity extends AppCompatActivity  {

    CalendarSelectView calendarSelectView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarSelectView = findViewById(R.id.calendar_select_view);
        calendarSelectView.setCalendarSelectObserver(new CalendarSelectObserver() {
            @Override
            public void onCalendarSelectChange(long fromDate, long toDate, boolean isSelectAll) {
                Log.e("@@@@@",fromDate +"  " + toDate);
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

    }


}
