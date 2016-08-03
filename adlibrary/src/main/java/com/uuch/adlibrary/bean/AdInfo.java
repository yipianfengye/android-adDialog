package com.uuch.adlibrary.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aaron on 16/8/2.
 * 广告弹窗实体类
 */
public class AdInfo implements Parcelable{

    private String adId = null;
    private String title = null;
    private String url = null;
    private String activityImg = null;
    private int activityImgId = -1;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getActivityImg() {
        return activityImg;
    }

    public void setActivityImg(String activityImg) {
        this.activityImg = activityImg;
    }

    public int getActivityImgId() {
        return activityImgId;
    }

    public void setActivityImgId(int activityImgId) {
        this.activityImgId = activityImgId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.adId);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.activityImg);
        dest.writeInt(this.activityImgId);
    }

    public AdInfo() {
    }

    protected AdInfo(Parcel in) {
        this.adId = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.activityImg = in.readString();
        this.activityImgId = in.readInt();
    }

    public static final Creator<AdInfo> CREATOR = new Creator<AdInfo>() {
        @Override
        public AdInfo createFromParcel(Parcel source) {
            return new AdInfo(source);
        }

        @Override
        public AdInfo[] newArray(int size) {
            return new AdInfo[size];
        }
    };
}
