package com.toybox.wjr.calendarlistview;

import android.content.Context;

/**
 * Created by Administrator on 2018/6/22 0022.
 */

public class CalendarUtil {
    public static final int get2BaseAverageSpace(int width, int spanCount) {
        int base;
        if (spanCount % 2 == 0) {
            base = spanCount;
        } else {
            base = spanCount * 2;
        }
        int space = width % base;
        return space;
    }

    public static final String getCalendarMonth(Context context, int month) {
        int id = -1;
        switch (month) {
            case 0:
                id = R.string.Jan;
                break;
            case 1:
                id = R.string.Feb;
                break;
            case 2:
                id = R.string.Mar;
                break;
            case 3:
                id = R.string.Apr;
                break;
            case 4:
                id = R.string.May;
                break;
            case 5:
                id = R.string.Jun;
                break;
            case 6:
                id = R.string.Jul;
                break;
            case 7:
                id = R.string.Aug;
                break;
            case 8:
                id = R.string.Sept;
                break;
            case 9:
                id = R.string.Oct;
                break;
            case 10:
                id = R.string.Nov;
                break;
            case 11:
                id = R.string.Dec;
                break;
        }
        if (id == -1) {
            return "";
        }
        return context.getResources().getString(id);
    }
}
