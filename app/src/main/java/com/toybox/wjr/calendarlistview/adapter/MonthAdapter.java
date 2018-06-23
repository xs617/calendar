package com.toybox.wjr.calendarlistview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.toybox.wjr.calendarlistview.DayEntityBuilder;
import com.toybox.wjr.calendarlistview.OnDayClickListener;
import com.toybox.wjr.calendarlistview.entity.DayEntity;
import com.toybox.wjr.calendarlistview.entity.MonthEntity;
import com.toybox.wjr.calendarlistview.holder.MonthViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2018/6/21 0021.
 */

public class MonthAdapter extends RecyclerView.Adapter implements OnDayClickListener {
    private final int TYPE_CALENDAR_ITEM = 2;
    DayEntityBuilder dayEntityBuilder;
    List<MonthEntity> monthItemEntities = new ArrayList<MonthEntity>();
    long currentLatestDate;

    public MonthAdapter(DayEntityBuilder dayEntityBuilder) {
        this.dayEntityBuilder = dayEntityBuilder;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        MonthEntity monthItemEntity = new MonthEntity();
        monthItemEntity.dayItemEntities = this.dayEntityBuilder.buildMonthDayEntities(System.currentTimeMillis());
        monthItemEntity.month = calendar.get(Calendar.MONTH);
        monthItemEntity.year = calendar.get(Calendar.YEAR);

        //从11个月前开始，总计生成11个月
        calendar.add(Calendar.MONTH, -11);
        currentLatestDate = calendar.getTimeInMillis();
        List<List<DayEntity>> monthList = dayEntityBuilder.buildRangEntities(calendar.getTimeInMillis(), 11);

        for (int i = 0; i < 11; i++) {
            MonthEntity monthEntity = new MonthEntity();
            monthEntity.dayItemEntities = monthList.get(i);
            monthEntity.month = calendar.get(Calendar.MONTH);
            monthEntity.year = calendar.get(Calendar.YEAR);
            calendar.add(Calendar.MONTH, 1);
            monthItemEntities.add(monthEntity);
        }

        monthItemEntities.add(monthItemEntity);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CALENDAR_ITEM:
            default:
                MonthViewHolder monthViewHolder = MonthViewHolder.build(parent);
                monthViewHolder.setOnDayClickListener(this);
                return monthViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MonthViewHolder) {
            ((MonthViewHolder) holder).onBind(monthItemEntities.get(position));

        }
    }

    @Override
    public int getItemCount() {
        return monthItemEntities.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_CALENDAR_ITEM;
    }

    DayEntity startSelectedDay;
    DayEntity endSelectedDay;
    MonthEntity startMonth;
    MonthEntity endMonth;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");

    private boolean isSameDay(long d1, long d2) {
        return simpleDateFormat.format(d1).equals(simpleDateFormat.format(d2));
    }

    private void batchSetDayCalendarSelectStatus() {
        if (startMonth == null || endMonth == null || startSelectedDay == null || endSelectedDay == null) {
            return;
        }
        int startDayIndex = startMonth.dayItemEntities.indexOf(startSelectedDay);
        int endDayIndex = endMonth.dayItemEntities.indexOf(endSelectedDay);
        int startMonthIndex = monthItemEntities.indexOf(startMonth);
        int endMonthIndex = monthItemEntities.indexOf(endMonth);

        if (startMonth != endMonth) {
            //设置startMonth
            for (int i = startDayIndex; i < startMonth.dayItemEntities.size(); i++) {
                setDaySelectedStatus(startMonth.dayItemEntities.get(i));
            }
            //设置中间Month
            for (int i = startMonthIndex + 1; i < endMonthIndex; i++) {
                MonthEntity monthEntity = monthItemEntities.get(i);
                for (int r = 0; r < monthEntity.dayItemEntities.size(); r++) {
                    setDaySelectedStatus(monthEntity.dayItemEntities.get(r));
                }
            }
            //设置endMonth
            for (int i = 0; i < endDayIndex + 1; i++) {
                setDaySelectedStatus(endMonth.dayItemEntities.get(i));
            }
            startSelectedDay.isSelectedStart = true;
            endSelectedDay.isSelectedEnd = true;
            notifyItemRangeChanged(startMonthIndex, endMonthIndex - startMonthIndex + 1);
        } else {
            for (int i = startDayIndex; i < endDayIndex + 1; i++) {
                setDaySelectedStatus(startMonth.dayItemEntities.get(i));
            }
            startSelectedDay.isSelectedStart = true;
            endSelectedDay.isSelectedEnd = true;
            notifyItemChanged(startMonthIndex);
        }

    }

    private void setDaySelectedStatus(DayEntity dayEntity) {
        if (dayEntity == null) {
            return;
        }
        dayEntity.isSelected = true;
        dayEntity.isSelectedEnd = false;
        dayEntity.isSelectedStart = false;
    }

    private void resetDaySelectStatus(List<DayEntity> dayItemEntities) {
        for (int i = 0; i < dayItemEntities.size(); i++) {
            DayEntity dayEntity = dayItemEntities.get(i);
            dayEntity.isSelected = false;
            dayEntity.isSelectedStart = false;
            dayEntity.isSelectedEnd = false;
        }
    }

    @Override
    public void onDayClick(MonthEntity monthEntity, DayEntity dayEntity) {
        //选中了start和end后点击，取消旧的，再单独选中新的
        if (startSelectedDay != null && endSelectedDay != null) {
            int startMonthIndex = monthItemEntities.indexOf(startMonth);
            int endMonthIndex = monthItemEntities.indexOf(endMonth);
            if (startMonth != null && endMonth != null) {
                for (int i = startMonthIndex; i < endMonthIndex + 1; i++) {
                    resetDaySelectStatus(monthItemEntities.get(i).dayItemEntities);
                }
            }
            endMonth = null;
            endSelectedDay = null;
            startMonth = monthEntity;
            startSelectedDay = dayEntity;
            startSelectedDay.isSelectedEnd = true;
            startSelectedDay.isSelectedStart = true;
            startSelectedDay.isSelected = true;
            notifyItemRangeChanged(startMonthIndex, endMonthIndex - startMonthIndex + 1);
            notifyItemChanged(monthItemEntities.indexOf(monthEntity));
        } else if (startSelectedDay != null) {
            //已经选中了一个，如果新的和旧的不是 同一个就连成区域
            if (isSameDay(startSelectedDay.date, dayEntity.date)) {
                startSelectedDay.isSelected = false;
                startSelectedDay.isSelectedStart = false;
                startSelectedDay.isSelectedEnd = false;
                notifyItemChanged(monthItemEntities.indexOf(startMonth));
                startMonth = null;
                startSelectedDay = null;
            } else if (startSelectedDay.date > dayEntity.date) {
                endSelectedDay = startSelectedDay;
                startSelectedDay = dayEntity;
                endMonth = startMonth;
                startMonth = monthEntity;
                batchSetDayCalendarSelectStatus();
            } else {
                endSelectedDay = dayEntity;
                endMonth = monthEntity;
                batchSetDayCalendarSelectStatus();
            }
        } else {
            startSelectedDay = dayEntity;
            startMonth = monthEntity;
            startSelectedDay.isSelectedEnd = true;
            startSelectedDay.isSelectedStart = true;
            startSelectedDay.isSelected = true;
            notifyItemChanged(monthItemEntities.indexOf(monthEntity));
        }
    }

    public void getMoreDate() {
        Calendar calendar = Calendar.getInstance();
        int count = 12;
        //从12个月前开始，总计生成12个月
        calendar.setTimeInMillis(currentLatestDate);
        calendar.add(Calendar.MONTH, count * -1);
        currentLatestDate = calendar.getTimeInMillis();
        List<List<DayEntity>> monthList = dayEntityBuilder.buildRangEntities(calendar.getTimeInMillis(), count);
        List<MonthEntity> monthEntities = new ArrayList<MonthEntity>();
        for (int i = 0; i < count; i++) {
            MonthEntity monthEntity = new MonthEntity();
            monthEntity.dayItemEntities = monthList.get(i);
            monthEntity.month = calendar.get(Calendar.MONTH);
            monthEntity.year = calendar.get(Calendar.YEAR);
            calendar.add(Calendar.MONTH, 1);
            monthEntities.add(monthEntity);
        }
        monthItemEntities.addAll(0, monthEntities);
    }
}
