package com.example.user.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//--------------Вспомагательный класс для работы с БД-----------------------------
public class DBUtilities {

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public DBUtilities(Context context) {
        this.context = context;

        // создание вспомогательного класса
        dbHelper = new DBHelper( this.context);
        dbHelper.create_db();
    } // DBUtilities

    //добавить пользователя в активности CreateAccountActivity
    public void addNewUser(ContentValues cv){
        db.insert("user", null,  cv);
    }//addNewUser

    public Cursor updCursor(){
        String query = "SELECT * FROM employees";
        return db.rawQuery(query, null);
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    // открытие подключения к БД
    public void open(){
        db = dbHelper.open();
    } // open

    // открытие подключения к БД
    public void remove(int _id){

        //удаляем элемент
        db.delete("employees","_id = " + _id, null);
    } // open

    // закрытие подключения к БД
    public void close(){
        if(dbHelper != null) dbHelper.close();
    } // close

}//DBUtilities
