package com.example.user.organizer;

import android.os.Parcel;
import android.os.Parcelable;

//----------Объект новостная запись, для упаковки даннных записей--------
public class Note implements Parcelable{
    public String noteId;
    public String noteLogo;
    public String noteHead;
    public String noteUserId;
    public String noteDate;
    public String noteCityName;
    public String noteMessage;
    public String noteTextSizeMessage;
    public String noteTextStyleMessage;

    public Note(String noteId, String noteLogo, String noteHead, String noteUserId, String noteDate,
                String noteCityName, String noteMessage, String noteTextSizeMessage,
                String noteTextStyleMessage) {
        this.noteId = noteId;
        this.noteLogo = noteLogo;
        this.noteHead = noteHead;
        this.noteUserId = noteUserId;
        this.noteDate = noteDate;
        this.noteCityName = noteCityName;
        this.noteMessage = noteMessage;
        this.noteTextSizeMessage = noteTextSizeMessage;
        this.noteTextStyleMessage = noteTextStyleMessage;
    }

    protected Note(Parcel in) {
        noteId = in.readString();
        noteLogo = in.readString();
        noteHead = in.readString();
        noteUserId = in.readString();
        noteDate = in.readString();
        noteCityName = in.readString();
        noteMessage = in.readString();
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

    public String getNoteId() {return noteId;}
    public void setNoteId(String noteId) {this.noteId = noteId;}

    public String getNoteLogo() {return noteLogo;}
    public void setNoteLogo(String noteLogo) {this.noteLogo = noteLogo;}

    public String getNoteHead() { return noteHead; }
    public void setNoteHead(String noteHead) { this.noteHead = noteHead; }

    public String getNoteUserId() {return noteUserId;}
    public void setNoteUserId(String noteUserId) {this.noteUserId = noteUserId;}

    public String getNoteDate() { return noteDate; }
    public void setNoteDate(String noteDate) { this.noteDate = noteDate; }

    public String getNoteCityName() { return noteCityName; }
    public void setNoteCityName(String noteCityName) { this.noteCityName = noteCityName; }

    public String getNoteMessage() { return noteMessage; }
    public void setNoteMessage(String noteMessage) { this.noteMessage = noteMessage; }

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
        dest.writeString(noteId);
        dest.writeString(noteLogo);
        dest.writeString(noteHead);
        dest.writeString(noteUserId);
        dest.writeString(noteDate);
        dest.writeString(noteCityName);
        dest.writeString(noteMessage);
        dest.writeString(noteTextSizeMessage);
        dest.writeString(noteTextStyleMessage);
    }
}//Note
