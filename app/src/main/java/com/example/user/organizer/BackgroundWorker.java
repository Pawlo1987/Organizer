package com.example.user.organizer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

//-------Класс для подключения и выполнения запросов на сервере-----------
//-------Поделился Валера Дорохольский---------------------------
public class BackgroundWorker extends AsyncTask<String, Void, String> implements AutoCloseable {
    final String DOMAIN = "http://strahovanie.dn.ua/football_db/index.php";     // домен для запросов

    // формирование, отправка запроса и обработка ответа
    @Override
    protected String doInBackground(String... params) {

        String operation = params[0];                           // операция
        switch (operation) {

            // авторизация в приложении
            case "authorization":
                try {
                    // параметры для передачи на сервер (для авторизации)
                    String login = params[1];
                    String password = params[2];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("login", "utf8") + "=" + URLEncoder.encode(login, "utf8")
                            + "&" + URLEncoder.encode("password", "utf8") + "=" + URLEncoder.encode(password, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getStrListTable":
                try {
                    // параметры для передачи на сервер (для получения всех запесей одного столбца)
                    String tableName = params[1];
                    String columnName = params[2];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("tableName", "utf8") + "=" + URLEncoder.encode(tableName, "utf8")
                            + "&" + URLEncoder.encode("columnName", "utf8") + "=" + URLEncoder.encode(columnName, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getListValuesByValueAndHisColumn":
                try {
                    // параметры для передачи на сервер (для получения коллекции значений по заданому значению и его столбцу)
                    String tableName = params[1];
                    String searchColumnName = params[2];
                    String searchValue = params[3];
                    String resultColumnName = params[4];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("tableName", "utf8") + "=" + URLEncoder.encode(tableName, "utf8")
                            + "&" + URLEncoder.encode("searchColumnName", "utf8") + "=" + URLEncoder.encode(searchColumnName, "utf8")
                            + "&" + URLEncoder.encode("searchValue", "utf8") + "=" + URLEncoder.encode(searchValue, "utf8")
                            + "&" + URLEncoder.encode("resultColumnName", "utf8") + "=" + URLEncoder.encode(resultColumnName, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "isTakingPart":
                try {
                    // параметры для передачи на сервер (для получения информации об участии в событии)
                    String idEvent = params[1];
                    String idUser = params[2];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("idEvent", "utf8") + "=" + URLEncoder.encode(idEvent, "utf8")
                            + "&" + URLEncoder.encode("idUser", "utf8") + "=" + URLEncoder.encode(idUser, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "deleteRowById":
                try {
                    // параметры для передачи на сервер (для удаления записи по id)
                    String tableName = params[1];
                    String id = params[2];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("tableName", "utf8") + "=" + URLEncoder.encode(tableName, "utf8")
                            + "&" + URLEncoder.encode("id", "utf8") + "=" + URLEncoder.encode(id, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "deleteRowByTwoValueAndTheyColumnName":
                try {
                    // параметры для передачи на сервер (для удаления записи по двум значениям и их стобцам)
                    String tableName = params[1];
                    String firstColumnName = params[2];
                    String firstValue = params[3];
                    String secondColumnName = params[4];
                    String secondValue = params[5];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("tableName", "utf8") + "=" + URLEncoder.encode(tableName, "utf8")
                            + "&" + URLEncoder.encode("firstColumnName", "utf8") + "=" + URLEncoder.encode(firstColumnName, "utf8")
                            + "&" + URLEncoder.encode("firstValue", "utf8") + "=" + URLEncoder.encode(firstValue, "utf8")
                            + "&" + URLEncoder.encode("secondColumnName", "utf8") + "=" + URLEncoder.encode(secondColumnName, "utf8")
                            + "&" + URLEncoder.encode("secondValue", "utf8") + "=" + URLEncoder.encode(secondValue, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getSomeFields":
                try {
                    // параметры для передачи на сервер (для получения записей имен полей по городу)
                    String city_id = params[1];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("city_id", "utf8") + "=" + URLEncoder.encode(city_id, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getAllNotes":
                try {

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getSomeNotes":
                try {
                    // параметры для передачи на сервер (для получения некоторых запесей новостей)
                    String city_id = params[1];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("city_id", "utf8") + "=" + URLEncoder.encode(city_id, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getListParticipantsUser":
                try {
                    // параметры для передачи на сервер (для получения списка учасников)
                    String city_id = params[1];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("city_id", "utf8") + "=" + URLEncoder.encode(city_id, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getListEvents":
                try {
                    // параметры для передачи на сервер (для получения списка событий)
                    String eventId = params[1];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("eventId", "utf8") + "=" + URLEncoder.encode(eventId, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getListField":
                try {
                    // параметры для передачи на сервер (для получения списка полей)
                    String fieldId = params[1];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("fieldId", "utf8") + "=" + URLEncoder.encode(fieldId, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getListEventsForAuthUser":
                try {
                    // параметры для передачи на сервер (для получения списка событий для авторизированого пользователя)
                    String eventId = params[1];
                    String idAuthUser = params[2];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("eventId", "utf8") + "=" + URLEncoder.encode(eventId, "utf8")
                            + "&" + URLEncoder.encode("idAuthUser", "utf8") + "=" + URLEncoder.encode(idAuthUser, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "updateEventsTable":
                try {
                    // параметры для передачи на сервер (для обновления записи таблицы события по id)
                    String id = params[1];
                    String field_id = params[2];
                    String city_id = params[3];
                    String date = params[4];
                    String time = params[5];
                    String duration = params[6];
                    String price = params[7];
                    String password = params[8];
                    String phone = params[9];
                    String user_id = params[10];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("id", "utf8") + "=" + URLEncoder.encode(id, "utf8")
                            + "&" + URLEncoder.encode("field_id", "utf8") + "=" + URLEncoder.encode(field_id, "utf8")
                            + "&" + URLEncoder.encode("city_id", "utf8") + "=" + URLEncoder.encode(city_id, "utf8")
                            + "&" + URLEncoder.encode("date", "utf8") + "=" + URLEncoder.encode(date, "utf8")
                            + "&" + URLEncoder.encode("time", "utf8") + "=" + URLEncoder.encode(time, "utf8")
                            + "&" + URLEncoder.encode("duration", "utf8") + "=" + URLEncoder.encode(duration, "utf8")
                            + "&" + URLEncoder.encode("price", "utf8") + "=" + URLEncoder.encode(price, "utf8")
                            + "&" + URLEncoder.encode("password", "utf8") + "=" + URLEncoder.encode(password, "utf8")
                            + "&" + URLEncoder.encode("phone", "utf8") + "=" + URLEncoder.encode(phone, "utf8")
                            + "&" + URLEncoder.encode("user_id", "utf8") + "=" + URLEncoder.encode(user_id, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "insertIntoUsers":
                try {
                    // параметры для передачи на сервер (для создания новой записи пользователя)
                    String login = params[1];
                    String password = params[2];
                    String name = params[3];
                    String phone = params[4];
                    String city_id = params[5];
                    String email = params[6];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("login", "utf8") + "=" + URLEncoder.encode(login, "utf8")
                            + "&" + URLEncoder.encode("password", "utf8") + "=" + URLEncoder.encode(password, "utf8")
                            + "&" + URLEncoder.encode("name", "utf8") + "=" + URLEncoder.encode(name, "utf8")
                            + "&" + URLEncoder.encode("phone", "utf8") + "=" + URLEncoder.encode(phone, "utf8")
                            + "&" + URLEncoder.encode("city_id", "utf8") + "=" + URLEncoder.encode(city_id, "utf8")
                            + "&" + URLEncoder.encode("email", "utf8") + "=" + URLEncoder.encode(email, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "insertIntoCities":
                try {
                    // параметры для передачи на сервер (для создания новой записи города)
                    String name = params[1];
                    String region_id = params[2];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("name", "utf8") + "=" + URLEncoder.encode(name, "utf8")
                            + "&" + URLEncoder.encode("region_id", "utf8") + "=" + URLEncoder.encode(region_id, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "insertIntoParticipants":
                try {
                    // параметры для передачи на сервер (для создания новой записи участника)
                    String event_id = params[1];
                    String user_id = params[2];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("event_id", "utf8") + "=" + URLEncoder.encode(event_id, "utf8")
                            + "&" + URLEncoder.encode("user_id", "utf8") + "=" + URLEncoder.encode(user_id, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "insertIntoCoatings":
                try {
                    // параметры для передачи на сервер (для создания новой записи покрытия)
                    String type = params[1];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("type", "utf8") + "=" + URLEncoder.encode(type, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "insertIntoFields":
                try {
                    // параметры для передачи на сервер (для создания новой записи поля)
                    String city_id = params[1];
                    String name = params[2];
                    String phone = params[3];
                    String light_status = params[4];
                    String coating_id = params[5];
                    String shower_status = params[6];
                    String roof_status = params[7];
                    String geo_long = params[8];
                    String geo_lat = params[9];
                    String address = params[10];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("city_id", "utf8") + "=" + URLEncoder.encode(city_id, "utf8")
                            + "&" + URLEncoder.encode("name", "utf8") + "=" + URLEncoder.encode(name, "utf8")
                            + "&" + URLEncoder.encode("phone", "utf8") + "=" + URLEncoder.encode(phone, "utf8")
                            + "&" + URLEncoder.encode("light_status", "utf8") + "=" + URLEncoder.encode(light_status, "utf8")
                            + "&" + URLEncoder.encode("coating_id", "utf8") + "=" + URLEncoder.encode(coating_id, "utf8")
                            + "&" + URLEncoder.encode("shower_status", "utf8") + "=" + URLEncoder.encode(shower_status, "utf8")
                            + "&" + URLEncoder.encode("roof_status", "utf8") + "=" + URLEncoder.encode(roof_status, "utf8")
                            + "&" + URLEncoder.encode("geo_long", "utf8") + "=" + URLEncoder.encode(geo_long, "utf8")
                            + "&" + URLEncoder.encode("geo_lat", "utf8") + "=" + URLEncoder.encode(geo_lat, "utf8")
                            + "&" + URLEncoder.encode("address", "utf8") + "=" + URLEncoder.encode(address, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "insertIntoEvents":
                try {
                    // параметры для передачи на сервер (для создания новой записи события)
                    String field_id = params[1];
                    String city_id = params[2];
                    String date = params[3];
                    String time = params[4];
                    String duration = params[5];
                    String price = params[6];
                    String password = params[7];
                    String phone = params[8];
                    String user_id = params[9];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("field_id", "utf8") + "=" + URLEncoder.encode(field_id, "utf8")
                            + "&" + URLEncoder.encode("city_id", "utf8") + "=" + URLEncoder.encode(city_id, "utf8")
                            + "&" + URLEncoder.encode("date", "utf8") + "=" + URLEncoder.encode(date, "utf8")
                            + "&" + URLEncoder.encode("time", "utf8") + "=" + URLEncoder.encode(time, "utf8")
                            + "&" + URLEncoder.encode("duration", "utf8") + "=" + URLEncoder.encode(duration, "utf8")
                            + "&" + URLEncoder.encode("price", "utf8") + "=" + URLEncoder.encode(price, "utf8")
                            + "&" + URLEncoder.encode("password", "utf8") + "=" + URLEncoder.encode(password, "utf8")
                            + "&" + URLEncoder.encode("phone", "utf8") + "=" + URLEncoder.encode(phone, "utf8")
                            + "&" + URLEncoder.encode("user_id", "utf8") + "=" + URLEncoder.encode(user_id, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "insertIntoRegions":
                try {
                    // параметры для передачи на сервер (для создания новой записи региона)
                    String name = params[1];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("name", "utf8") + "=" + URLEncoder.encode(name, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "insertIntoNotes":
                try {
                    // параметры для передачи на сервер (для создания новой записи новостей)
                    String head = params[1];
                    String message = params[2];
                    String date = params[3];
                    String city_id = params[4];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("head", "utf8") + "=" + URLEncoder.encode(head, "utf8")
                            + "&" + URLEncoder.encode("message", "utf8") + "=" + URLEncoder.encode(message, "utf8")
                            + "&" + URLEncoder.encode("date", "utf8") + "=" + URLEncoder.encode(date, "utf8")
                            + "&" + URLEncoder.encode("city_id", "utf8") + "=" + URLEncoder.encode(city_id, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getIdByValue":
                try {
                    // параметры для передачи на сервер (для поиска id по определеному значению)
                    String tableName = params[1];
                    String columnName = params[2];
                    String value = params[3];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("tableName", "utf8") + "=" + URLEncoder.encode(tableName, "utf8")
                            + "&" + URLEncoder.encode("columnName", "utf8") + "=" + URLEncoder.encode(columnName, "utf8")
                            + "&" + URLEncoder.encode("value", "utf8") + "=" + URLEncoder.encode(value, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getIdByTwoValues":
                try {
                    // параметры для передачи на сервер (для поиска id по двум определеным значениям)
                    String tableName = params[1];
                    String firstColumnName = params[2];
                    String firstValue = params[3];
                    String secondColumnName = params[4];
                    String secondValue = params[5];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("tableName", "utf8") + "=" + URLEncoder.encode(tableName, "utf8")
                            + "&" + URLEncoder.encode("firstColumnName", "utf8") + "=" + URLEncoder.encode(firstColumnName, "utf8")
                            + "&" + URLEncoder.encode("firstValue", "utf8") + "=" + URLEncoder.encode(firstValue, "utf8")
                            + "&" + URLEncoder.encode("secondColumnName", "utf8") + "=" + URLEncoder.encode(secondColumnName, "utf8")
                            + "&" + URLEncoder.encode("secondValue", "utf8") + "=" + URLEncoder.encode(secondValue, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "searchValueInColumn":
                try {
                    // параметры для передачи на сервер (для поиска value в определенном столбце)
                    String tableName = params[1];
                    String searchColumnName = params[2];
                    String searchValue = params[3];
                    String resultColumnName = params[4];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("tableName", "utf8") + "=" + URLEncoder.encode(tableName, "utf8")
                            + "&" + URLEncoder.encode("searchColumnName", "utf8") + "=" + URLEncoder.encode(searchColumnName, "utf8")
                            + "&" + URLEncoder.encode("searchValue", "utf8") + "=" + URLEncoder.encode(searchValue, "utf8")
                            + "&" + URLEncoder.encode("resultColumnName", "utf8") + "=" + URLEncoder.encode(resultColumnName, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getTableSize":
                try {
                    // параметры для передачи на сервер (для поиска размера таблицы по названию таблицы)
                    String tableName = params[1];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("tableName", "utf8") + "=" + URLEncoder.encode(tableName, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch

            case "getMaxValueInHisColumn":
                try {
                    // параметры для передачи на сервер (для поиска максимального элемента в столбце)
                    String tableName = params[1];
                    String searchColumnName = params[2];

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8")
                            + "&" + URLEncoder.encode("tableName", "utf8") + "=" + URLEncoder.encode(tableName, "utf8")
                            + "&" + URLEncoder.encode("searchColumnName", "utf8") + "=" + URLEncoder.encode(searchColumnName, "utf8");

                    return workWithServer(postData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "IO:" + e.getMessage();
                } // try-catch
        } // switch

        return null;
    } // doInBackground

    // отправка данных на сервер и получение ответа
    private String workWithServer(String postData) {
        try {
            URL url = new URL(DOMAIN); // URL для отправки запроса

            // создание соединения
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // метод передачи данных
            httpURLConnection.setRequestMethod("POST");

            // разрешение на отправку и принятие данных
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            // создаем буфер для отпаравки запроса
            OutputStream ops = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ops, "UTF8"));

            bw.write(postData);     // отправляем данные на сервер
            bw.flush();             // очищаем буфер
            bw.close();             // закрываем буфер

            // создаем буфер для получение результата
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf8"));

            StringBuilder sb = new StringBuilder(); // строка для получения результата
            String line = "";                       // строка для построчного чтения из пришедшего ответа
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            is.close();
            httpURLConnection.disconnect();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "IO:" + e.getMessage();
        } // try-catch
    } // workWithServer

    @Override
    public void close() throws Exception {

    }
} // BackgroundWorker