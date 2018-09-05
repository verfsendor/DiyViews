package com.diy.views.diyviews.view;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.diy.views.diyviews.R;
import java.util.Calendar;
/**
 * 钟表控件
 * Created by xuzhendong on 2018/8/27.
 */

public class ClockView extends View {
    private Paint mClockPaint;//表盘画笔
    private Paint mHourPaint;//时针画笔
    private Paint mMinutePaint;//分针画笔
    private Paint mSecondPaint;//秒针画笔
    private Paint mbgPaint;//表盘背景

    private int WRAP_WIDTH = 200;//默认宽度
    private int WRAP_HEIGHT = 200;//默认高度
    private int clockPadding = 20;//默认表盘到view边缘间距
    private Context mContext;
    private int mClockColor = Color.WHITE;//默认表盘颜色
    private int mHourColor = Color.GRAY;//默认时针颜色
    private int mMinuteColor = Color.WHITE;//默认分针颜色
    private int mSencondColor = Color.RED;//默认秒针颜色
    private int mbgColor = Color.BLACK;//默认秒针颜色
    private int mBgColor2 = Color.parseColor("#000000");//默认秒针颜色

    private int hour;//小时
    private int minute;//分钟
    private int second;//秒
    private boolean handleMsg;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(handleMsg) {
                Calendar cal = Calendar.getInstance();
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);
                second = cal.get(Calendar.SECOND);
                invalidate();
                handler.sendMessageDelayed(new Message(), 1000);
            }
        }
    };

    public ClockView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void getAttr(AttributeSet attrs) {
        if(attrs != null) {
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ClockView);
//            progressStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBarView_progressStrokeWidth, defaultStrokeWidth);
            mMinuteColor = typedArray.getColor(R.styleable.ClockView_minuteColor, mMinuteColor);
            mHourColor = typedArray.getColor(R.styleable.ClockView_hourColor, mHourColor);
            mClockColor = typedArray.getColor(R.styleable.ClockView_clockColor, mClockColor);
            mSencondColor = typedArray.getColor(R.styleable.ClockView_sencondColor, mSencondColor);
            mbgColor = typedArray.getColor(R.styleable.ClockView_bgColor, mbgColor);

//            isDrawCenterProgressText = typedArray.getBoolean(R.styleable.CircleProgressBarView_isDrawCenterProgressText, false);
            typedArray.recycle();
        }
    }

    private void init(AttributeSet attrs) {
        getAttr(attrs);
        initPaint();
        handleMsg = true;
        handler.sendMessage(new Message());
    }

    private void initPaint(){
        mClockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mClockPaint.setColor(mClockColor);
        mClockPaint.setAntiAlias(true);
        mClockPaint.setStrokeWidth(5);
        mClockPaint.setStyle(Paint.Style.STROKE);

        mHourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourPaint.setColor(mHourColor);
        mHourPaint.setAntiAlias(true);
        mHourPaint.setStrokeWidth(10);
        mHourPaint.setStyle(Paint.Style.FILL);

        mMinutePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinutePaint.setColor(mMinuteColor);
        mMinutePaint.setAntiAlias(true);
        mMinutePaint.setStrokeWidth(7);
        mMinutePaint.setStyle(Paint.Style.FILL);

        mSecondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondPaint.setColor(mSencondColor);
        mSecondPaint.setAntiAlias(true);
        mSecondPaint.setStrokeWidth(4);
        mSecondPaint.setStyle(Paint.Style.FILL);

        mbgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mbgPaint.setColor(mbgColor);
        mbgPaint.setAntiAlias(true);
        mbgPaint.setStrokeWidth(5);
        mbgPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(WRAP_WIDTH, WRAP_HEIGHT);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(WRAP_WIDTH, height);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, WRAP_HEIGHT);
        }else {
            setMeasuredDimension(width,height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画表盘
        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredWidth()/2, getMeasuredWidth()/2 - clockPadding,mbgPaint);
        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredWidth()/2, getMeasuredWidth()/2 - clockPadding,mClockPaint);
        mClockPaint.setStrokeWidth(10);
        //画表心1
        mClockPaint.setStyle(Paint.Style.FILL);
        mClockPaint.setColor(mBgColor2);
        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2,75f, mClockPaint);
        mClockPaint.setStyle(Paint.Style.STROKE);
        mClockPaint.setColor(mClockColor);
        mClockPaint.setStrokeWidth(5);
        //画钟表格子
        Path path = new Path();
        String x = "";
        for(int i = 0; i < 60; i++){
            if(i % 5 == 0) {
                mClockPaint.setTextSize(25);
                canvas.drawLine(clockPadding, getMeasuredHeight() / 2, clockPadding + 22, getMeasuredHeight() / 2, mClockPaint);
                path.addArc(new RectF(clockPadding,clockPadding, getMeasuredWidth() - clockPadding, getMeasuredHeight() - clockPadding),0,30);
                x = "" + ((i + 15)%60)/5;
                if("0".equals(x)){
                    x = "12";
                }
                mClockPaint.setStrokeWidth(1f);
                canvas.drawTextOnPath(x,path,0,60,mClockPaint);
                mClockPaint.setStrokeWidth(5f);
            }else {
                canvas.drawLine(clockPadding, getMeasuredHeight() / 2, clockPadding + 10, getMeasuredHeight() / 2, mClockPaint);
            }
            canvas.rotate(6,getMeasuredWidth()/2, getMeasuredHeight()/2);
        }
        //
        //画表针
        canvas.rotate((hour + 3) * 30,getMeasuredWidth()/2, getMeasuredHeight()/2);
        canvas.drawLine(getMeasuredWidth()/2, getMeasuredHeight()/2,getMeasuredWidth()/2 - 150, getMeasuredHeight()/2,mHourPaint);
        canvas.drawCircle(getMeasuredWidth()/2 - 150, getMeasuredHeight()/2,4,mHourPaint);
        canvas.rotate(-(hour + 3) * 30,getMeasuredWidth()/2, getMeasuredHeight()/2);

        canvas.rotate((minute + 15) * 6,getMeasuredWidth()/2, getMeasuredHeight()/2);
        canvas.drawLine(getMeasuredWidth()/2, getMeasuredHeight()/2,getMeasuredWidth()/2 - 200, getMeasuredHeight()/2,mMinutePaint);
        canvas.drawCircle(getMeasuredWidth()/2 - 200, getMeasuredHeight()/2,4,mMinutePaint);
        canvas.rotate(-(minute + 15) * 30,getMeasuredWidth()/2, getMeasuredHeight()/2);

        canvas.rotate((second + 15) * 6,getMeasuredWidth()/2, getMeasuredHeight()/2);
        canvas.drawLine(getMeasuredWidth()/2, getMeasuredHeight()/2,getMeasuredWidth()/2 - 200, getMeasuredHeight()/2,mSecondPaint);
        canvas.drawCircle(getMeasuredWidth()/2 - 200, getMeasuredHeight()/2,4,mSecondPaint);
        canvas.rotate(-(second + 15) * 30,getMeasuredWidth()/2, getMeasuredHeight()/2);
        //画表心2
        mClockPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2,15f, mClockPaint);
        mClockPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handleMsg = false;
    }
}
