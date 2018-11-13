package com.example.user.organizer;

import android.os.Parcel;
import android.os.Parcelable;

//----------Объект новостная запись, для упаковки даннных записей--------
public class Note implements Parcelable{
    public String noteHead;
    public String noteDate;
    public String noteCityName;
    public String noteMessage;

    public Note(String noteHead, String noteDate, String noteCityName, String noteMessage) {
        this.noteHead = noteHead;
        this.noteDate = noteDate;
        this.noteCityName = noteCityName;
        this.noteMessage = noteMessage;
    }

    protected Note(Parcel in) {
        noteHead = in.readString();
        noteDate = in.readString();
        noteCityName = in.readString();
        noteMessage = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public Note() {

    }

    public String getNoteHead() { return noteHead; }
    public void setNoteHead(String noteHead) { this.noteHead = noteHead; }

    public String getNoteDate() { return noteDate; }
    public void setNoteDate(String noteDate) { this.noteDate = noteDate; }

    public String getNoteCityName() { return noteCityName; }
    public void setNoteCityName(String noteCityName) { this.noteCityName = noteCityName; }

    public String getNoteMessage() { return noteMessage; }
    public void setNoteMessage(String noteMessage) { this.noteMessage = noteMessage; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noteHead);
        dest.writeString(noteDate);
        dest.writeString(noteCityName);
        dest.writeString(noteMessage);
    }
}//Note
