package com.toybox.wjr.calendarlistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2018/6/22 0022.
 */

public class CalendarDayView extends android.support.v7.widget.AppCompatTextView {
    public CalendarDayView(Context context) {
        super(context);
        init();
    }

    public CalendarDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Paint paint = new Paint();
    float roundRadius = 0;
    RectF drawRect = new RectF();
    Path startSelectedPath;
    Path endSelectedPath;

    private void init() {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.dark_sky_blue));

        roundRadius = getResources().getDimension(R.dimen.selected_round_radius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        drawRect.right = getMeasuredWidth();
        drawRect.bottom = getMeasuredHeight();
        if (startSelectedPath == null || endSelectedPath == null) {
            startSelectedPath = new Path();
            endSelectedPath = new Path();

            float radius = Math.min(getMeasuredHeight(), getMeasuredWidth()) / 2.f;
            float left = (getMeasuredWidth() - (radius * 2)) / 2.f;
            float top = (getMeasuredHeight() - (radius * 2)) / 2.f;
            float right = left + 2 * radius;
            float bottom = top + 2 * radius;
            RectF rectF = new RectF(left,top,right,bottom);
            startSelectedPath.addArc(rectF,90,180);
            startSelectedPath.lineTo(getMeasuredWidth(),0);
            startSelectedPath.lineTo(getMeasuredWidth(),getMeasuredHeight());
            startSelectedPath.close();

            endSelectedPath.addArc(rectF,90,-180);
            endSelectedPath.lineTo(0,0);
            endSelectedPath.lineTo(0,getMeasuredHeight());
            endSelectedPath.close();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isSelectedEnd && isSelectedStart) {
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, Math.min(getMeasuredHeight(), getMeasuredWidth()) / 2.0f, paint);
        } else if (isSelectedStart) {
            canvas.drawPath(startSelectedPath, paint);
        } else if (isSelectedEnd) {
            canvas.drawPath(endSelectedPath,paint);
        } else if (isSelected) {
            canvas.drawRect(drawRect, paint);
        }
        super.onDraw(canvas);
    }

    boolean isValid = true;
    boolean isSelected = false;
    boolean isSelectedEnd = false;
    boolean isSelectedStart = false;

    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setSelectedEnd(boolean selectedEnd) {
        isSelectedEnd = selectedEnd;
    }

    public void setSelectedStart(boolean selectedStart) {
        isSelectedStart = selectedStart;
    }

    public void notifyValid() {
        if (isValid) {
            setTextColor(getResources().getColor(R.color.colorCalendarTextValid));
        } else {
            setTextColor(getResources().getColor(R.color.colorCalendarTextInvalid));
        }
    }

    public void notifyIsSelected() {
        if (isSelected) {
            setTextColor(getResources().getColor(R.color.colorCalendarTextSelected));
        } else {
            notifyValid();
        }
    }


}
