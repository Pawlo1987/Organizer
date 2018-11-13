package com.example.user.organizer;

import android.os.Parcel;
import android.os.Parcelable;

//------объект для упаковки данных о пользователе------
public class User implements Parcelable {
    public String id;
    public String login;
    public String password;
    public String name;
    public String phone;
    public String city_id;
    public String email;
    public String logo;

    public User(String id, String login, String password, String name,
                String phone, String city_id, String email, String logo) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.city_id = city_id;
        this.email = email;
        this.logo = logo;
    }

    protected User(Parcel in) {
        id = in.readString();
        login = in.readString();
        password = in.readString();
        name = in.readString();
        phone = in.readString();
        city_id = in.readString();
        email = in.readString();
        logo = in.readString();
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

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getLogin() {return login;}
    public void setLogin(String login) {this.login = login;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public String getCity_id() {return city_id;}
    public void setCity_id(String city_id) {this.city_id = city_id;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getLogo() {return logo;}
    public void setLogo(String logo) {this.logo = logo;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(login);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(city_id);
        dest.writeString(email);
        dest.writeString(logo);
    }
}//User
