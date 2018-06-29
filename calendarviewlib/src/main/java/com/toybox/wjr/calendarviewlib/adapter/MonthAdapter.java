package com.toybox.wjr.calendarviewlib.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.toybox.wjr.calendarviewlib.CalendarSelectObserver;
import com.toybox.wjr.calendarviewlib.DayEntityBuilder;
import com.toybox.wjr.calendarviewlib.OnDayClickListener;
import com.toybox.wjr.calendarviewlib.entity.DayEntity;
import com.toybox.wjr.calendarviewlib.entity.MonthEntity;
import com.toybox.wjr.calendarviewlib.holder.MonthViewHolder;

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
    boolean isSelectAll;

    DayEntity startSelectedDay;
    DayEntity endSelectedDay;
    MonthEntity startMonth;
    MonthEntity endMonth;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");

    public MonthAdapter(DayEntityBuilder dayEntityBuilder) {
        this.dayEntityBuilder = dayEntityBuilder;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        MonthEntity monthItemEntity = new MonthEntity();
        monthItemEntity.dayItemEntities = this.dayEntityBuilder.buildMonthDayEntities(System.currentTimeMillis(), false);
        monthItemEntity.month = calendar.get(Calendar.MONTH);
        monthItemEntity.year = calendar.get(Calendar.YEAR);

        //从11个月前开始，总计生成11个月
        calendar.add(Calendar.MONTH, -11);
        currentLatestDate = calendar.getTimeInMillis();
        List<List<DayEntity>> monthList = dayEntityBuilder.buildRangEntities(calendar.getTimeInMillis(), 11, false);

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


    /**
     * 向前生成一年份的日历
     */
    public void getMoreDate() {
        Calendar calendar = Calendar.getInstance();
        int count = 12;
        //从12个月前开始，总计生成12个月
        calendar.setTimeInMillis(currentLatestDate);
        calendar.add(Calendar.MONTH, count * -1);
        currentLatestDate = calendar.getTimeInMillis();
        List<List<DayEntity>> monthList = dayEntityBuilder.buildRangEntities(calendar.getTimeInMillis(), count, isSelectAll);
        List<MonthEntity> monthEntities = new ArrayList<MonthEntity>();
        for (int i = 0; i < count; i++) {
            MonthEntity monthEntity = new MonthEntity();
            monthEntity.dayItemEntities = monthList.get(i);
            monthEntity.month = calendar.get(Calendar.MONTH);
            monthEntity.year = calendar.get(Calendar.YEAR);
            calendar.add(Calendar.MONTH, 1);
            monthEntities.add(monthEntity);
        }
        if (isSelectAll) {
            //旧的头
            if (startMonth != null && startMonth.dayItemEntities != null){
                int startDayIndex = startMonth.dayItemEntities.indexOf(startSelectedDay);
                for (int i=0;i<startDayIndex +1;i++){
                    DayEntity dayEntity=  startMonth.dayItemEntities.get(i);
                    setDaySelectedStatus(dayEntity);
                }
            }
            //新的头
            if (!monthList.isEmpty()) {
                startMonth = monthEntities.get(0);
                for (int i = 0; i < startMonth.dayItemEntities.size(); i++) {
                    DayEntity dayEntity = startMonth.dayItemEntities.get(i);
                    if (dayEntity.isValid) {
                        setStartSelectedDay(dayEntity);
                        break;
                    }else{
                        resetDaySelectedStatus(dayEntity);
                    }
                }
            }
        }

        monthItemEntities.addAll(0, monthEntities);
    }


    /**
     * 日期选中点击事件
     *
     * @param monthEntity
     * @param dayEntity
     */
    @Override
    public void onDayClick(MonthEntity monthEntity, DayEntity dayEntity) {
        if (isSelectAll) {
            isSelectAll = false;
            resetSelected();
            notifyDataSetChanged();
            notifySelectedDateChange();
            return;
        }
        //选中了start和end后点击，取消旧的，再单独选中新的
        if (startSelectedDay != null && endSelectedDay != null) {
            resetSelected();
            startMonth = monthEntity;
            startSelectedDay = dayEntity;
            setDaySingleSelectedStatus(startSelectedDay);
            notifyItemChanged(monthItemEntities.indexOf(monthEntity));
        } else if (startSelectedDay != null) {
            //已经选中了一个，如果新的和旧的不是 同一个就连成区域
            if (isSameDay(startSelectedDay.date, dayEntity.date)) {
                resetDaySelectedStatus(startSelectedDay);
                notifyItemChanged(monthItemEntities.indexOf(startMonth));
                startMonth = null;
                startSelectedDay = null;
            } else if (startSelectedDay.date > dayEntity.date) {
                endSelectedDay = startSelectedDay;
                startSelectedDay = dayEntity;
                endMonth = startMonth;
                startMonth = monthEntity;
                batchSetDaySelectStatus();
            } else {
                endSelectedDay = dayEntity;
                endMonth = monthEntity;
                batchSetDaySelectStatus();
            }
        } else {
            startSelectedDay = dayEntity;
            startMonth = monthEntity;
            setDaySingleSelectedStatus(startSelectedDay);
            notifyItemChanged(monthItemEntities.indexOf(monthEntity));
        }
        notifySelectedDateChange();
    }

    private boolean isSameDay(long d1, long d2) {
        return simpleDateFormat.format(d1).equals(simpleDateFormat.format(d2));
    }


    /**
     * 批量设置日期选中状态
     */
    private void batchSetDaySelectStatus() {
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

    /**
     * 清空所有选中状态
     */
    public void resetSelected() {
        int startMonthIndex = monthItemEntities.indexOf(startMonth);
        int endMonthIndex = monthItemEntities.indexOf(endMonth);
        if (startMonth != null && endMonth != null) {
            for (int i = startMonthIndex; i < endMonthIndex + 1; i++) {
                resetDaySelectedStatus(monthItemEntities.get(i).dayItemEntities);
            }
        } else if (startSelectedDay != null) {
            resetDaySelectedStatus(startSelectedDay);
            notifyItemChanged(startMonthIndex);
        } else if (endSelectedDay != null) {
            resetDaySelectedStatus(endSelectedDay);
            notifyItemChanged(endMonthIndex);
        }
        endMonth = null;
        endSelectedDay = null;
        startMonth = null;
        startSelectedDay = null;
        notifyItemRangeChanged(startMonthIndex, endMonthIndex - startMonthIndex + 1);
        notifySelectedDateChange();
    }


    private CalendarSelectObserver calendarSelectObserver;


    /**
     * 设置成单选状态
     *
     * @param dayEntity
     */
    private void setDaySingleSelectedStatus(DayEntity dayEntity) {
        if (dayEntity != null) {
            startSelectedDay.isSelectedEnd = true;
            startSelectedDay.isSelectedStart = true;
            startSelectedDay.isSelected = true;
        }
    }

    /***
     * 设置成start,end之间的选中状态
     * @param dayEntity
     */
    private void setDaySelectedStatus(DayEntity dayEntity) {
        if (dayEntity != null) {
            dayEntity.isSelected = true;
            dayEntity.isSelectedEnd = false;
            dayEntity.isSelectedStart = false;
        }

    }

    /**
     * 设置成未选中状态
     *
     * @param dayEntity
     */
    private void resetDaySelectedStatus(DayEntity dayEntity) {
        if (dayEntity != null) {
            dayEntity.isSelected = false;
            dayEntity.isSelectedEnd = false;
            dayEntity.isSelectedStart = false;
            endSelectedDay = dayEntity;
        }
    }

    public void setStartSelectedDay(DayEntity dayEntity) {
        if (dayEntity != null) {
            dayEntity.isSelected = true;
            dayEntity.isSelectedStart = true;
            dayEntity.isSelectedEnd = false;
            startSelectedDay = dayEntity;
        }
    }

    public void setEndSelectedDay(DayEntity dayEntity) {
        if (dayEntity != null) {
            dayEntity.isSelected = true;
            dayEntity.isSelectedStart = false;
            dayEntity.isSelectedEnd = true;
            endSelectedDay = dayEntity;
        }
    }


    /**
     * 设置成未选中状态
     *
     * @param dayItemEntities
     */
    private void resetDaySelectedStatus(List<DayEntity> dayItemEntities) {
        for (int i = 0; i < dayItemEntities.size(); i++) {
            DayEntity dayEntity = dayItemEntities.get(i);
            resetDaySelectedStatus(dayEntity);
        }
    }


    /**
     * 设置回调选中监听
     *
     * @param calendarSelectObserver
     */
    public void setCalendarSelectObserver(CalendarSelectObserver calendarSelectObserver) {
        this.calendarSelectObserver = calendarSelectObserver;
    }

    /**
     * 回调选中监听
     */
    private void notifySelectedDateChange() {
        if (calendarSelectObserver != null) {
            long fromDate = -1;
            long toDate = -1;
            if (startSelectedDay != null) {
                fromDate = startSelectedDay.date;
            }
            if (endSelectedDay != null) {
                toDate = endSelectedDay.date;
            }
            calendarSelectObserver.onCalendarSelectChange(fromDate, toDate, isSelectAll);
        }
    }

    public void setIsSelectAll(boolean isSelectAll) {
        this.isSelectAll = isSelectAll;
        if (isSelectAll) {
            startSelectedDay = null;
            for (int i = 0; i < monthItemEntities.size(); i++) {
                MonthEntity monthEntity = monthItemEntities.get(i);
                if (monthEntity == null || monthEntity.dayItemEntities == null) {
                    continue;
                }
                for (int r = 0; r < monthEntity.dayItemEntities.size(); r++) {
                    DayEntity dayEntity = monthEntity.dayItemEntities.get(r);
                    //第一个前面的不需要选中
                    if (startSelectedDay != null) {
                        setDaySelectedStatus(dayEntity);
                    }
                    //设置头部
                    if (i == 0) {
                        startMonth = monthItemEntities.get(i);
                        if (r - 1 > 0) {
                            DayEntity lastEntity = monthEntity.dayItemEntities.get(r - 1);
                            if (lastEntity != null && dayEntity.isValid && !lastEntity.isValid) {
                                setStartSelectedDay(dayEntity);
                            }
                        }
                    }
                    //设置尾部
                    if (i == monthItemEntities.size() - 1) {
                        endMonth = monthEntity;
                        if (r + 1 < monthEntity.dayItemEntities.size()) {
                            //有下一个但下一个是无效的，那么它就是尾巴了
                            DayEntity nextEntity = monthEntity.dayItemEntities.get(r + 1);
                            if (nextEntity != null && dayEntity.isValid && !nextEntity.isValid) {
                                setEndSelectedDay(dayEntity);
                                //最后一个之后的不用选中
                                break;
                            }
                        } else if (dayEntity.isValid) {
                            //最后一个且有效，那么它就是尾巴了
                            setEndSelectedDay(dayEntity);
                            break;
                        }
                    }

                }
            }
        } else {
            resetSelected();
        }
        notifyDataSetChanged();
        notifySelectedDateChange();
    }
}