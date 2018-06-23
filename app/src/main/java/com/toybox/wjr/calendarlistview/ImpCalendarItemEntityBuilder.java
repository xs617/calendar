package com.toybox.wjr.calendarlistview;

import com.toybox.wjr.calendarlistview.entity.DayEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2018/6/21 0021.
 */

public class ImpCalendarItemEntityBuilder implements CalendarItemEntityBuilder {
    final int WEEK_DAY_COUNT = 7;

    private long getMonthFirstDayOfWeekTimeMilli(long dateMilli) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMilli);
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, -1 * (monthDay - 1));

        long returnTime = calendar.getTimeInMillis();
        return returnTime;
    }

    private List<DayEntity> buildMonthDayItems(final long fromDateMilli, final long toDateMilli, boolean isValid) {
        List<DayEntity> list = new ArrayList<DayEntity>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(toDateMilli);
        int toValue = calendar.get(Calendar.DAY_OF_MONTH);
        int toMonth = calendar.get(Calendar.MONTH);
        int toYear = calendar.get(Calendar.YEAR);

        calendar.setTimeInMillis(fromDateMilli);
        int fromValue = calendar.get(Calendar.DAY_OF_MONTH);
        int fromMonth = calendar.get(Calendar.MONTH);
        int fromYear = calendar.get(Calendar.YEAR);

        if (fromYear != toYear || fromMonth != toMonth || toValue < fromValue) {
            return list;
        }

        for (int i = 0; i < toValue - fromValue + 1; i++) {
            DayEntity entity = new DayEntity();
            entity.isValid = isValid;
            entity.displayValue = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            entity.date = calendar.getTimeInMillis();
            list.add(entity);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }


    private List<DayEntity> buildSplaceItem(final int count) {
        List<DayEntity> list = new ArrayList<DayEntity>();
        for (int i = 0; i < count; i++) {
            DayEntity entity = new DayEntity();
            entity.isValid = false;
            entity.displayValue = "";
            list.add(entity);
        }
        return list;
    }

    @Override
    public List<DayEntity> buildMonthDayEntities(long timeMilli) {
        List<DayEntity> list = new ArrayList<DayEntity>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMilli);
        //本月日数
        int monthDayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        //第一天是周几，即前面有monthFirstDayOfWeek-1个空格
        long monthFirstDayOfWeekTimeMilli = getMonthFirstDayOfWeekTimeMilli(timeMilli);
        calendar.setTimeInMillis(monthFirstDayOfWeekTimeMilli);
        int monthFirstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (calendar.getFirstDayOfWeek() == Calendar.SUNDAY) {
            monthFirstDayOfWeek -= 1;
            if (monthFirstDayOfWeek == 0){
                monthFirstDayOfWeek = 7;
            }
        }

        //本月的第几天
        calendar.setTimeInMillis(timeMilli);
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);

        int supplementEntityCount = (WEEK_DAY_COUNT - (calendar.get(Calendar.DAY_OF_WEEK) - 1)) % WEEK_DAY_COUNT;
        int detla = monthDayCount - monthDay - supplementEntityCount;
        int invalidDateCount = 0;
        int splaceCount = 0;
        if (detla > 0) {
            invalidDateCount = supplementEntityCount;
        } else {
            splaceCount = Math.abs(detla);
            invalidDateCount = supplementEntityCount + detla;
        }
        //填空格
        list.addAll(buildSplaceItem(monthFirstDayOfWeek - 1));
        //填有效日期
        list.addAll(buildMonthDayItems(monthFirstDayOfWeekTimeMilli, timeMilli, true));
        //填无效日期
        if (invalidDateCount > 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            long nextDateMilli = calendar.getTimeInMillis();
            calendar.add(Calendar.DAY_OF_MONTH, invalidDateCount - 1);
            long lastDateMilli = calendar.getTimeInMillis();
            list.addAll(buildMonthDayItems(nextDateMilli, lastDateMilli, false));
        }
        //填空格
        list.addAll(buildSplaceItem(splaceCount));
        return list;
    }

    @Override
    public List<List<DayEntity>> buildRangEntities(long fromMilli, int monthCount) {
        List<List<DayEntity>> list = new ArrayList<List<DayEntity>>();

        Calendar monthCalendar = Calendar.getInstance();
        monthCalendar.setTimeInMillis(fromMilli);
        int size = Math.abs(monthCount);
        int base = monthCount > 0 ? 1 : -1;
        for (int i = 0; i < size; i++) {
            Calendar dayCalendar = Calendar.getInstance();
            dayCalendar.setTimeInMillis(monthCalendar.getTimeInMillis());
            int dayIndex = dayCalendar.get(Calendar.DAY_OF_MONTH);
            int dayCount = dayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            dayCalendar.add(Calendar.DAY_OF_MONTH, dayCount - dayIndex);
            long endMilli = dayCalendar.getTimeInMillis();
            list.add(buildMonthDayEntities(endMilli));
            monthCalendar.add(Calendar.MONTH, base);
        }

        return list;
    }

}
