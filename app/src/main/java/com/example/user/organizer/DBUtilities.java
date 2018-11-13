package com.example.user.organizer;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//--------------Вспомагательный класс для работы с БД-----------------------------
public class DBUtilities {

    private Context context;

    public DBUtilities(Context context) {
        this.context = context;

    } // DBUtilities

    // метод для проверки подключения
    protected boolean isConnection() {
        boolean res;
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(cs);
        return (cm.getActiveNetworkInfo() == null)          //проверка подключения к интернету
                || (getTableSize("cities") == null) //проверка подключения к БД
                ? false : true;
    }

    //проверка введенных параметров для авторизации
    public String getAuthUserParam(String login, String password) {
        String idAuthUser = null;
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("authorization", login, password);

            String resultDB = bg.get();
            JSONObject jResult = new JSONObject(resultDB);
            if (jResult.getString("error").toString().equals("")) {
                idAuthUser = jResult.getJSONObject("user").getString("id");

            } else {
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
        return idAuthUser;
    }//getAuthUserParam

    //поиск значения searchValue в определеному стобце searchColumnName
    public String searchValueInColumn(String tableName, String searchColumnName,
                                      String resultColumnName, String searchValue) {
        String value1 = null;
        //searchValue      -- переменная для сравнения
        //searchColumnName -- столбец в котором происходит сравнение переменной searchValue
        //resultColumnName -- столбец в котором находится результат
        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("searchValueInColumn", tableName, searchColumnName, searchValue, resultColumnName);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {
                //получаем результат
                value1 = jResult.getJSONObject("rez").getString(resultColumnName).toString();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
        return value1;
    }//searchValueInColumn

    //поиск _id по определеному значению
    public String getIdByValue(String tableName, String columnName, String value) {
        String id = null;
        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getIdByValue", tableName, columnName, value);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {
                //получаем результат
                id = jResult.getJSONObject("rez").getString("id").toString();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
        return id;
    }//getIdByValue

    //поиск _id по двум определеным значениям
    public String getIdByTwoValues(String tableName, String firstColumnName, String firstValue,
                                   String secondColumnName, String secondValue) {
        String id = null;
        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getIdByTwoValues", tableName, firstColumnName, firstValue,
                    secondColumnName, secondValue);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {
                //получаем результат
                id = jResult.getJSONObject("rez").getString("id").toString();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
        return id;
    }//getIdByValue

    //получаем данные из БД
    public List<String> getStrListTableFromDB(String tableName, String columnName) {
        List<String> list = new ArrayList<>();
        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getStrListTable", tableName, columnName);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {
                JSONObject jOb1 = jResult.getJSONObject(tableName);
                Iterator<String> i = jOb1.keys();

                while (i.hasNext()) {
                    list.add(jOb1.getString(i.next()));
                }//while

                Log.d("FOOTBALL", list.toString());
            } else {
                Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch

        return list;
    }//getStrListTableFromDB

    // получение списка всех имеющихся новостей из базы
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();

        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getAllNotes");

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {

                JSONObject jListId = jResult.getJSONObject("id");
                List<String> idList = getListFromJSON(jListId);

                JSONObject jListLogo = jResult.getJSONObject("logo");
                List<String> logoList = getListFromJSON(jListLogo);

                JSONObject jListHead = jResult.getJSONObject("head");
                List<String> headList = getListFromJSON(jListHead);

                JSONObject jListUser = jResult.getJSONObject("user_id");
                List<String> userList = getListFromJSON(jListUser);

                JSONObject jListCity = jResult.getJSONObject("city");
                List<String> cityList = getListFromJSON(jListCity);

                JSONObject jListDate = jResult.getJSONObject("date");
                List<String> dateList = getListFromJSON(jListDate);

                JSONObject jListMess = jResult.getJSONObject("message");
                List<String> messList = getListFromJSON(jListMess);

                JSONObject jListTSizeMessage = jResult.getJSONObject("tsizemessage");
                List<String> tSizeMessageList = getListFromJSON(jListTSizeMessage);

                JSONObject jListTStyleMessage = jResult.getJSONObject("tstylemessage");
                List<String> tStyleMessageList = getListFromJSON(jListTStyleMessage);

                int n = headList.size();

                for (int i = 0; i < n; i++) {
                    notes.add(new Note(idList.get(i), logoList.get(i), headList.get(i),
                            userList.get(i), dateList.get(i),
                            cityList.get(i), messList.get(i), tSizeMessageList.get(i),
                            tStyleMessageList.get(i)));
                }//fori

            } else {
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
        return notes;
    }//getAllNotes

    //вспомагательный внутренний метод для преобразования JSONObject в List
    private List<String> getListFromJSON(JSONObject jList) {
        List<String> list = new ArrayList<>();

        Iterator<String> i = jList.keys();     //создаем итератор для перебора занчений объекта

        //перебираем значения и заносим в коллекцию list
        while (i.hasNext()) {
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
                                String city_id, String email, String logo) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("insertIntoUsers", login, password, name, phone, city_id, email, logo);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//insertIntoUsers

    //обращяемся к БД на сервер для создания нового записи в таблицу notification
    public void insertIntoNotifications(String event_id, String user_id, String city_id, String field_id, String time,
                                        String date, String message_id, String notice) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("insertIntoNotifications", event_id, user_id, city_id, field_id, time, date, message_id, notice);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом о создании нового уведмления
//                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом о создании нового уведмления
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//insertIntoNotification

    //обращяемся к БД на сервер для создания новой записи в таблицу fields
    public void insertIntoFields(String city_id, String name, String phone, String light_status,
                                 String coating_id, String shower_status, String roof_status,
                                 String geo_long, String geo_lat, String address, String user_id) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("insertIntoFields", city_id, name, phone, light_status, coating_id,
                    shower_status, roof_status, geo_long, geo_lat, address, user_id);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//insertIntoFields

    //обращяемся к БД на сервер для создания новой записи в таблицу participants
    public void insertIntoParticipants(String event_id, String user_id) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("insertIntoParticipants", event_id, user_id);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом о создании нового пользователя
//                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//insertIntoParticipants

    //обращяемся к БД на сервер для создания новой записи в таблицу regions
    public void insertIntoRegions(String name) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("insertIntoRegions", name);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//insertIntoRegions

    //обращяемся к БД на сервер для создания новой записи в таблицу events
    public void insertIntoEvents(String field_id, String city_id, String date,
                                 String time, String duration, String price,
                                 String password, String phone, String user_id) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("insertIntoEvents", field_id, city_id, date, time, duration, price,
                    password, phone, user_id);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//insertIntoEvents

    //обращяемся к БД на сервер для создания новой записи в таблицу coatings
    public void insertIntoCoatings(String type) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("insertIntoCoatings", type);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//insertIntoCoatings

    //обращяемся к БД на сервер для создания новой записи в таблицу cities
    public void insertIntoCities(String name, String region_id) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("insertIntoCities", name, region_id);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//insertIntoCities

    //обращяемся к БД на сервер для создания новой записи в таблицу notes
    public void insertIntoNotes(String logo, String head, String user_id, String date,
                                String city_id, String message,
                                String tsizemessage, String tstylemessage) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("insertIntoNotes", logo, head, user_id, date, city_id, message, tsizemessage, tstylemessage);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом о создании новой записи в таблицу
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом о создании новой записи в таблицу
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//insertIntoNotes

    // получение списка новостей из определенного города из базы
    public List<Note> getSomeNotesFromDB(String cityName) {
        List<Note> notes = new ArrayList<>();

        //получаем id города
        String city_id = getIdByValue("cities", "name", cityName);

        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getSomeNotes", city_id);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {

                JSONObject jListId = jResult.getJSONObject("id");
                List<String> idList = getListFromJSON(jListId);

                JSONObject jListLogo = jResult.getJSONObject("logo");
                List<String> logoList = getListFromJSON(jListLogo);

                JSONObject jListHead = jResult.getJSONObject("head");
                List<String> headList = getListFromJSON(jListHead);

                JSONObject jListUser = jResult.getJSONObject("user_id");
                List<String> userList = getListFromJSON(jListUser);

                JSONObject jListCity = jResult.getJSONObject("city");
                List<String> cityList = getListFromJSON(jListCity);

                JSONObject jListDate = jResult.getJSONObject("date");
                List<String> dateList = getListFromJSON(jListDate);

                JSONObject jListMess = jResult.getJSONObject("message");
                List<String> messList = getListFromJSON(jListMess);

                JSONObject jListTSizeMessage = jResult.getJSONObject("tsizemessage");
                List<String> tSizeMessageList = getListFromJSON(jListTSizeMessage);

                JSONObject jListTStyleMessage = jResult.getJSONObject("tstylemessage");
                List<String> tStyleMessageList = getListFromJSON(jListTStyleMessage);

                int n = headList.size();

                for (int i = 0; i < n; i++) {
                    notes.add(new Note(idList.get(i), logoList.get(i), headList.get(i),
                            userList.get(i), dateList.get(i),
                            cityList.get(i), messList.get(i), tSizeMessageList.get(i),
                            tStyleMessageList.get(i)));
                }//fori

            } else {
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
        return notes;
    }//getSomeNotesFromDB

    // получение списка имен полей по признаку город
    public List<String> getSomeFieldsFromDB(String cityName) {
        List<String> list = new ArrayList<>();

        //получаем id по имени
        String city_id = getIdByValue("cities", "name", cityName);
        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getSomeFields", city_id);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {
                JSONObject jOb1 = jResult.getJSONObject("name");
                Iterator<String> i = jOb1.keys();

                while (i.hasNext()) {
                    list.add(jOb1.getString(i.next()));
                }//while

                Log.d("FOOTBALL", list.toString());
            } else {
                Toast.makeText(context, "В базе нет полей!", Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch

        return list;
    }//getSomeFieldsFromDB

    // получение списка уведомлений по признаку user_id
    public List<Notification> getSomeNotifications(String user_id) {
        List<Notification> listNotification = new ArrayList<>();

        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getSomeNotifications", user_id);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {

                JSONObject jListId = jResult.getJSONObject("id");
                List<String> idList = getListFromJSON(jListId);

                JSONObject jListEvent = jResult.getJSONObject("event");
                List<String> eventList = getListFromJSON(jListEvent);

                JSONObject jListUser = jResult.getJSONObject("user");
                List<String> userList = getListFromJSON(jListUser);

                JSONObject jListCity = jResult.getJSONObject("city");
                List<String> cityList = getListFromJSON(jListCity);

                JSONObject jListField = jResult.getJSONObject("field");
                List<String> fieldList = getListFromJSON(jListField);

                JSONObject jListTime = jResult.getJSONObject("time");
                List<String> timeList = getListFromJSON(jListTime);

                JSONObject jListDate = jResult.getJSONObject("date");
                List<String> dateList = getListFromJSON(jListDate);

                JSONObject jListMessage = jResult.getJSONObject("message");
                List<String> messageList = getListFromJSON(jListMessage);

                JSONObject jListNotice = jResult.getJSONObject("notice");
                List<String> noticeList = getListFromJSON(jListNotice);

                int n = idList.size();

                for (int i = 0; i < n; i++) {
                    listNotification.add(new Notification(idList.get(i), eventList.get(i),
                            userList.get(i), cityList.get(i), fieldList.get(i), timeList.get(i),
                            dateList.get(i), messageList.get(i), noticeList.get(i)));
                }//fori

            } else {
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch

        return listNotification;
    }//getSomeNotifications

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

    //получение максимального элемента в столбце
    public String getMaxValueInHisColumn(String tableName, String searchColumnName) {
        String size = null;
        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getMaxValueInHisColumn", tableName, searchColumnName);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {
                //получаем результат
                size = jResult.getJSONObject("rez")
                        .getString(String.format("MAX(%s)", searchColumnName))
                        .toString();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
        return size;
    }//getMaxValueInHisColumn

    //получение коллекции пользователей для списка учасников
    public List<Participant> getListParticipantsUser(String cityName, String idAuthUser) {
        List<Participant> participantList = new ArrayList<>();

        //получаем id города
        String city_id = cityName.equals("ВСЕ ГОРОДА") ? "" : getIdByValue(
                "cities", "name", cityName);

        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getListParticipantsUser", city_id);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {

                JSONObject jListId = jResult.getJSONObject("id");
                List<String> idList = getListFromJSON(jListId);

                JSONObject jListName = jResult.getJSONObject("name");
                List<String> nameList = getListFromJSON(jListName);

                JSONObject jListLogin = jResult.getJSONObject("login");
                List<String> loginList = getListFromJSON(jListLogin);

                JSONObject jListCityId = jResult.getJSONObject("city_id");
                List<String> cityIdList = getListFromJSON(jListCityId);

                int n = idList.size();

                for (int i = 0; i < n; i++) {
                    if (idList.get(i).equals(idAuthUser))
                        continue;  //если попали на запись организатора
                    participantList.add(new Participant(idList.get(i), nameList.get(i), loginList.get(i), cityIdList.get(i)));
                }//fori

            } else {
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch

        return participantList;
    }//getListParticipantsUser

    //получение коллекции полей
    public List<Field> getListField(String fieldId, String cityId) {
        List<Field> fieldList = new ArrayList<>();

        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getListField", fieldId, cityId);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {
                JSONObject jListId = jResult.getJSONObject("id");
                List<String> listId = getListFromJSON(jListId);

                JSONObject jListCity = jResult.getJSONObject("city");
                List<String> listCity = getListFromJSON(jListCity);

                JSONObject jListName = jResult.getJSONObject("name");
                List<String> listName = getListFromJSON(jListName);

                JSONObject jListPhone = jResult.getJSONObject("phone");
                List<String> listPhone = getListFromJSON(jListPhone);

                JSONObject jListLight = jResult.getJSONObject("light");
                List<String> listLight = getListFromJSON(jListLight);

                JSONObject jListCoating = jResult.getJSONObject("coating");
                List<String> listCoating = getListFromJSON(jListCoating);

                JSONObject jListShower = jResult.getJSONObject("shower");
                List<String> listShower = getListFromJSON(jListShower);

                JSONObject jListRoof = jResult.getJSONObject("roof");
                List<String> listRoof = getListFromJSON(jListRoof);

                JSONObject jListGeoLong = jResult.getJSONObject("geoLong");
                List<String> listGeoLong = getListFromJSON(jListGeoLong);

                JSONObject jListGeoLat = jResult.getJSONObject("geoLat");
                List<String> listGeoLat = getListFromJSON(jListGeoLat);

                JSONObject jListAddress = jResult.getJSONObject("address");
                List<String> listAddress = getListFromJSON(jListAddress);

                JSONObject jListUser = jResult.getJSONObject("user");
                List<String> listUser = getListFromJSON(jListUser);

                int n = listId.size();

                for (int i = 0; i < n; i++) {
                    fieldList.add(new Field(listId.get(i), listCity.get(i),
                            listName.get(i), listPhone.get(i), listLight.get(i),
                            listCoating.get(i), listShower.get(i), listRoof.get(i),
                            listGeoLong.get(i), listGeoLat.get(i), listAddress.get(i),
                            listUser.get(i)));
                }//fori

            } else {
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch

        return fieldList;
    }//getListField

    //получение коллекции городов
    public List<City> getListCity(String cityId) {
        List<City> cityList = new ArrayList<>();

        //обращаемся к базе для получения списка необходимых полей таблицы
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getListCity", cityId);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {
                JSONObject jListId = jResult.getJSONObject("id");
                List<String> listId = getListFromJSON(jListId);

                JSONObject jListName = jResult.getJSONObject("name");
                List<String> listName = getListFromJSON(jListName);

                int n = listId.size();

                for (int i = 0; i < n; i++) {
                    cityList.add(new City(listId.get(i), listName.get(i)));
                }//fori

            } else {
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch

        return cityList;
    }//getListCity

    //получение коллекции покрытий полей
    public List<Coating> getListCoating(String coatingId) {
        List<Coating> coatingList = new ArrayList<>();

        //обращаемся к базе для получения списка необходимых полей таблицы
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getListCoating", coatingId);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {
                JSONObject jListId = jResult.getJSONObject("id");
                List<String> listId = getListFromJSON(jListId);

                JSONObject jListType = jResult.getJSONObject("type");
                List<String> listType = getListFromJSON(jListType);

                int n = listId.size();

                for (int i = 0; i < n; i++) {
                    coatingList.add(new Coating(listId.get(i), listType.get(i)));
                }//fori

            } else {
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch

        return coatingList;
    }//getListCoating

    //получение коллекции пользователей
    public List<User> getListUser(String userId) {
        List<User> userList = new ArrayList<>();

        //обращаемся к базе для получения списка необходимых полей таблицы
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getListUser", userId);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {
                JSONObject jListId = jResult.getJSONObject("id");
                List<String> listId = getListFromJSON(jListId);

                JSONObject jListLogin = jResult.getJSONObject("login");
                List<String> listLogin = getListFromJSON(jListLogin);

                JSONObject jListPassword = jResult.getJSONObject("password");
                List<String> listPassword = getListFromJSON(jListPassword);

                JSONObject jListName = jResult.getJSONObject("name");
                List<String> listName = getListFromJSON(jListName);

                JSONObject jListPhone = jResult.getJSONObject("phone");
                List<String> listPhone = getListFromJSON(jListPhone);

                JSONObject jListCityId = jResult.getJSONObject("city_id");
                List<String> listCityId = getListFromJSON(jListCityId);

                JSONObject jListEmail = jResult.getJSONObject("email");
                List<String> listEmail = getListFromJSON(jListEmail);

                JSONObject jListLogo = jResult.getJSONObject("logo");
                List<String> listLogo = getListFromJSON(jListLogo);

                int n = listId.size();

                for (int i = 0; i < n; i++) {
                    userList.add(new User(listId.get(i), listLogin.get(i), listPassword.get(i),
                            listName.get(i), listPhone.get(i), listCityId.get(i), listEmail.get(i),
                            listLogo.get(i)));
                }//fori

            } else {
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch

        return userList;
    }//getListUser

    public String getLogoCount() {
        String result = null;
        //обращаемся к базе для получения списка необходимых полей таблицы
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getLogoCount");

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {
                JSONObject jLogoCount = jResult.getJSONObject("count");
                List<String> logoCount = getListFromJSON(jLogoCount);

                result = logoCount.get(0);
            } else {
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch

        return result;
    }//getLogoList

    //получение коллекции событий для авторизированого пользователя
    public List<Event> getListEvents(String eventId, String cityId, String idAuthUser) {
        List<Event> eventsList = new ArrayList<>();

        //обращаемся к базе для получения списка имен городов
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getListEvents", eventId, cityId, idAuthUser);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {

                JSONObject jListEventId = jResult.getJSONObject("id");
                List<String> listEventId = getListFromJSON(jListEventId);

                JSONObject jListCityName = jResult.getJSONObject("city");
                List<String> listCityName = getListFromJSON(jListCityName);

                JSONObject jListFieldName = jResult.getJSONObject("field");
                List<String> listFieldName = getListFromJSON(jListFieldName);

                JSONObject jListEventData = jResult.getJSONObject("date");
                List<String> listEventData = getListFromJSON(jListEventData);

                JSONObject jListEventTime = jResult.getJSONObject("time");
                List<String> listEventTime = getListFromJSON(jListEventTime);

                JSONObject jListEventDuration = jResult.getJSONObject("duration");
                List<String> listEventDuration = getListFromJSON(jListEventDuration);

                JSONObject jListEventPrice = jResult.getJSONObject("price");
                List<String> listEventPrice = getListFromJSON(jListEventPrice);

                JSONObject jListEventPassword = jResult.getJSONObject("password");
                List<String> listEventPassword = getListFromJSON(jListEventPassword);

                JSONObject jListEventPhone = jResult.getJSONObject("phone");
                List<String> listEventPhone = getListFromJSON(jListEventPhone);

                JSONObject jListEventUser = jResult.getJSONObject("user");
                List<String> listEventUser = getListFromJSON(jListEventUser);

                JSONObject jListEventUserStatus = jResult.getJSONObject("status");
                List<String> listEventUserStatus = getListFromJSON(jListEventUserStatus);

                int n = listCityName.size();

                for (int i = 0; i < n; i++) {
                    eventsList.add(new Event(listEventId.get(i), listCityName.get(i),
                            listFieldName.get(i), listEventData.get(i), listEventTime.get(i),
                            listEventDuration.get(i), listEventPrice.get(i), listEventPassword.get(i),
                            listEventPhone.get(i), listEventUser.get(i), listEventUserStatus.get(i)));
                }//fori

            } else {
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch

        return eventsList;
    }//getListEvents

    //участвует ли данный пользователь в данном событии
    public boolean isTakingPart(String idEvent, String idUser) {
        boolean result = false;
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("isTakingPart", idEvent, idUser);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);

            //результат
            result = jResult.getString("error").toString().equals("") ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
        return result;
    }//isTakingPart

    //удалить запись по id
    public void deleteRowById(String tableName, String id) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("deleteRowById", tableName, id);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом о создании нового пользователя
//                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//deleteRowById

    //удалить запись по Value
    public void deleteRowByValue(String tableName, String columnName, String value) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("deleteRowByValue", tableName, columnName, value);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом о создании нового пользователя
//                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//deleteRowByValue

    //получить коллекцию значений по задоному значению и его столбцу
    public List<String> getListValuesByValueAndHisColumn(String tableName, String searchColumnName,
                                                         String searchValue, String resultColumnName) {
        List<String> list = new ArrayList<>();

        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("getListValuesByValueAndHisColumn", tableName, searchColumnName, searchValue, resultColumnName);

            String resultdb = bg.get();
            Log.d("FOOTBALL", resultdb);
            JSONObject jResult = new JSONObject(resultdb);

            if (jResult.getString("error").toString().equals("")) {

                JSONObject jListRes = jResult.getJSONObject(
                        String.format("%s", resultColumnName));
                list = getListFromJSON(jListRes);

                Log.d("FOOTBALL", list.toString());
            } else {
//                Toast.makeText(context, "Учасников нет!", Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch

        return list;
    }//getListValuesByValueAndHisColumn

    //удаление записи по двум значениям и их стобцам
    public void deleteRowByTwoValueAndTheyColumnName(String tableName, String firstColumnName,
                                                     String firstValue, String secondColumnName,
                                                     String secondValue) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("deleteRowByTwoValueAndTheyColumnName", tableName, firstColumnName,
                    firstValue, secondColumnName, secondValue);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом о создании нового пользователя
//                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом о создании нового пользователя
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//deleteRowByTwoValueAndTheyColumnName

    //обновляем запись в таблице "events" по id
    public void updateEventsTable(String id, String field_id, String city_id,
                                  String date, String time, String duration,
                                  String price, String password, String phone, String user_id) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("updateEventsTable", id, field_id, city_id, date, time, duration, price,
                    password, phone, user_id);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//updateEventsTable

    //обновляем запись в таблице "fields" по id
    public void updateFieldsTable(String id, String city_id, String name, String phone,
                                  String light_status, String coating_id, String shower_status,
                                  String roof_status, String geo_long, String geo_lat, String address,
                                  String user_id) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("updateFieldsTable", id, city_id, name, phone, light_status, coating_id,
                    shower_status, roof_status, geo_long, geo_lat, address, user_id);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }

    //обновляем запись в таблице "notes" по id
    public void updateNotesTable(String id, String logo, String head, String user_id, String date,
                                 String city_id, String message,
                                 String tsizemessage, String tstylemessage) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("updateNotesTable", id, logo, head, user_id, date, city_id,
                    message, tsizemessage, tstylemessage);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }

    //обновляем один столбец в  определенной записи таблици по id
    public void updateOneColumnTable(String id, String column, String value, String table) {
        try (BackgroundWorker bg = new BackgroundWorker()) {
            bg.execute("updateOneColumnTable", id, column, value, table);

            String resultdb = bg.get();
            JSONObject jResult = new JSONObject(resultdb);
            if (jResult.getString("error").toString().equals("")) {
                //выводим текст с положительным ответом
                Toast.makeText(context, jResult.getString("rez").toString(), Toast.LENGTH_LONG).show();
            } else {
                //выводим текст с отрецательным ответом
                Toast.makeText(context, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }//if-else

        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//updateOneColumnTable

    //------------------------ФУНКЦИИ НЕ ДЛЯ БАЗЫ ДАННЫХ-----------------------------------------

    //фильтр вводимых символов для EditText.addTextChangedListener()
    public void inputFilterForListener(EditText editText, int countSymbol, String rightSymbols ) {

        //формируем фильтр количества вводимых символов
        int maxLength = countSymbol;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);

        //формируем фильтр вводимых символов
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                       int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    String checkMe = String.valueOf(source.charAt(i));
                    Pattern pattern = Pattern.compile( rightSymbols);
                    Matcher matcher = pattern.matcher(checkMe);
                    boolean valid = matcher.matches();
                    if (!valid) {
                        //Log.d("", "Некорректный ввод!");
                        return "";
                    }
                }
                return null;
            }
        };//filter

        //проверка длины вводимой строки и выбор фильтра
        if(editText.length() < countSymbol) {
            editText.setFilters(new InputFilter[]{filter});  //применяем фильтр вводимых символов
        }else {
            editText.setFilters(FilterArray);                //применяем фильтр количества символов
        }//if
    }//inputFilterForListener

    //фильтр вводимых символов для номера телефона
    public void inputFilterForPhoneNumber(EditText editText){
        //применяем регулярное выражения для правельности ввода номера телефона
        final String regex = "\\(\\d{3}\\)\\d{3}\\-\\d{2}\\-\\d{2}";
        editText.setFilters(
                new InputFilter[] { new PartialRegexInputFilter(regex)}
        );
        editText.setText("(");
        editText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        editText.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                //если нажата кнопка
                                if (event.getAction() == KeyEvent.ACTION_UP ||
                                        event.getAction() != KeyEvent.ACTION_DOWN)
                                    //если это кнопка удалить KEYCODE_DEL
                                    if ((keyCode != KeyEvent.KEYCODE_DEL)) {
                                        //вставляем нужные символы взависимости от позиции формата
                                        if (s.length() == 0)
                                            editText.getText().insert(editText.getSelectionStart(), "(");
                                        if (s.length() == 4)
                                            editText.getText().insert(editText.getSelectionStart(), ")");
                                        if (s.length() == 8 || s.length() == 11)
                                            editText.getText().insert(editText.getSelectionStart(), "-");
                                    } else {
                                        //если символ последний, то вставить обратно "("
                                        if (s.length() == 0)
                                            editText.getText().insert(editText.getSelectionStart(), "(");
                                    }
                                return false;
                            }
                        });
                        String value = s.toString();
                        if (value.matches(regex))
                            editText.setTextColor(Color.BLACK);
                        else
                            editText.setTextColor(Color.RED);
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {}
                }
        );
    }//inputFilterForPhoneNumber

}//DBUtilities
