package com.example.user.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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

    //добавить строку в таблицу, соответственно параметры(cv - данные, table - таблица)
    public void insertInto(ContentValues cv, String table){
        db.insert(table, null,  cv);
    }//insertInto

    //длина определенного столбца
    public int tableSize(String table){
        String query = "SELECT _id FROM \"" + table + "\"";
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount();
    }//insertInto

    //поиск _id по определеному значению
    public int findIdbySPObject(String obj, String tableName, String tableColumn){
        String query = "SELECT \"" + tableName + "\"._id FROM \"" + tableName + "\" " +
                       "WHERE \"" + tableName + "\".\"" + tableColumn + "\" = \"" + obj + "\"";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToPosition(0); // переходим в курсоре в нулевую позицию
        return cursor.getInt(0);
    }//insertInto

    //заполнить коллекцию(List) данные для отображения в Spinner
    public List<String> fillList(String query) {
        List<String> list = new ArrayList<>();
        Cursor cursor;

        // получаем данные из БД в виде курсора
        cursor = db.rawQuery(query, null);
        int n = cursor.getCount();        //количество строк в курсоре
        for (int i = 0; i < n; i++) {
            cursor.moveToPosition(i); // переходим в курсоре на текущую позицию
            list.add(cursor.getString(0));
        }//for
        return list;
    }//fillList

    public SQLiteDatabase getDb() {
        return db;
    }

    // открытие подключения к БД
    public void open(){
        db = dbHelper.open();
    } // open

    // закрытие подключения к БД
    public void close(){
        if(dbHelper != null) dbHelper.close();
    } // close

}//DBUtilities
