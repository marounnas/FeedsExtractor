package com.shedd.feedsextractor.data.model.cache;

import android.os.Parcel;
import android.os.Parcelable;

import com.shedd.feedsextractor.data.model.Tweet;

import java.util.ArrayList;


public class Filters implements Parcelable {

    private ArrayList<Filter> filters;

    public Filters() {
    }

    public Filters(Parcel source) {
        readFromParcel(source);
    }

    public ArrayList<Filter> getFilters() {
        return filters;
    }

    public void setFilters(ArrayList<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(filters);
    }

    public void readFromParcel(Parcel source) {
        filters = source.readArrayList(Filter.class.getClassLoader());
    }

    public static Creator<Filters> CREATOR = new Creator<Filters>() {
        @Override
        public Filters[] newArray(int size) {
            return new Filters[size];
        }

        @Override
        public Filters createFromParcel(Parcel source) {
            return new Filters(source);
        }
    };
}
