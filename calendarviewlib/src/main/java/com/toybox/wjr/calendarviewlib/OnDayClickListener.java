package com.toybox.wjr.calendarviewlib;

import com.toybox.wjr.calendarviewlib.entity.DayEntity;
import com.toybox.wjr.calendarviewlib.entity.MonthEntity;

/**
 * Created by Administrator on 2018/6/21 0021.
 */

public interface OnDayClickListener {
    void onDayClick(MonthEntity monthEntity, DayEntity dayEntity);
}
