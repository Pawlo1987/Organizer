package com.example.user.organizer;

import android.os.Parcel;
import android.os.Parcelable;

// ------------Обект поле, для упаковки данных о поле--------------------
public class Field implements Parcelable{
    String id;
    String city_id;
    String name;
    String phone;
    String light_status;
    String coating_id;
    String shower_status;
    String roof_status;
    String geo_long;
    String geo_lat;
    String address;

    public Field() {
    }//Field

    public Field(String id, String city_id, String name,
                 String phone, String light_status, String coating_id,
                 String shower_status, String roof_status, String geo_long,
                 String geo_lat, String address) {
        this.id = id;
        this.city_id = city_id;
        this.name = name;
        this.phone = phone;
        this.light_status = light_status;
        this.coating_id = coating_id;
        this.shower_status = shower_status;
        this.roof_status = roof_status;
        this.geo_long = geo_long;
        this.geo_lat = geo_lat;
        this.address = address;
    }//Field


    protected Field(Parcel in) {
        id = in.readString();
        city_id = in.readString();
        name = in.readString();
        phone = in.readString();
        light_status = in.readString();
        coating_id = in.readString();
        shower_status = in.readString();
        roof_status = in.readString();
        geo_long = in.readString();
        geo_lat = in.readString();
        address = in.readString();
    }

    public static final Creator<Field> CREATOR = new Creator<Field>() {
        @Override
        public Field createFromParcel(Parcel in) {
            return new Field(in);
        }

        @Override
        public Field[] newArray(int size) {
            return new Field[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(city_id);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(light_status);
        dest.writeString(coating_id);
        dest.writeString(shower_status);
        dest.writeString(roof_status);
        dest.writeString(geo_long);
        dest.writeString(geo_lat);
        dest.writeString(address);
    }
}//Field
