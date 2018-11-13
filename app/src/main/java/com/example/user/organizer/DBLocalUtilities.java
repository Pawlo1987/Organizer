package com.example.user.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

//--------------Вспомагательный класс для работы с БД-----------------------------
public class DBLocalUtilities {

    private DBLocalHelper dbLocalHelper;
    private Context context;
    private SQLiteDatabase db;

    public DBLocalUtilities(Context context) {
        this.context = context;

        // создание вспомогательного класса
        dbLocalHelper = new DBLocalHelper(this.context);
        dbLocalHelper.create_db();
    } // DBUtilities

    //добавить строку в таблицу, соответственно параметры(cv - данные, table - таблица)
    public void insertInto(ContentValues cv, String table) {
        db.insert(table, null, cv);
    }//insertInto

    //обновить строку в таблице, соответственно параметры(cv - данные, table - таблица, id таблицы)
    public void updateTable(ContentValues cv, String table, String id) {
        db.update(table, cv, "_id = ?", new String[]{id});
    }//updateTable

    //удалить строку из таблицы, соответственно параметры(cv - данные, table - таблица)
    public void deleteRowById(String table, int id) {
        db.delete(table, "_id = " + id, null);
    }//insertInto

    //длина определенного столбца
    public int tableSize(String table) {
        String query = "SELECT _id FROM \"" + table + "\"";
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount();
    }//insertInto

    //поиск id по определеному значению
    public int findIdbySPObject(String obj, String tableName, String tableColumn) {
        String query = "SELECT \"" + tableName + "\"._id FROM \"" + tableName + "\" " +
                "WHERE \"" + tableName + "\".\"" + tableColumn + "\" = \"" + obj + "\"";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToPosition(0); // переходим в курсоре в нулевую позицию
        return cursor.getInt(0);
    }//insertInto

    //заполнить коллекцию(List) данные для отображения в Spinner
    public List<String> fillListStr(String query) {
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
    }//fillListStr

    //заполнить коллекцию(List) данные для отображения в Spinner
    public List<Field> getFieldList(String cityId) {
        List<Field> list = new ArrayList<>();
        Cursor cursor;

        // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
        String query = null;
        if(cityId.equals("")) query = "SELECT * FROM fields;";
        else query = "SELECT * FROM fields WHERE city_id = " + cityId + ";";
        // получаем данные из БД в виде курсора
        cursor = db.rawQuery(query, null);
        int n = cursor.getCount();        //количество строк в курсоре
        for (int i = 0; i < n; i++) {
            cursor.moveToPosition(i); // переходим в курсоре на текущую позицию
            //считываем данные
            list.add(
                    new Field(
                            (cursor.getString(0)),
                            (cursor.getString(1)),
                            (cursor.getString(2)),
                            (cursor.getString(3)),
                            (cursor.getString(4)),
                            (cursor.getString(5)),
                            (cursor.getString(6)),
                            (cursor.getString(7)),
                            (cursor.getString(8)),
                            (cursor.getString(9)),
                            (cursor.getString(10)),
                            (cursor.getString(11))
                    )//Field
            );//list.add
        }//for
        return list;
    }//getFieldList

    //заполнить коллекцию(List) данные для отображения в Spinner
    public List<Integer> fillListInt(String query) {
        List<Integer> list = new ArrayList<>();
        Cursor cursor;

        // получаем данные из БД в виде курсора
        cursor = db.rawQuery(query, null);
        int n = cursor.getCount();        //количество строк в курсоре
        for (int i = 0; i < n; i++) {
            cursor.moveToPosition(i); // переходим в курсоре на текущую позицию
            list.add(cursor.getInt(0));
        }//for
        return list;
    }//fillListStr

    public SQLiteDatabase getDb() {
        return db;
    }

    // открытие подключения к БД
    public void open() {
        db = dbLocalHelper.open();
    } // open

    // закрытие подключения к БД
    public void close() {
        if (dbLocalHelper != null) dbLocalHelper.close();
    } // close
}//DBLocalHelper
