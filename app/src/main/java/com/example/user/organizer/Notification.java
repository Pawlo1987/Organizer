package com.example.user.organizer;

import android.os.Parcel;
import android.os.Parcelable;

//------объект Уведомление, для упаковки данных в уведомлении------
public class Notification implements Parcelable {
    String id;
    String event_id;
    String user_id;
    String city_id;
    String field_id;
    String time;
    String date;
    String message;
    String notice;

    public Notification(String id, String event_id, String user_id,
                        String city_id, String field_id, String time,
                        String date, String message, String notice) {
        this.id = id;
        this.event_id = event_id;
        this.user_id = user_id;
        this.city_id = city_id;
        this.field_id = field_id;
        this.time = time;
        this.date = date;
        this.message = message;
        this.notice = notice;
    }

    protected Notification(Parcel in) {
        id = in.readString();
        event_id = in.readString();
        user_id = in.readString();
        city_id = in.readString();
        field_id = in.readString();
        time = in.readString();
        date = in.readString();
        message = in.readString();
        notice = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(event_id);
        dest.writeString(user_id);
        dest.writeString(city_id);
        dest.writeString(field_id);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(message);
        dest.writeString(notice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getEvent_id() {return event_id;}
    public void setEvent_id(String event_id) {this.event_id = event_id;}

    public String getUser_id() {return user_id;}
    public void setUser_id(String user_id) {this.user_id = user_id;}

    public String getCity_id() {return city_id;}
    public void setCity_id(String city_id) {this.city_id = city_id;}

    public String getField_id() {return field_id;}
    public void setField_id(String field_id) {this.field_id = field_id;}

    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public String getMessage() {return message;}
    public void setMessage(String message_id) {this.message = message_id;}

    public String getNotice() {return notice;}
    public void setNotice(String notice) {this.notice = notice;}
}//Notification
