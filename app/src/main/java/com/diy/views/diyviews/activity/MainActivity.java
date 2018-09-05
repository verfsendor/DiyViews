package com.diy.views.diyviews.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.diy.views.diyviews.R;
import com.diy.views.diyviews.view.WaterView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("verf","mainactivity oncreate");
        setContentView(R.layout.activity_main);
        findViewById(R.id.a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ClockActivity.class));
            }
        });

        findViewById(R.id.b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WaterViewActivity.class));
            }
        });

        findViewById(R.id.c).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PtrLayoutActivity.class));
            }
        });
        findViewById(R.id.d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DatePickerActivity.class));
            }
        });
        getDate2();
    }

    public void getDate(){
        try {
            ArrayList<Date> calendars = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, 10);
            Date m2 = c.getTime();
            calendars.add(m2);
            Log.v("verf","getDate 0  " + format.format(m2));
            int x = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DAY_OF_MONTH, -x + 2);
            Date m3 = c.getTime();
            calendars.add(m3);
            Log.v("verf","getDate 1  " + format.format(m3));
            for(int i = 0; i < calendars.size(); i ++){
                Date m = calendars.get(i);
                Log.v("verf","getDate ---  " + format.format(m));
            }
        }catch (Exception e){
            Log.v("verf","getDate " + e.getMessage());
        }

    }

    public void getDate2(){
        try {
            ArrayList<Date> dates = new ArrayList<>();
            Calendar monday = Calendar.getInstance();
            Date today = new Date();
            monday.setTime(today);
            int x = monday.get(Calendar.DAY_OF_WEEK);
            monday.add(Calendar.DAY_OF_MONTH, -x + 2);//转移日历到当前的周一
            dates.clear();
            Calendar calendar = monday;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            for(int i = 0; i < 21; i ++){
                Date date = calendar.getTime();
                dates.add(date);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

        }catch (Exception e){
            Log.v("verf","getDate " + e.getMessage());
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
