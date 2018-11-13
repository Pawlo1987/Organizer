package com.example.user.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//--------------Вспомагательный класс для работы с БД-----------------------------
public class DBUtilities{

//    private DBHelper dbHelper;
    private Context context;
//    private SQLiteDatabase db;

    public DBUtilities(Context context) {
        this.context = context;

//        // создание вспомогательного класса
//        dbHelper = new DBHelper( this.context);
//        dbHelper.create_db();
    } // DBUtilities

    //добавить строку в таблицу, соответственно параметры(cv - данные, table - таблица)
    public void insertInto(ContentValues cv, String table){
//        db.insert(table, null,  cv);
    }//insertInto

    //обновить строку в таблице, соответственно параметры(cv - данные, table - таблица, id таблицы)
    public void updateTable(ContentValues cv, String table, String id){
//        db.update(table, cv,  "_id = ?", new String[] { id });
    }//updateTable

    //удалить строку из таблицы, соответственно параметры(cv - данные, table - таблица)
    public void deleteRowById(String table, int id){
//        db.delete(table, "_id = " + id,  null);
    }//insertInto

    //длина определенного столбца
    public int tableSize(String table){
//        String query = "SELECT _id FROM \"" + table + "\"";
//        Cursor cursor = db.rawQuery(query, null);
//        return cursor.getCount();
        return 0;
    }//insertInto

    //поиск _id по определеному значению
    public String getIdbyValue( String tableName, String columnName, String value){
        String id = null;
        //обращаемся к базе для получения списка имен городов
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("getIdbyValue", tableName, columnName, value);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if(jResult.getString("error").toString().equals("")){
                //получаем результат
                id = jResult.getJSONObject("rez").getString("id").toString();
            }else{
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        return id;
    }//getIdbyValue

    //заполнить коллекцию(List) данные для отображения в Spinner
    public List<String> fillListStr(String query) {
        List<String> list = new ArrayList<>();
//        Cursor cursor;
//
//        // получаем данные из БД в виде курсора
//        cursor = db.rawQuery(query, null);
//        int n = cursor.getCount();        //количество строк в курсоре
//        for (int i = 0; i < n; i++) {
//            cursor.moveToPosition(i); // переходим в курсоре на текущую позицию
//            list.add(cursor.getString(0));
//        }//for
//        return list;
        return null;
    }//fillListStr

    //заполнить коллекцию(List) данные для отображения в Spinner
    public List<Integer> fillListInt(String query) {
//        List<Integer> list = new ArrayList<>();
//        Cursor cursor;
//
//        // получаем данные из БД в виде курсора
//        cursor = db.rawQuery(query, null);
//        int n = cursor.getCount();        //количество строк в курсоре
//        for (int i = 0; i < n; i++) {
//            cursor.moveToPosition(i); // переходим в курсоре на текущую позицию
//            list.add(cursor.getInt(0));
//        }//for
//        return list;
        return null;
    }//fillListStr

    public SQLiteDatabase getDb() {
//        return db;
        return null;
    }

    // открытие подключения к БД
    public void open(){
//        db = dbHelper.open();
    } // open

    // закрытие подключения к БД
    public void close(){
//        if(dbHelper != null) dbHelper.close();
    } // close

    //получаем данные из БД
    public List<String> getStringListFromDB( String operation, String tableName) {
        List<String> list = new ArrayList<>();
        //обращаемся к базе для получения списка имен городов
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute(operation);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if(jResult.getString("error").toString().equals("")){
                JSONObject jCities = jResult.getJSONObject(tableName);
                Iterator<String> i = jCities.keys();

                while(i.hasNext()){
                    list.add(jCities.getString(i.next()));
                }//while

                Log.d("FOOTBALL", list.toString());
            }else{
                Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
            }//if-else

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch

        return list;
    }//getStringListFromDB

    // получение списка всех имеющихся новостей из базы
    public List<Note> getAllNotesfromDB() {
        List<Note> notes = new ArrayList<>();

        //обращаемся к базе для получения списка имен городов
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("getAllNotes");

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if(jResult.getString("error").toString().equals("")){

                JSONObject jListHead = jResult.getJSONObject("head");
                List<String> headList = getListFromJSON(jListHead);

                JSONObject jListName = jResult.getJSONObject("name");
                List<String> nameList = getListFromJSON(jListName);

                JSONObject jListDate = jResult.getJSONObject("date");
                List<String> dateList = getListFromJSON(jListDate);

                JSONObject jListMess = jResult.getJSONObject("message");
                List<String> messList = getListFromJSON(jListMess);

                int n = headList.size();

                for (int i = 0; i <n ; i++) {
                    notes.add(new Note(headList.get(i),dateList.get(i),nameList.get(i),messList.get(i)));
                }//fori

            }else{
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        return notes;
    }//getNotesfromDB

    //вспомагательный внутренний метод для преобразования JSONObject в List
    private List<String> getListFromJSON(JSONObject jList) {
        List<String> list = new ArrayList<>();

        Iterator<String> i = jList.keys();     //создаем итератор для перебора занчений объекта

        //перебираем значения и заносим в коллекцию list
        while(i.hasNext()){
            try {
                list.add(jList.getString(i.next()).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }//while

        return list;
    }//getListFromJSON

    //обращяемся к БД на сервер для создания новой записи в таблицу user
    public void addNewUser( String login, String password, String name, String phone, String city_id, String email) {
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("addNewUser", login, password, name, phone, city_id, email);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if(jResult.getString("error").toString().equals("")){
                //выводим текст с положительным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            }else{
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
    }//addNewUser

    //обращяемся к БД на сервер для создания новой записи в таблицу notes
    public void addNewNote(String head, String message, String date, String city_id) {
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("addNewNote", head, message, date, city_id);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if(jResult.getString("error").toString().equals("")){
                //выводим текст с положительным ответом о создании новой записи в таблицу
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            }else{
                //выводим текст с отрецательным ответом о создании новой записи в таблицу
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
    }//addNewNote

    // получение списка новостей из определенного города из базы
    public List<Note> getSomeNotesfromDB(String cityName) {
        List<Note> notes = new ArrayList<>();

        //получаем id города
        String city_id = getIdbyValue("cities", "name", cityName);

        //обращаемся к базе для получения списка имен городов
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("getSomeNotes", city_id);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if(jResult.getString("error").toString().equals("")){

                JSONObject jListHead = jResult.getJSONObject("head");
                List<String> headList = getListFromJSON(jListHead);

                JSONObject jListName = jResult.getJSONObject("name");
                List<String> nameList = getListFromJSON(jListName);

                JSONObject jListDate = jResult.getJSONObject("date");
                List<String> dateList = getListFromJSON(jListDate);

                JSONObject jListMess = jResult.getJSONObject("message");
                List<String> messList = getListFromJSON(jListMess);

                int n = headList.size();

                for (int i = 0; i <n ; i++) {
                    notes.add(new Note(headList.get(i),dateList.get(i),nameList.get(i),messList.get(i)));
                }//fori

            }else{
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        return notes;
    }//getSomeNotesfromDB
}//DBUtilities
