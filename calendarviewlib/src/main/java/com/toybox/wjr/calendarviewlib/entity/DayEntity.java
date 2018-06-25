package com.toybox.wjr.calendarviewlib.entity;

/**
 * Created by Administrator on 2018/6/20 0020.
 */

public class DayEntity {
    public String displayValue;
    public long date;
    public boolean isValid;
    public boolean isSelected;
    //同时isSelectedStart 和 isSelectedEnd 为true就显示圆
    public boolean isSelectedStart;
    public boolean isSelectedEnd;
}
