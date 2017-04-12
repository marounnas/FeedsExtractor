package com.shedd.feedsextractor.data.model.cache;

import android.os.Parcel;
import android.os.Parcelable;

import com.shedd.feedsextractor.data.model.Tweet;

import java.util.ArrayList;


public class Filter implements Parcelable {

    private String name;
    private ArrayList<Tweet> tweets;

    public Filter() {
    }

    public Filter(Parcel source) {
        readFromParcel(source);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeList(tweets);
    }

    public void readFromParcel(Parcel source) {
        name = source.readString();
        tweets = source.readArrayList(Tweet.class.getClassLoader());
    }

    public static Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }

        @Override
        public Filter createFromParcel(Parcel source) {
            return new Filter(source);
        }
    };
}
