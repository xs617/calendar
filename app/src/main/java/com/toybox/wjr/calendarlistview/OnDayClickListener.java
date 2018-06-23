package com.toybox.wjr.calendarlistview;

import android.view.View;

import com.toybox.wjr.calendarlistview.entity.DayEntity;
import com.toybox.wjr.calendarlistview.entity.MonthEntity;
import com.toybox.wjr.calendarlistview.holder.MonthViewHolder;

/**
 * Created by Administrator on 2018/6/21 0021.
 */

public interface OnDayClickListener {
    void onDayClick(MonthEntity monthEntity, DayEntity dayEntity);
}
