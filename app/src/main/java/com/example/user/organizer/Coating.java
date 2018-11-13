package com.example.user.organizer;

import android.os.Parcel;
import android.os.Parcelable;

public class Coating implements Parcelable{
    private String id;
    private String type;

    public Coating(String id, String type) {
        this.id = id;
        this.type = type;
    }//Coating

    protected Coating(Parcel in) {
        id = in.readString();
        type = in.readString();
    }

    public static final Creator<Coating> CREATOR = new Creator<Coating>() {
        @Override
        public Coating createFromParcel(Parcel in) {
            return new Coating(in);
        }

        @Override
        public Coating[] newArray(int size) {
            return new Coating[size];
        }
    };

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
    }
}//Coating
