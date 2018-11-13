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

// класс для подключения и выполнения запросов на сервере
public class BackgroundWorker extends AsyncTask<String, Void, String> implements AutoCloseable{
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

            case "getAllCities":
                try {

                    // формируем строку для отправки на сервер
                    String postData = URLEncoder.encode("operation", "utf8") + "=" + URLEncoder.encode(operation, "utf8");

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

            case "addNewUser":
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

            case "addNewNote":
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

            case "getIdbyValue":
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