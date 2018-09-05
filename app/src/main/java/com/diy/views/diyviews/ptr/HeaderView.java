package com.diy.views.diyviews.ptr;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xuzhendong on 2018/9/3.
 */

public class HeaderView extends View {
    public static final int HEADER_STATUS_READER = 1;//初始状态
    public static final int HEADER_STATUS_DRAG = 2;//下拉刷新状态
    public static final int HEADER_STATUS_REFLEASE = 3;//释放状态
    public static final int HEADER_STATUS_REFRESHING = 4;//刷新中状态
    public static final int HEADER_STATUS_FINSH = 5;//刷新完成状态
    public int status;
    public HeaderView(Context context) {
        super(context);
        init();
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){}

    public void setStatus(int status){
        this.status = status;
    }

}
