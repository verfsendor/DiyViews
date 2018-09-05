package com.diy.views.diyviews.view;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.diy.views.diyviews.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xuzhendong on 2018/9/5.
 */

public class DatePicker extends View {
    TextPaint yearPaint;
    TextPaint weekPaint;
    TextPaint dayPaint;
    Paint defaultPaint;
    ArrayList<Date> dates;//从0-20 存储最近三周的日期
    String[] week = {"一","二","三","四","五","六","日"};
    Date today; //当前日期
    Calendar monday; //当前显示的周一
    Paint bgPaint;
    float startX; //绘制的启始位置
    float mDistanceX;
    float valueWidth;//每一天的宽度
    int minScrollDistance = 200;//最小滑动距离，大于此距离view会翻页，小于此距离，滑回
    int textColor = Color.parseColor("#ffffff");
    int bgColor = Color.parseColor("#FF0000");
    int clickColor = Color.parseColor("#ffffff");
    int clickTxtColor = Color.parseColor("#ff0000");
    int clickRadius = 50;
    float yearHeight;
    float weekHeight;
    float dayHeight;
    float dayWeekPadding;//day和week之间的间距
    float defaultHeight = 350;
    float animationValue = 1;
    Date clickDate;
    boolean detectorEvent = true;
    GestureDetector detector;
    public DatePicker(Context context) {
        super(context);
        init(null);
    }

    public DatePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DatePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void getAttr(AttributeSet attrs) {
        if(attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ClockView);
//            mbgColor = typedArray.getColor(R.styleable.ClockView_bgColor, mbgColor);
            typedArray.recycle();
        }
    }

    private void initPaint(){
        yearPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        yearPaint.setColor(textColor);
        yearPaint.setAntiAlias(true);
        yearPaint.setStrokeWidth(2);
        yearPaint.setTextSize(50);
        yearPaint.setStyle(Paint.Style.FILL);

        weekPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        weekPaint.setColor(textColor);
        weekPaint.setAntiAlias(true);
        weekPaint.setStrokeWidth(2);
        weekPaint.setTextSize(40);
        weekPaint.setStyle(Paint.Style.FILL);

        dayPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        dayPaint.setColor(textColor);
        dayPaint.setAntiAlias(true);
        dayPaint.setStrokeWidth(2);
        dayPaint.setTextSize(40);
        dayPaint.setStyle(Paint.Style.FILL);

        defaultPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        defaultPaint.setColor(textColor);
        defaultPaint.setAntiAlias(true);
        defaultPaint.setStrokeWidth(2);
        defaultPaint.setTextSize(40);
        defaultPaint.setStyle(Paint.Style.FILL);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(textColor);
        bgPaint.setAntiAlias(true);
        bgPaint.setStrokeWidth(5);
        bgPaint.setStyle(Paint.Style.STROKE);
    }

    public void init(AttributeSet attributeSet){
        dates = new ArrayList<>();
        monday = Calendar.getInstance();
        today = new Date();
        monday.setTime(today);
        int x = monday.get(Calendar.DAY_OF_WEEK);
        monday.add(Calendar.DAY_OF_MONTH, -x + 2);//转移日历到当前的周一
        getDate();
        getAttr(attributeSet);
        initPaint();
        detector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if(e.getY() >= yearHeight + weekHeight){
                   int position = (int) ((e.getX() - startX)/valueWidth);
                   clickDate = dates.get(position);
                }
                invalidate();
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if(detectorEvent) {
                    mDistanceX += distanceX;
                    Log.v("verf", "onscroll " + distanceX);
                    startX = -getMeasuredWidth() - mDistanceX;
                    invalidate();
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                showNextAnimation();
                return false;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec)
;        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if(heightMode == MeasureSpec.EXACTLY && height < defaultHeight){
            height = (int) defaultHeight;
        }
        setMeasuredDimension(width,height);
        yearHeight = getMeasuredHeight()/3;
        weekHeight = getMeasuredHeight()*3/12;
        dayHeight = getMeasuredHeight()*5/12;
        startX = - getMeasuredWidth();
        valueWidth = getMeasuredWidth()/7;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(bgColor);
        float weektxtWidth = weekPaint.measureText("一");
        float daytxtheight = dayPaint.measureText("0");
        for(int i = 0; i < 7; i++){
            canvas.drawText(week[i],-getMeasuredWidth() + 7 * valueWidth + valueWidth * i + valueWidth/2 - weektxtWidth/2, yearHeight + weekHeight/2 + weektxtWidth/2,weekPaint);
        }
        for(int i = 0; i < dates.size(); i ++){
            float daytxtWidth = 0;
            if(dates.get(i).getDate() > 9){
                daytxtWidth = dayPaint.measureText("10");
            }else {
                daytxtWidth = dayPaint.measureText("0");
            }
            canvas.drawText("" + dates.get(i).getDate(),startX + valueWidth * i + valueWidth/2 - daytxtWidth/2, yearHeight + weekHeight + dayWeekPadding + dayHeight/2 + daytxtheight/2,dayPaint);
            if(clickDate != null && dates.get(i).getTime() == clickDate.getTime()){
                defaultPaint.setColor(clickColor);
                canvas.drawCircle(startX + valueWidth * i + valueWidth/2, yearHeight + weekHeight + dayWeekPadding + dayHeight/2 ,clickRadius,defaultPaint);
                defaultPaint.setColor(clickTxtColor);
                canvas.drawText("" + dates.get(i).getDate(),startX + valueWidth * i + valueWidth/2 - daytxtWidth/2, yearHeight + weekHeight + dayWeekPadding + dayHeight/2 + daytxtheight/2,defaultPaint);
            }
        }
        float yeartxtWidth = yearPaint.measureText("2018年");
        float monthtxtWidth = yearPaint.measureText("00");
        float yeartxtheight = yearPaint.measureText("0");
        for(float i = startX; i <= startX + dates.size() * valueWidth - 100; i += getMeasuredWidth()){
            Date date = dates.get((int) ((i - startX)/getMeasuredWidth() * 7));
            yearPaint.setAlpha(getAlpha(i));
            canvas.drawText(date.getMonth() + "月",i,yearHeight/2 + yeartxtheight/2, yearPaint);
            yearPaint.setAlpha(getAlpha(i + getMeasuredWidth() - yeartxtWidth));
            canvas.drawText("" + (1900 +date.getYear()) + "年",i + getMeasuredWidth() - yeartxtWidth,yearHeight/2 + yeartxtheight/2, yearPaint);
            yearPaint.setAlpha(getAlpha(i + getMeasuredWidth()/2));
            canvas.drawText("" + (1900 +date.getYear()) + "年",i + getMeasuredWidth()/2 - yeartxtWidth,yearHeight/2 + yeartxtheight/2, yearPaint);
            canvas.drawText(date.getMonth() + "月",i + getMeasuredWidth()/2,yearHeight/2 + yeartxtheight/2, yearPaint);
        }
        SimpleDateFormat format = new SimpleDateFormat("MM");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        //滑动抬起时，判断滑动距离是否过半，如果过半则下滑下一页，否则滑回
        if(event.getAction() == MotionEvent.ACTION_UP && detectorEvent){
            if(Math.abs(mDistanceX) < getMeasuredWidth()/2){
                showBackAnimation();
            }else {
                showNextAnimation();
            }
        }
        return true;
    }

    public void showBackAnimation(){
        detectorEvent = false;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1)
                .setDuration(500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animationValue = (float) animation.getAnimatedValue();
                startX = - getMeasuredWidth() - (1 - animationValue) * mDistanceX;
                invalidate();
                if(animationValue == 1){
                    detectorEvent = true;
                    mDistanceX = 0;
                }
            }
        });
        valueAnimator.start();
    }

    public void showNextAnimation(){
        detectorEvent = false;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1)
                .setDuration(500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animationValue = (float) animation.getAnimatedValue();
                int symbol = mDistanceX > 0 ? 1 : -1;
                startX = - getMeasuredWidth() - mDistanceX - symbol * animationValue * (getMeasuredWidth() - Math.abs(mDistanceX));
                invalidate();
                if(animationValue == 1){
                    detectorEvent = true;
                    mDistanceX = 0;
                    startX = -getMeasuredWidth();
                    monday.add(Calendar.DAY_OF_YEAR,symbol * 7);
                    getDate();
                    invalidate();
                }
            }
        });
        valueAnimator.start();
    }

    public void getDate(){
        try {
            dates.clear();
            Calendar calendar = monday;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            for(int i = 0; i < 21; i ++){
                Date date = calendar.getTime();
                dates.add(date);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            calendar.add(Calendar.DAY_OF_YEAR, -14);
            monday = calendar;
        }catch (Exception e){
            Log.v("verf","getDate " + e.getMessage());
        }
    }

    /**
     * 获取对应X位置的画笔透明度
     * @param x
     * @return
     */
    public int getAlpha(float x){
        float alpha = 220 - 100 * (Math.abs(getMeasuredWidth()/2 - x)/getMeasuredWidth() * 2);
        return (int) alpha;
    }
}
