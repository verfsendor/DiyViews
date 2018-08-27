package com.diy.views.diyviews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.math.BigDecimal;

import static java.lang.Math.PI;

/**
 * Created by xuzhendong on 2018/8/23.
 */

public class WaterView extends View {
    private Paint backPaint;//背景色，圆外部分
    private Paint circleBackPaint;//圆上部画笔
    private Paint aboveWavePaint;//上方波浪画笔
    private Paint behindWavePaint;//下方波浪画笔
    private Paint  textPaint;//文字画笔
    private Path abovePath;//上方波浪的path路径
    private Path behindPath;//下方波浪的path路径
    private int backColor = Color.parseColor("#003333");//圆外部颜色
    private int circleBackColor = Color.parseColor("#ffffff");//圆上部颜色
    private int aboveColor = Color.parseColor("#90990000");//上方波浪颜色
    private int behindColor = Color.parseColor("#60990000");//下方波浪颜色
    private int radius = 270;//圆的半径
    private static int START_ANGEL = 0;//波浪三角函数角度初始值
    private int waveV = 15;//波浪移动速度
    private int waveH = 18;//波浪震幅
    private float dValue = 2; //两个波浪的相值差
    private float finishCode = 20; //完成度百分比
    //通过handler定时更新重绘
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
            START_ANGEL += waveV;
            dValue = dValue + 0.02f;
            if(finishCode + 0.2 < 100) {
                finishCode += 0.2;
            }else {
                finishCode = 100;
            }
//            if(dValue % (PI * 2) < 0.1){
//                dValue += 0.2;
//            }
            handler.sendMessageDelayed(new Message(),50);
        }
    };

    public WaterView(Context context) {
        super(context);
        init();
    }

    public WaterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        abovePath = new Path();
        behindPath = new Path();
        initPaint();
    }

    public void initPaint(){
        textPaint = new Paint();
        textPaint.setColor(backColor);
        textPaint.setStrokeWidth(2f);
        textPaint.setTextSize(35);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);


        backPaint = new Paint();
        backPaint.setColor(backColor);
        backPaint.setStrokeWidth(5f);
        backPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        circleBackPaint = new Paint();
        circleBackPaint.setColor(circleBackColor);
        circleBackPaint.setStrokeWidth(5f);
        circleBackPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        aboveWavePaint = new Paint();
        aboveWavePaint.setColor(aboveColor);
        aboveWavePaint.setStrokeWidth(5f);
        aboveWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        behindWavePaint = new Paint();
        behindWavePaint.setColor(behindColor);
        behindWavePaint.setStrokeWidth(5f);
        behindWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        handler.sendMessage(new Message());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //初始化两个path，并且加入初始坐标点，便于形成封闭图形
        abovePath.reset();
        behindPath.reset();
        abovePath.moveTo(getMeasuredWidth()/2 - radius - 100,0);
        behindPath.moveTo(getMeasuredWidth()/2 - radius - 100,0);
        //根据完成度计算y的差值
        float deep = (100 - finishCode) * (2 * radius / 100) + getMeasuredHeight()/2 - radius;
        int startX = getMeasuredWidth()/2 - radius - 20 ;
        abovePath.lineTo(getMeasuredWidth()/2 - radius - 100, (float) (waveH * Math.sin((startX + START_ANGEL) * PI/180)) + deep);
        behindPath.lineTo(getMeasuredWidth()/2 - radius - 100, (float) (waveH * Math.sin((startX + START_ANGEL) * PI/180 + dValue)) + deep);
        //画外部圆
        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, radius, circleBackPaint);
        for(int i = startX; i < getMeasuredWidth() + radius + 20; i = i + 10){
            double y1 = waveH * Math.sin((i + START_ANGEL) * PI/180) + deep;
            double y2 = waveH * Math.sin((i + START_ANGEL)* PI/180 + dValue) + deep;
            abovePath.lineTo(i, (float) y1);
            behindPath.lineTo(i, (float) y2);
            if((i - getMeasuredWidth()/2 < 11 && (i - getMeasuredWidth()/2 > -11))){
                BigDecimal b  =   new  BigDecimal(finishCode);
                float f1   =  b.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                if(f1 == 100){
                    y1 += 50;
                }
                canvas.drawText(f1 + "%", getMeasuredWidth() / 2 - 30, (float)y1,textPaint);
            }

        }
        abovePath.lineTo( getMeasuredWidth() + radius + 100,0);
        behindPath.lineTo( getMeasuredWidth() + radius + 100,0);
        abovePath.close();
        behindPath.close();
        //画两个波浪,并对波浪底部进行填充
        behindPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        canvas.drawPath(behindPath,behindWavePaint);
        abovePath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        canvas.drawPath(abovePath,aboveWavePaint);
        //画一个圆，并对圆外部进行填充，这样只能看到圆内的波浪部分
        Path path = new Path();
        path.addCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, radius, Path.Direction.CCW);
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        canvas.drawPath(path,backPaint);
    }


}
