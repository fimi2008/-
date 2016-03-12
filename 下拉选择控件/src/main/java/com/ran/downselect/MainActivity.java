package com.ran.downselect;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editText;
    private ImageView iv_select;
    private ArrayList<String> datas = new ArrayList<String>();
    private ListView listView;
    private PopupWindow popupWindow;
    private int initListViewHeight = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initListener();
        initData();
    }



    private void initView() {
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        iv_select = (ImageView) findViewById(R.id.iv_select);
    }

    private void initListener() {
        iv_select.setOnClickListener(this);
    }

    private void initData() {
        for (int i = 0; i < 15; i++){
            datas.add("人员"+i);
        }

        initListView();
    }

    private void initListView(){
        listView = new ListView(this);
        listView.setBackgroundColor(Color.WHITE);
        listView.setVerticalScrollBarEnabled(false); // 隐藏listView滚动条

        listView.setAdapter(new MyAdapter());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("list", "post" + position);
                editText.setText(datas.get(position));
                popupWindow.dismiss();
            }
        });
    }

    private void showNumberList(){
        if (null == popupWindow){
            popupWindow = new PopupWindow(listView, editText.getWidth(), initListViewHeight);
        }
        // 要让其中的view获取焦点,必须设置为true
        popupWindow.setFocusable(true);
        // 还必须设置一个背景图片
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击外部消失
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAsDropDown(editText, 0, 2);

    }

    class MyAdapter extends BaseAdapter{

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view = View.inflate(MainActivity.this, R.layout.adapter_list, null);
            TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
            ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);

            tv_number.setText(datas.get(position));
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("main", "position:" + position);
                    datas.remove(position);
                    notifyDataSetChanged();

                    int listViewHeight = view.getHeight() * datas.size();
                    popupWindow.update(editText.getWidth(), listViewHeight < initListViewHeight? listViewHeight : initListViewHeight);

                    if (datas.size() == 0){
                        popupWindow.dismiss();
                        iv_select.setVisibility(View.GONE);
                    }
                }
            });
            return view;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_select:
                showNumberList();
                break;
        }
    }
}