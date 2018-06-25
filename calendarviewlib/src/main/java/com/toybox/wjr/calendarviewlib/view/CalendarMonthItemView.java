package com.toybox.wjr.calendarviewlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.toybox.wjr.calendarviewlib.OnDayClickListener;
import com.toybox.wjr.calendarviewlib.R;
import com.toybox.wjr.calendarviewlib.entity.DayEntity;
import com.toybox.wjr.calendarviewlib.entity.MonthEntity;

/**
 * Created by Administrator on 2018/6/22 0022.
 */

public class CalendarMonthItemView extends View {

    public CalendarMonthItemView(Context context) {
        super(context);
        init();
    }

    public CalendarMonthItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarMonthItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CalendarMonthItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    int selectedRadius;
    int divideHeight;
    int spanCount = 7;
    Paint textPaint;
    Paint bgPaint;
    int selectedTextColor;
    int validTextColor;
    int invalidTextColor;
    int selectedBgColor;

    Path startSelectedPath;
    Path endSelectedPath;

    float itemWidth = -1;
    float itemHeight = -1;

    GestureDetector gestureDetector;
    MonthEntity monthEntity;

    private void init() {
        selectedRadius = (int) getResources().getDimension(R.dimen.selected_round_radius);
        divideHeight = (int) getResources().getDimension(R.dimen.calendar_divide);
        selectedTextColor = getResources().getColor(R.color.colorCalendarTextSelected);
        validTextColor = getResources().getColor(R.color.colorCalendarTextValid);
        invalidTextColor = getResources().getColor(R.color.colorCalendarTextInvalid);
        selectedBgColor = getResources().getColor(R.color.colorCalendarBgSelected);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(validTextColor);
        textPaint.setTextSize(getResources().getDimension(R.dimen.calendar_day_text_size));

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(getResources().getColor(R.color.dark_sky_blue));

        startSelectedPath = new Path();
        endSelectedPath = new Path();

        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                disposeTouchEvent(e);
                return true;
            }
        });
    }


    public void onBind(MonthEntity monthEntity) {
        this.monthEntity = monthEntity;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthExpect = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightExpect = MeasureSpec.getSize(heightMeasureSpec);

        int measureWidth = 0;
        int measureHeight = 0;
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
                measureWidth = selectedRadius * 7 * 2;
                break;
            case MeasureSpec.AT_MOST:
                measureWidth = Math.min(widthExpect, selectedRadius * 7 * 2);
                break;
            case MeasureSpec.EXACTLY:
                measureWidth = widthExpect;
                break;
        }

        if (monthEntity != null) {
            double itemHeightCount = Math.ceil(monthEntity.dayItemEntities.size() * 1.0d / spanCount);
            measureHeight = (int) (selectedRadius * 2 * itemHeightCount + divideHeight * (itemHeightCount - 1));
        }
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.AT_MOST:
                measureHeight = Math.min(heightExpect, measureHeight);
                break;
            case MeasureSpec.EXACTLY:
                measureHeight = heightExpect;
                break;
        }
        setMeasuredDimension(measureWidth, measureHeight);
        if (itemWidth <= 0) {
            int validWidth = 0;
            if (getMeasuredWidth() % spanCount != 0) {
                int space = getMeasuredWidth() % (spanCount * 2);
                validWidth = getMeasuredWidth() - space;
                setPadding(space / 2, getPaddingTop(), space / 2, getPaddingBottom());
            } else {
                validWidth = getMeasuredWidth();
            }
            itemWidth = validWidth / spanCount;
        }
        if (itemHeight <= 0) {
            itemHeight = selectedRadius * 2;
        }
    }

    RectF itemRect = new RectF();
    RectF circleRect = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (monthEntity == null || monthEntity.dayItemEntities == null) {
            return;
        }

        for (int i = 0; i < monthEntity.dayItemEntities.size(); i++) {
            DayEntity dayEntity = monthEntity.dayItemEntities.get(i);
            if (dayEntity == null) {
                continue;
            }
            itemRect.top = (i / spanCount) * (itemHeight + divideHeight) + getPaddingTop();
            itemRect.bottom = itemRect.top + itemHeight;
            itemRect.left = (i % spanCount) * itemWidth + getPaddingLeft();
            itemRect.right = itemRect.left + itemWidth;

            circleRect.left = itemRect.left + (itemRect.width() - (selectedRadius * 2)) / 2.f;
            circleRect.top = itemRect.top + (itemRect.height() - (selectedRadius * 2)) / 2.f;
            circleRect.right = circleRect.left + 2 * selectedRadius;
            circleRect.bottom = circleRect.top + 2 * selectedRadius;

            if (dayEntity.isSelectedEnd && dayEntity.isSelectedStart) {
                canvas.drawCircle(circleRect.left + circleRect.width() / 2, circleRect.top + circleRect.height() / 2, selectedRadius, bgPaint);
            } else if (dayEntity.isSelectedStart) {
                startSelectedPath.reset();
                startSelectedPath.addArc(circleRect, 90, 180);
                startSelectedPath.lineTo(itemRect.right, itemRect.top);
                startSelectedPath.lineTo(itemRect.right, itemRect.bottom);
                startSelectedPath.close();
                canvas.drawPath(startSelectedPath, bgPaint);
            } else if (dayEntity.isSelectedEnd) {
                endSelectedPath.reset();
                endSelectedPath.addArc(circleRect, 90, -180);
                endSelectedPath.lineTo(itemRect.left, itemRect.top);
                endSelectedPath.lineTo(itemRect.left, itemRect.bottom);
                endSelectedPath.close();
                canvas.drawPath(endSelectedPath, bgPaint);
            } else if (dayEntity.isSelected) {
                canvas.drawRect(itemRect, bgPaint);
            }

            if (dayEntity.isSelected) {
                textPaint.setColor(selectedTextColor);
            } else if (dayEntity.isValid) {
                textPaint.setColor(validTextColor);
            } else {
                textPaint.setColor(invalidTextColor);
            }
            float textLength = textPaint.measureText(dayEntity.displayValue);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float tx = itemRect.left + (itemRect.width() - textLength) / 2.f;
            float ty = itemRect.top + (itemRect.height() + fontMetrics.bottom - fontMetrics.top) / 2.f - fontMetrics.bottom;
            canvas.drawText(dayEntity.displayValue, tx, ty, textPaint);
//            canvas.drawLine(itemRect.left,itemRect.top + itemRect.height() /2,itemRect.right,itemRect.top + itemRect.height() /2,textPaint);
        }
    }

    public void disposeTouchEvent(MotionEvent e) {
        int selectPosition = (int) (e.getX() / itemWidth) + (int) (e.getY() / (itemHeight + divideHeight)) * spanCount;
        if (monthEntity != null && monthEntity.dayItemEntities != null && monthEntity.dayItemEntities.size() > selectPosition) {
            DayEntity dayEntity = monthEntity.dayItemEntities.get(selectPosition);
            if (dayEntity.isValid && onDayClickListener != null) {
                onDayClickListener.onDayClick(monthEntity, dayEntity);
            }
//            Toast.makeText(CalendarMonthView.this.getContext(), monthEntity.dayItemEntities.get(selectPosition).displayValue, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private OnDayClickListener onDayClickListener = null;

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
    }
}
