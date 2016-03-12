package com.ran.togglebtn.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作者: wangxiang on 16/3/12 19:20
 * 邮箱: vonshine15@163.com
 */
public class ToggleButton extends View{

    private ToggleState toggleState = ToggleState.Open;    // 开关的状态
    private Bitmap slideBg;             // 滑动开关背景图片
    private Bitmap switchBg;            // 滑动开关按钮背景图片
    private int currentX;               // 当前触摸点x坐标
    private boolean isMoving;           // 是否在滑动中

    public enum ToggleState{
        Open, Close
    }

    /**
     * 如果你的view需要在java代码中动态new出来.走的是这个构造方法
     * @param context
     */
    public ToggleButton(Context context) {
        super(context);
    }

    /**
     * 如果你的view只是在布局文件中使用,只需要重写这个构造方法
     * @param context
     * @param attrs
     */
    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置滑动块的背景图片
     * @param imgres
     */
    public void setSlideBackgroundResource(int imgres) {
        slideBg = BitmapFactory.decodeResource(getResources(), imgres);

    }

    /**
     * 设置滑动开关的背景图片
     * @param imgres
     */
    public void setSwitchBackgroundResource(int imgres) {
        switchBg = BitmapFactory.decodeResource(getResources(), imgres);

    }

    /**
     * 设置开关的状态
     * @param state
     */
    public void setToggleState(ToggleState state) {
        toggleState = state;
    }

    /**
     * 设置当前控件显示在屏幕上的宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(switchBg.getWidth(), switchBg.getHeight());
    }

    /**
     * 绘制自己显示在屏幕时的样子
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 1.绘制背景图片
         * left: 图片的左边的x坐标
         * top: 图片的顶部的y坐标
         */
        canvas.drawBitmap(switchBg, 0, 0, null);

        /**
         * 2.绘制滑动块的图片
         */
        if (isMoving){
            int left = currentX - slideBg.getWidth()/2;
            if (left < 0){
                left = 0;
            }
            if (left > switchBg.getWidth()-slideBg.getWidth()){
                left = switchBg.getWidth() - slideBg.getWidth();
            }
            canvas.drawBitmap(slideBg, left, 0,null);
        }else{
            // 此时抬起时,根据state状态去绘制滑块位置
            if (toggleState == ToggleState.Open) {
                canvas.drawBitmap(slideBg, switchBg.getWidth() - slideBg.getWidth(), 0, null);
            } else {
                canvas.drawBitmap(slideBg, 0, 0, null);
            }
        }
    }

    /**
     * 给控件绑定触摸监听器
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentX = (int) event.getX();
        switch (event.getAction()){
            case  MotionEvent.ACTION_DOWN:
                isMoving = true;
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                int centerX = switchBg.getWidth()/2;
                if (currentX > centerX){
                    // open
                    if (toggleState != ToggleState.Open){
                        toggleState = ToggleState.Open;
                        if (null != listener){
                            listener.onToggleStateChange(toggleState);
                        }
                    }
                }else{
                    if (toggleState != ToggleState.Close){
                        toggleState = ToggleState.Close;
                        if (null != listener){
                            listener.onToggleStateChange(toggleState);
                        }
                    }
                }

                break;
        }
        invalidate(); // 由系统来调用onDraw方法
        return true;
    }

    private OnToggleStateChangeListener listener;
    public void setOnToggleStateChangeListener(OnToggleStateChangeListener listener){
        this.listener = listener;
    }

    /**
     * 观察者模式:将控件状态反馈给调用者
     */
    public interface OnToggleStateChangeListener{
        void onToggleStateChange(ToggleState state);
    }
}
