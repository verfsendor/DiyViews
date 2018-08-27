package com.diy.views.diyviews.view;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by xuzhendong on 2018/8/23.
 */

public class WaterView extends View {
    Paint paint;
    private AnimatorSet animatorSet;
    private float circleX = 200f;
    private float circleY = 200f;
    private float circleRadius = 50f;
    private float secondAngel = 30;

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
        Log.v("verf","water view init()");
        initPaint();
        animatorSet = new AnimatorSet();
//        openAnimation();
    }

    public void initPaint(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20);
//        paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heigthMode = MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }


    /**
     * 开启动画
     */
    public void openAnimation() {
        if (!animatorSet.isRunning()) {
            animatorSet.play(floatAnimation());
            animatorSet.start();
        }
    }

    /**
     * 关闭动画
     */
    public void stopAnimation() {
        if (animatorSet.isRunning()) {
            animatorSet.cancel();
        }
    }

    /**
     * 上下左右浮动效果动画
     *
     * @return 返回ValueAnimator
     */
    private ValueAnimator floatAnimation() {

        final float[] pos = new float[2];
        final float[] tan = new float[2];

        ValueAnimator floatAnimator = ValueAnimator.ofFloat(0, 3);
        floatAnimator.setDuration(18000);
        floatAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        floatAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float t = (float) valueAnimator.getAnimatedValue();
                circleX = circleX + t;
                circleY = circleY + t;
                circleRadius = circleRadius + 5 * t;
                secondAngel = t;
                invalidate();
            }
        });
        return floatAnimator;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        canvas.drawCircle(0,0,400,paint);          // 绘制两个圆形
        canvas.drawCircle(0,0,380,paint);

        for (int i=0; i<=360; i+=10){               // 绘制圆形之间的连接线
            canvas.drawLine(0,380,0,400,paint);
            canvas.rotate(10);
        }
    }

}
