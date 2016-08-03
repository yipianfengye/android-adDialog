package com.uuch.android_addialog;

/**
 * Created by aaron on 16/8/3.
 */

public class DataBean {

    public int animType;
    public String animName;

    public int getAnimType() {
        return animType;
    }

    public DataBean(int animType, String animName) {
        this.animType = animType;
        this.animName = animName;
    }

    public void setAnimType(int animType) {
        this.animType = animType;
    }

    public String getAnimName() {
        return animName;
    }

    public void setAnimName(String animName) {
        this.animName = animName;
    }
}
