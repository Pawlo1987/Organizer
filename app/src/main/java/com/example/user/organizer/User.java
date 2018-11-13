package com.example.user.organizer;

import android.os.Parcel;
import android.os.Parcelable;

//------объект пользователь, для упаковки данных о пользователе------
public class User implements Parcelable {
    public String id;
    public String name;

    public User() {
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}
