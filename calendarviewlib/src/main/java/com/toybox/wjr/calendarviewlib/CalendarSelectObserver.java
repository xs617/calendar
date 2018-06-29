package com.toybox.wjr.calendarviewlib;

/**
 * Created by Administrator on 2018/6/23 0023.
 */

public interface CalendarSelectObserver {
    void onCalendarSelectChange(long fromDate, long toDate, boolean isSelectAll);
}
