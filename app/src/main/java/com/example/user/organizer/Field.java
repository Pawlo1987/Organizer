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
    String user_id;

    public Field() {
    }//Field

    public Field(String id, String city_id, String name,
                 String phone, String light_status, String coating_id,
                 String shower_status, String roof_status, String geo_long,
                 String geo_lat, String address, String user_id) {
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
        this.user_id = user_id;
    }//Field

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getCity_id() {return city_id;}
    public void setCity_id(String city_id) {this.city_id = city_id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public String getLight_status() {return light_status;}
    public void setLight_status(String light_status){this.light_status = light_status;}

    public String getCoating_id() {return coating_id;}
    public void setCoating_id(String coating_id) {this.coating_id = coating_id;}

    public String getShower_status() {return shower_status;}
    public void setShower_status(String shower_status) {this.shower_status = shower_status;}

    public String getRoof_status() {return roof_status;}
    public void setRoof_status(String roof_status) {this.roof_status = roof_status;}

    public String getGeo_long() {return geo_long;}
    public void setGeo_long(String geo_long) {this.geo_long = geo_long;}

    public String getGeo_lat() {return geo_lat;}
    public void setGeo_lat(String geo_lat) {this.geo_lat = geo_lat;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}

    public String getUser_id() {return user_id;}
    public void setUser_id(String user_id) {this.user_id = user_id;}

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
        user_id = in.readString();
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
        dest.writeString(user_id);
    }
}//Field
