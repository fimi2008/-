package com.ran.zidingyi;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

/**
 * 菜单动画显示工具类
 *
 * 作者: wangxiang on 16/3/6 12:42
 * 邮箱: vonshine15@163.com
 */
public class AnimUtil {
    public static int animationCount = 0;

    /**
     * 立即关闭菜单动画
     * @param rl
     */
    public static void closeMenu(RelativeLayout rl){
        closeMenu(rl, 0);
    }

    /**
     * 延迟${startOffset}毫秒关闭菜单动画
     * @param rl
     * @param startOffset
     */
    public static void closeMenu(RelativeLayout rl, int startOffset){
        // 将子view都置为不可用
        for (int i = 0; i < rl.getChildCount(); i++){
            rl.getChildAt(i).setEnabled(false);
        }
        // pivotXValue: 0 -1
        RotateAnimation animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1);
        animation.setDuration(500);
        animation.setFillAfter(true); // 动画结束后保持当时的状态
        animation.setStartOffset(startOffset);  // 动画延迟
        animation.setAnimationListener(new MyAnimationListen());    // 绑定动画监听器
        rl.startAnimation(animation);
    }

    /**
     * 立即打开菜单动画
     * @param rl
     */
    public static void openMenu(RelativeLayout rl){
        openMenu(rl, 0);
    }

    /**
     * 延${startOffset}毫秒打开菜单动画
     * @param rl
     * @param startOffset
     */
    public static void openMenu(RelativeLayout rl, int startOffset){
        // 将子view都置为可用
        for (int i = 0; i < rl.getChildCount(); i++){
            rl.getChildAt(i).setEnabled(true);
        }
        // pivotXValue: 0 -1
        RotateAnimation animation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1);
        animation.setDuration(500);
        animation.setFillAfter(true); // 动画结束后保持当时的状态
        animation.setStartOffset(startOffset);  // 动画延迟
        animation.setAnimationListener(new MyAnimationListen()); // 绑定动画监听器
        rl.startAnimation(animation);
    }

    /**
     * 动画监听器
     */
    static class MyAnimationListen implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {
            animationCount++;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animationCount--;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}