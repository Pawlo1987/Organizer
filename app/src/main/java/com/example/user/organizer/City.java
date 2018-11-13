package com.example.user.organizer;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable{
    private String id;
    private String name;

    public City(String id, String name) {
        this.id = id;
        this.name = name;
    }//City

    protected City(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}//City
