package com.example.user.organizer;


import android.os.Parcel;
import android.os.Parcelable;

//------------Объект учасник, для упаковки данных об учаснике
public class Participant implements Parcelable{
    String id;
    String name;
    String login;
    String city_id;

    public Participant(String id, String name, String login, String city_id) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.city_id = city_id;
    }//Participant

    public Participant() {
    }

    protected Participant(Parcel in) {
        id = in.readString();
        name = in.readString();
        login = in.readString();
        city_id = in.readString();
    }

    public static final Creator<Participant> CREATOR = new Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLogin() { return login;    }
    public void setLogin(String login) { this.login = login; }

    public String getCity_id() { return city_id; }
    public void setCity_id(String city_id) { this.city_id = city_id; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(login);
        dest.writeString(city_id);
    }
}//Participant
