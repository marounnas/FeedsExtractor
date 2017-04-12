package com.shedd.feedsextractor.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Urls implements Parcelable {

    @SerializedName("url")
    private String url;

    public Urls() {
    }

    public Urls(Parcel source) {
        readFromParcel(source);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }

    public void readFromParcel(Parcel source) {
        url = source.readString();
    }

    public static Creator<Urls> CREATOR = new Creator<Urls>() {
        @Override
        public Urls[] newArray(int size) {
            return new Urls[size];
        }

        @Override
        public Urls createFromParcel(Parcel source) {
            return new Urls(source);
        }
    };

}
