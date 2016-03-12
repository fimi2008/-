package com.ran.zidingyi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String tag = MainActivity.class.getSimpleName();
    private ImageView iv_home, iv_menu;
    private RelativeLayout level1,level2,level3;
    private boolean is_show_level2 = true;
    private boolean is_show_level3 = true;
    private boolean show_all = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initClickListen();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_home:
                // 等待当前动画执行完执行
                if (AnimUtil.animationCount > 0){
                    return;
                }
                if (is_show_level2){
                    int startOffset = 0;
                    if (is_show_level3){
                        closeLevel3(startOffset);
                        startOffset += 200;
                    }
                    Log.e(tag, "关闭二级菜单");
                    closeLevel2(startOffset);
                }else{
                    Log.e(tag, "打开二级菜单");
                    openLevel2(0);
                }
                break;
            case R.id.iv_menu:
                // 等待当前动画执行完执行
                if (AnimUtil.animationCount > 0){
                    return;
                }
                if (is_show_level3){
                    Log.e(tag, "关闭三级菜单");
                   closeLevel3(0);
                }else{
                    Log.e(tag, "打开三级菜单");
                    openLevel3(0);
                }
            default:break;
        }
    }

    /**
     * 监听用户按下menu(菜单键)时,打开/隐藏所有菜单
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU){
            // 等待当前动画执行完执行
            if (AnimUtil.animationCount > 0){
                return true;
            }
            if (show_all){
                closeAllMenu();
            }else{
                openAllMenu();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化UI
     */
    private void initView() {
        setContentView(R.layout.activity_main);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        level1 = (RelativeLayout) findViewById(R.id.level1);
        level2 = (RelativeLayout) findViewById(R.id.level2);
        level3 = (RelativeLayout) findViewById(R.id.level3);
    }

    /**
     * 初始化点击事件
     */
    private void initClickListen() {
        iv_home.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
    }

    /**
     * 关闭所有显示的菜单
     */
    private void closeAllMenu(){
        int startOffset = 0;
        if (is_show_level3){
            closeLevel3(startOffset);
            startOffset += 200;
        }
        if (is_show_level2){
            closeLevel2(startOffset);
            startOffset += 200;
        }
        closeLevel1(startOffset);
        show_all = false;
    }

    /**
     * 打开所有菜单
     */
    private void openAllMenu(){
        show_all = true;
        int startOffset = 0;
        openLevel1(startOffset);
        startOffset += 200;
        openLevel2(startOffset);
        startOffset += 200;
        openLevel3(startOffset);
    }

    private void openLevel2(int startOffset){
        AnimUtil.openMenu(level2, startOffset);
        is_show_level2 = true;
    }

    private void openLevel3(int startOffset){
        AnimUtil.openMenu(level3, startOffset);
        is_show_level3 = true;
    }

    private void closeLevel2(int startOffset){
        AnimUtil.closeMenu(level2, startOffset);
        is_show_level2 = false;
    }

    private void closeLevel3(int startOffset){
        AnimUtil.closeMenu(level3, startOffset);
        is_show_level3 = false;
    }

    private void closeLevel1(int startOffset){
        AnimUtil.closeMenu(level1, startOffset);
    }

    private void openLevel1(int startOffset){
        AnimUtil.openMenu(level1, startOffset);
    }
}