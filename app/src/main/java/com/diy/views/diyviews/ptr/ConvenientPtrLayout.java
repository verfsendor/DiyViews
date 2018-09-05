package com.diy.views.diyviews.ptr;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * Created by xuzhendong on 2018/9/3.
 */

public class ConvenientPtrLayout extends ViewGroup {
    HeaderView headerView;
    public ConvenientPtrLayout(Context context) {
        super(context);
        init();
    }

    public ConvenientPtrLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConvenientPtrLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setHeaderView(HeaderView view){
        this.headerView = view;
    }

    public void init(){
        //添加headerView
        if(headerView == null){
            headerView = new MyHeaderView(getContext());
        }
        addView(headerView,0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getChildCount() == 2){
            final MarginLayoutParams lp = (MarginLayoutParams) getChildAt(1).getLayoutParams();
            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
            final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    getPaddingTop() + getPaddingBottom() + lp.topMargin, lp.height);

            getChildAt(1).measure(childWidthMeasureSpec, childHeightMeasureSpec);

            final MarginLayoutParams lp1 = (MarginLayoutParams) getChildAt(0).getLayoutParams();
            final int childWidthMeasureSpec1 = getChildMeasureSpec(widthMeasureSpec,
                    getPaddingLeft() + getPaddingRight() + lp1.leftMargin + lp1.rightMargin, lp1.width);
            final int childHeightMeasureSpec1 = getChildMeasureSpec(heightMeasureSpec,
                    getPaddingTop() + getPaddingBottom() + lp1.topMargin, lp1.height);
            getChildAt(0).measure(childWidthMeasureSpec1, childHeightMeasureSpec1);
        }
        setMeasuredDimension(1000, 1000);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() == 0 || getChildCount() > 2){
            removeAllViews();
            TextView errorView = new TextView(getContext());
            errorView.setWidth(1000);
            errorView.setHeight(1000);
            errorView.setTextSize(25);
            errorView.setTextColor(Color.parseColor("#ff6600"));
            errorView.setText("对不起，layout最多只能含有一个子view");
            errorView.setBackgroundColor(Color.BLUE);
            errorView.setGravity(Gravity.CENTER);
            addView(errorView);
        }else if(getChildCount() == 1){
            View view = getChildAt(0);
            removeAllViews();
            TextView errorView = new TextView(getContext());
            errorView.setWidth(1000);
            errorView.setHeight(1000);
            errorView.setTextSize(25);
            errorView.setTextColor(Color.parseColor("#ff6600"));
            if(view instanceof HeaderView){
                errorView.setText("请添加content view");
            }else {
                errorView.setText("对不起，您还未设置Headerview");
            }
            errorView.setBackgroundColor(Color.BLUE);
            errorView.setGravity(Gravity.CENTER);
            addView(errorView);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(getChildCount() == 2){
            View view1 = getChildAt(0);
            View view2 = getChildAt(1);
            getChildAt(0).layout(0, -50,300, 0);
            getChildAt(1).layout(0,0,500,500);
        }
    }
}
