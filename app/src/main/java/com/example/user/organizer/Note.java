package com.example.user.organizer;

import android.os.Parcel;
import android.os.Parcelable;

//----------Объект новостная запись, для упаковки даннных записей--------
public class Note implements Parcelable{
    public String noteLogo;
    public String noteHead;
    public String noteDate;
    public String noteCityName;
    public String noteMessage;
    public String noteTextSizeHead;
    public String noteTextSizeMessage;
    public String noteTextStyleMessage;

    public Note(String noteLogo, String noteHead, String noteDate,
                String noteCityName, String noteMessage, String noteTextSizeHead,
                String noteTextSizeMessage, String noteTextStyleMessage) {
        this.noteLogo = noteLogo;
        this.noteHead = noteHead;
        this.noteDate = noteDate;
        this.noteCityName = noteCityName;
        this.noteMessage = noteMessage;
        this.noteTextSizeHead = noteTextSizeHead;
        this.noteTextSizeMessage = noteTextSizeMessage;
        this.noteTextStyleMessage = noteTextStyleMessage;
    }

    protected Note(Parcel in) {
        noteLogo = in.readString();
        noteHead = in.readString();
        noteDate = in.readString();
        noteCityName = in.readString();
        noteMessage = in.readString();
        noteTextSizeHead = in.readString();
        noteTextSizeMessage = in.readString();
        noteTextStyleMessage = in.readString();
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

    public String getNoteLogo() {return noteLogo;}
    public void setNoteLogo(String noteLogo) {this.noteLogo = noteLogo;}

    public String getNoteHead() { return noteHead; }
    public void setNoteHead(String noteHead) { this.noteHead = noteHead; }

    public String getNoteDate() { return noteDate; }
    public void setNoteDate(String noteDate) { this.noteDate = noteDate; }

    public String getNoteCityName() { return noteCityName; }
    public void setNoteCityName(String noteCityName) { this.noteCityName = noteCityName; }

    public String getNoteMessage() { return noteMessage; }
    public void setNoteMessage(String noteMessage) { this.noteMessage = noteMessage; }

    public String getNoteTextSizeHead() {return noteTextSizeHead;}
    public void setNoteTextSizeHead(String noteTextSizeHead) {this.noteTextSizeHead = noteTextSizeHead;}

    public String getNoteTextSizeMessage() {return noteTextSizeMessage;}
    public void setNoteTextSizeMessage(String noteTextSizeMessage) {this.noteTextSizeMessage = noteTextSizeMessage;}

    public String getNoteTextStyleMessage() {return noteTextStyleMessage;}
    public void setNoteTextStyleMessage(String noteTextStyleMessage) {this.noteTextStyleMessage = noteTextStyleMessage;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noteLogo);
        dest.writeString(noteHead);
        dest.writeString(noteDate);
        dest.writeString(noteCityName);
        dest.writeString(noteMessage);
        dest.writeString(noteTextSizeHead);
        dest.writeString(noteTextSizeMessage);
        dest.writeString(noteTextStyleMessage);
    }
}//Note
