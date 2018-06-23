package com.toybox.wjr.calendarlistview;

import com.toybox.wjr.calendarlistview.entity.DayEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/6/21 0021.
 */

public interface CalendarItemEntityBuilder {
    /**
     * 获取 timeMilli 为最后一行的该月数据
     *
     * @param timeMilli
     * @return
     */
    List<DayEntity> buildMonthDayEntities(long timeMilli);

    /**
     * 获取从frommilli 开始 monthCount 个月得完整数据
     * @param fromMilli
     * @param monthCount
     * @return
     */
    List<List<DayEntity>> buildRangEntities(long fromMilli, int monthCount);
}
