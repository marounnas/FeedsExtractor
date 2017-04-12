package com.shedd.feedsextractor.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 4/11/2017.
 */

public class User implements Parcelable {

    @SerializedName("name")
    private String name;
    @SerializedName("screen_name")
    private String screenName;
    @SerializedName("profile_background_image_url")
    private String imageUrl;

    public User() {
    }

    public User(Parcel source) {
        readFromParcel(source);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(screenName);
        dest.writeString(imageUrl);
    }

    public void readFromParcel(Parcel source) {
        name = source.readString();
        screenName = source.readString();
        imageUrl = source.readString();
    }

    public static Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }
    };
}
