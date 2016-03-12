package com.ran.viewpage;

/**
 * 广告对象封装
 *
 * 作者: wangxiang on 16/3/6 17:13
 * 邮箱: vonshine15@163.com
 */
public class Ad {
    private int iconResId;

    private String intro;

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Ad(int iconResId, String intro) {
        this.iconResId = iconResId;
        this.intro = intro;
    }
}