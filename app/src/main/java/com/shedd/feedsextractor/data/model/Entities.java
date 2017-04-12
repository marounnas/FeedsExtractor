package com.shedd.feedsextractor.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class Entities implements Parcelable {

    @SerializedName("urls")
    private ArrayList<Urls> urls;

    public Entities() {
    }

    public Entities(Parcel source) {
        readFromParcel(source);
    }

    public ArrayList<Urls> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<Urls> urls) {
        this.urls = urls;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(urls);
    }

    public void readFromParcel(Parcel source) {
//        urls = source.readParcelableArray(Urls.class.getClassLoader());
    }

    public static Creator<Entities> CREATOR = new Creator<Entities>() {
        @Override
        public Entities[] newArray(int size) {
            return new Entities[size];
        }

        @Override
        public Entities createFromParcel(Parcel source) {
            return new Entities(source);
        }
    };

}
