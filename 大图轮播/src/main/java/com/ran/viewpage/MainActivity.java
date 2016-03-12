package com.ran.viewpage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ArrayList<Ad> ads = new ArrayList<Ad>();
    private TextView tv_intro;
    private String tag = this.getClass().getSimpleName();
    private LinearLayout dot_layout;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

            handler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    /**
     * 初始化UI
     */
    private void initView() {
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tv_intro = (TextView) findViewById(R.id.tv_intro);
        dot_layout = (LinearLayout) findViewById(R.id.dot_layout);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixel) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.e(tag, "position:" + position);
                updateIntro();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        ads.add(new Ad(R.mipmap.a, "巩俐不低俗,我就不低俗"));
        ads.add(new Ad(R.mipmap.b, "朴树又来了,在唱经典老歌引万人大合唱"));
        ads.add(new Ad(R.mipmap.c, "东风吹过春满地,揭秘北京电影节如何升级"));
        ads.add(new Ad(R.mipmap.d, "乐视网TV版大派送!参加活动赢取国安亚冠球票"));
        ads.add(new Ad(R.mipmap.e, "热血屌丝的反击"));

        initDots();

        viewPager.setAdapter(new MyPagerAdapter());

        // 将这个数置于最大数的中间数,并保证初始显示第一个
        int initPosition = Integer.MAX_VALUE / 2;
        initPosition = initPosition - initPosition % ads.size();
        viewPager.setCurrentItem(initPosition);

        updateIntro();

        handler.sendEmptyMessageDelayed(0, 3000);
    }

    /**
     * 初始化dot
     */
    private void initDots() {
        for (int i = 0; i < ads.size(); i++) {
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            if (i != 0) {
                params.leftMargin = 5;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.selector_dot);
            dot_layout.addView(view);
        }
    }

    /**
     * 更新广告文本
     */
    private void updateIntro() {
        int currentPage = viewPager.getCurrentItem();
        currentPage = currentPage % ads.size();
        tv_intro.setText(ads.get(currentPage).getIntro());

        for (int i = 0; i < dot_layout.getChildCount(); i++) {
            dot_layout.getChildAt(i).setEnabled(i == currentPage);
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        /**
         * 返回多少page
         *
         * @return
         */
        @Override
        public int getCount() {
            return Integer.MAX_VALUE; // 将这个数尽可能大
        }

        /**
         * ViewPager 预加载机制.最多保存3个page
         *
         * @param view
         * @param object
         * @return true:表示不用创建,使用缓存 false:去重新创建
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object; // google建议这么写
        }

        /**
         * 类似于BaseAdapter的getView方法
         * 用来将数据设置给view的
         * 由于它最多就3个界面,不需要viewHolder
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(MainActivity.this, R.layout.adapter_ad, null);
            ImageView image = (ImageView) view.findViewById(R.id.image);

            Ad ad = ads.get(position % ads.size());
            image.setImageResource(ad.getIconResId());
            container.addView(view); // 一定不能少,将view加入到viewPager中

            return view;
        }

        /**
         * 销毁page
         *
         * @param container
         * @param position  当前需要销毁的第几个page(下标)
         * @param object    当前需要销毁的page
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}