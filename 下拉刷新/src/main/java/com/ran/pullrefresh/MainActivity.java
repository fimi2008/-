package com.ran.pullrefresh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ran.pullrefresh.view.RefreshListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private RefreshListView refreshListView;
    private ArrayList<String> list = new ArrayList<String>();
    private MyAdapter adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            refreshListView.completeRefresh();
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        refreshListView = (RefreshListView) findViewById(R.id.refreshListView);

    }

    private void initData() {
        for (int i = 0 ; i < 15; i++){
            list.add("listview原来的数据-"+ i);
        }
        /*View headerView = View.inflate(this, R.layout.layout_header, null);
        refreshListView.addHeaderView(headerView);*/
        adapter = new MyAdapter();
        refreshListView.setAdapter(adapter);
        refreshListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                requestServer(false);
            }

            @Override
            public void onLoadingMore() {
                requestServer(true);
            }
        });
    }

    /**
     * 向服务器请求数据
     */
    private void requestServer(final boolean isLoadingMore) {
        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(1000);
                if (isLoadingMore){
                    list.add("加载的数据 -1!");
                    handler.sendEmptyMessage(0);
                }else{
                    list.add(0, "刷新后获得的数据!");
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(MainActivity.this);
            textView.setPadding(20, 20, 20 ,20);
            textView.setTextSize(16);
            textView.setText(list.get(position));
            return textView;
        }
    }
}
