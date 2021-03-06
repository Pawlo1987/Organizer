package com.example.user.organizer;

import android.os.Parcel;
import android.os.Parcelable;

//-------------Объект Собитие для упаковки данных о событии----------
public class Event implements Parcelable{
    String eventId;
    String cityName;
    String fieldName;
    String eventData;
    String eventTime;
    String eventDuration;
    String eventPrice;
    String eventPassword;
    String eventPhone;
    String eventUserId;
    String eventUserStatus;

    public Event(String eventId, String cityName, String fieldName,
                 String eventData, String eventTime, String eventDuration,
                 String eventPrice, String eventPassword, String eventPhone,
                 String eventUserId, String eventUserStatus) {
        this.eventId = eventId;
        this.cityName = cityName;
        this.fieldName = fieldName;
        this.eventData = eventData;
        this.eventTime = eventTime;
        this.eventDuration = eventDuration;
        this.eventPrice = eventPrice;
        this.eventPassword = eventPassword;
        this.eventPhone = eventPhone;
        this.eventUserId = eventUserId;
        this.eventUserStatus = eventUserStatus;
    }//Event

    public Event() {
    }//Event

    protected Event(Parcel in) {
        eventId = in.readString();
        cityName = in.readString();
        fieldName = in.readString();
        eventData = in.readString();
        eventTime = in.readString();
        eventDuration = in.readString();
        eventPrice = in.readString();
        eventPassword = in.readString();
        eventPhone = in.readString();
        eventUserId = in.readString();
        eventUserStatus = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getEventPassword() { return eventPassword; }
    public void setEventPassword(String eventPassword) { this.eventPassword = eventPassword; }

    public String getEventUserStatus() { return eventUserStatus; }
    public void setEventUserStatus(String eventUserStatus) { this.eventUserStatus = eventUserStatus; }

    public String getEventUserId() { return eventUserId; }
    public void setEventUserId(String eventUserId) { this.eventUserId = eventUserId; }

    public String getEventDuration() { return eventDuration; }
    public void setEventDuration(String eventDuration) { this.eventDuration = eventDuration; }

    public String getEventPrice() { return eventPrice; }
    public void setEventPrice(String eventPrice) { this.eventPrice = eventPrice; }

    public String getEventPhone() { return eventPhone; }
    public void setEventPhone(String eventPhone) { this.eventPhone = eventPhone; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getEventData() { return eventData; }
    public void setEventData(String eventData) { this.eventData = eventData; }

    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(cityName);
        dest.writeString(fieldName);
        dest.writeString(eventData);
        dest.writeString(eventTime);
        dest.writeString(eventDuration);
        dest.writeString(eventPrice);
        dest.writeString(eventPassword);
        dest.writeString(eventPhone);
        dest.writeString(eventUserId);
        dest.writeString(eventUserStatus);
    }
}//Event
