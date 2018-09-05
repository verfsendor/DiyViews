package com.diy.views.diyviews.activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.diy.views.diyviews.R;
import java.util.ArrayList;
/**
 * Created by xuzhendong on 2018/9/3.
 */

public class PtrLayoutActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> datas;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptr_layout);
//        listView = findViewById(R.id.listview);
        datas = new ArrayList<>();
        for(int i = 0; i < 15; i ++){
            datas.add("" + i);
        }
//        listView.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ConverHolder converHolder;
            if(convertView == null){
                convertView = LayoutInflater.from(PtrLayoutActivity.this).inflate(R.layout.item_simple,null);
                converHolder = new ConverHolder();
                converHolder.view = convertView.findViewById(R.id.txt);
                convertView.setTag(converHolder);
            }else {
                converHolder = (ConverHolder) convertView.getTag();
            }
            converHolder.view.setText(datas.get(position));
            return convertView;
        }
    }

    public class ConverHolder {
        public TextView view;
    }
}
