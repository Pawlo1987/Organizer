package com.example.user.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;
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

    } // DBUtilities

    //проверка введенных параметров для авторизации
    public User getUserParam(String login, String password) {
        User user = new User();
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("authorization", login, password);

            String resultDB = bg.get();
            JSONObject jResult = new JSONObject(resultDB);
            if(jResult.getString("error").toString().equals("")){
                user.id = jResult.getJSONObject("user").getString("id");
                user.name = jResult.getJSONObject("user").getString("name");

            }else{
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        return user;
    }//getUserParam

    //поиск значения searchValue в определеному стобце searchColumnName
    public String searchValueInColumn( String tableName, String searchColumnName,
                                           String resultColumnName, String searchValue){
        String value1 = null;
        //searchValue      -- переменная для сравнения
        //searchColumnName -- столбец в котором происходит сравнение переменной searchValue
        //resultColumnName -- столбец в котором находится результат
        //обращаемся к базе для получения списка имен городов
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("getSomeValue1byValue2", tableName, searchColumnName, searchValue);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if(jResult.getString("error").toString().equals("")){
                //получаем результат
                value1 = jResult.getJSONObject("rez").getString(resultColumnName).toString();
            }else{
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        return value1;
    }//searchValueInColumn

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

    //получаем данные из БД
    public List<String> getStrListTableFromDB( String tableName, String columnName) {
        List<String> list = new ArrayList<>();
        //обращаемся к базе для получения списка имен городов
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("getStrListTable", tableName, columnName);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if(jResult.getString("error").toString().equals("")){
                JSONObject jOb1 = jResult.getJSONObject(tableName);
                Iterator<String> i = jOb1.keys();

                while(i.hasNext()){
                    list.add(jOb1.getString(i.next()));
                }//while

                Log.d("FOOTBALL", list.toString());
            }else{
                Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
            }//if-else

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch

        return list;
    }//getStrListTableFromDB

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

    //обращяемся к БД на сервер для создания новой записи в таблицу users
    public void insertIntoUsers(String login, String password, String name, String phone,
                                String city_id, String email) {
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("insertIntoUsers", login, password, name, phone, city_id, email);

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
    }//insertIntoUsers

    //обращяемся к БД на сервер для создания новой записи в таблицу fields
    public void insertIntoFields(String city_id, String name, String phone, String light_status,
                                 String coating_id, String shower_status, String roof_status,
                                 String geo_long, String geo_lat, String address) {
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("insertIntoFields", city_id, name, phone, light_status, coating_id,
                        shower_status, roof_status, geo_long, geo_lat, address);

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
    }//insertIntoFields

    //обращяемся к БД на сервер для создания новой записи в таблицу participants
    public void insertIntoParticipants(String event_id, String user_id) {
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("insertIntoParticipants", event_id, user_id);

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
    }//insertIntoParticipants

    //обращяемся к БД на сервер для создания новой записи в таблицу regions
    public void insertIntoRegions(String name) {
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("insertIntoRegions", name);

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
    }//insertIntoRegions

    //обращяемся к БД на сервер для создания новой записи в таблицу events
    public void insertIntoEvents(String field_id, String city_id, String date,
                                 String time, String duration, String price,
                                 String password, String phone, String user_id) {
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("insertIntoEvents", field_id, city_id, date, time, duration, price,
                    password, phone, user_id);

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
    }//insertIntoEvents

    //обращяемся к БД на сервер для создания новой записи в таблицу coatings
    public void insertIntoCoatings(String type) {
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("insertIntoCoatings", type);

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
    }//insertIntoCoatings

    //обращяемся к БД на сервер для создания новой записи в таблицу cities
    public void insertIntoCities(String name, String region_id) {
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("insertIntoCities", name, region_id);

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
    }//insertIntoCities

    //обращяемся к БД на сервер для создания новой записи в таблицу notes
    public void insertIntoNotes(String head, String message, String date, String city_id) {
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("insertIntoNotes", head, message, date, city_id);

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
    }//insertIntoNotes

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

    // получение списка имен полей по признаку город
    public List<String> getSomeFieldsfromDB(String cityName) {
        List<String> list = new ArrayList<>();

        //получаем id по имени
        String city_id = getIdbyValue("cities", "name", cityName);
        //обращаемся к базе для получения списка имен городов
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("getSomeFields", city_id);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if(jResult.getString("error").toString().equals("")){
                JSONObject jOb1 = jResult.getJSONObject("name");
                Iterator<String> i = jOb1.keys();

                while(i.hasNext()){
                    list.add(jOb1.getString(i.next()));
                }//while

                Log.d("FOOTBALL", list.toString());
            }else{
                Toast.makeText(context, "В базе нет полей!", Toast.LENGTH_LONG).show();
            }//if-else

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch

        return list;
    }//getSomeFieldsfromDB

    //получение размера определенной таблицы
    public String getTableSize(String tableName) {
        String size = null;
        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getTableSize", tableName);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {
                //получаем результат
                size = jResult.getJSONObject("rez").getString("COUNT(id)").toString();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
        return size;
    }//getTableSize

    //получение коллекции пользователей для списка учасников
    public List<Participant> getListParticipantsUser(String cityName) {
        List<Participant> participantList = new ArrayList<>();

        //получаем id города
        String city_id = cityName.equals("ВСЕ ГОРОДА") ? "" : getIdbyValue(
                "cities", "name", cityName);

        //обращаемся к базе для получения списка имен городов
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("getListParticipantsUser", city_id);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if(jResult.getString("error").toString().equals("")){

                JSONObject jListId = jResult.getJSONObject("id");
                List<String> idList = getListFromJSON(jListId);

                JSONObject jListName = jResult.getJSONObject("name");
                List<String> nameList = getListFromJSON(jListName);

                JSONObject jListLogin = jResult.getJSONObject("login");
                List<String> loginList = getListFromJSON(jListLogin);

                JSONObject jListCityId = jResult.getJSONObject("city_id");
                List<String> cityIdList = getListFromJSON(jListCityId);

                int n = idList.size();

                for (int i = 0; i <n ; i++) {
                    participantList.add(new Participant(idList.get(i),nameList.get(i),loginList.get(i),cityIdList.get(i)));
                }//fori

            }else{
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch

        return participantList;
    }//getListParticipantsUser

    //получение коллекции событий
    public List<Event> getListEvents(String eventId) {
        List<Event> eventsList = new ArrayList<>();

        //обращаемся к базе для получения списка имен городов
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("getListEvents", eventId);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if(jResult.getString("error").toString().equals("")){

                JSONObject jListEventId = jResult.getJSONObject("id");
                List<String> ListEventId = getListFromJSON(jListEventId);

                JSONObject jListCityName = jResult.getJSONObject("city");
                List<String> ListCityName = getListFromJSON(jListCityName);

                JSONObject jListFieldName = jResult.getJSONObject("field");
                List<String> ListFieldName = getListFromJSON(jListFieldName);

                JSONObject jListEventData = jResult.getJSONObject("date");
                List<String> ListEventData = getListFromJSON(jListEventData);

                JSONObject jListEventTime = jResult.getJSONObject("time");
                List<String> ListEventTime = getListFromJSON(jListEventTime);

                JSONObject jListEventDuration = jResult.getJSONObject("duration");
                List<String> ListEventDuration = getListFromJSON(jListEventDuration);

                JSONObject jListEventPrice = jResult.getJSONObject("price");
                List<String> ListEventPrice = getListFromJSON(jListEventPrice);

                JSONObject jListEventPhone = jResult.getJSONObject("phone");
                List<String> ListEventPhone = getListFromJSON(jListEventPhone);

                JSONObject jListEventUser = jResult.getJSONObject("user");
                List<String> ListEventUser = getListFromJSON(jListEventUser);

                int n = ListCityName.size();

                for (int i = 0; i <n ; i++) {
                    eventsList.add(new Event(ListEventId.get(i), ListCityName.get(i),
                            ListFieldName.get(i), ListEventData.get(i), ListEventTime.get(i),
                            ListEventDuration.get(i), ListEventPrice.get(i), ListEventPhone.get(i),
                            ListEventUser.get(i)));
                }//fori

            }else{
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        }catch(Exception e){
            e.printStackTrace();
        }//try-catch

        return eventsList;
    }//getListEvents

    //участвует ли данный пользователь в данном событии
    public boolean isTakingPart(String idEvent, String idUser) {
        boolean result = false;
        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("isTakingPart", idEvent, idUser);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);

            //результат
            result = jResult.getString("error").toString().equals("") ? true : false;
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        return result;
    }//isTakingPart

}//DBUtilities
