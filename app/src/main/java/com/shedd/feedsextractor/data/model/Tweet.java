package com.shedd.feedsextractor.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Tweet implements Parcelable {

    @SerializedName("id_str")
    private String id;
    @SerializedName("text")
    private String message;
    @SerializedName("created_at")
    private String date;
    @SerializedName("user")
    private User user;
    @SerializedName("entities")
    private Entities entities;

    public Tweet() {
    }

    public Tweet(Parcel source) {
        readFromParcel(source);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(message);
        dest.writeString(date);
        dest.writeParcelable(user, 0);
        dest.writeParcelable(entities, 0);
    }

    public void readFromParcel(Parcel source) {
        id = source.readString();
        message = source.readString();
        date = source.readString();
        user = source.readParcelable(User.class.getClassLoader());
        entities = source.readParcelable(Entities.class.getClassLoader());
    }

    public static Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }

        @Override
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }
    };
}
