package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

//--------Активность для авторизация пользователя или перехода в актиность создания нового акаунта-

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class AuthorizationActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
    Cursor authorCursor;                // курсор для чтения данных из БД
    Context context;
    String result;                      // ответ сервера

    //Коллекции храннящие сущществующие логины и имейлы для авторизации
    ArrayMap<Integer, String> loginMap = new ArrayMap<>();
    //Коллекции храннящие сущществующие пароли к логинам и имейлам
    ArrayMap<Integer, String> passwordMap = new ArrayMap<>();

    EditText etLogin;         //поле ввода для логина или email
    EditText etPassword;      //поле ввода для пароля

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        context = getBaseContext();
        dbUtilities = new DBUtilities(context);
//        dbUtilities.open();
//        // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
//        String query = "SELECT users.login, users.email, users.password FROM users;";
//        authorCursor =  dbUtilities.getDb().rawQuery(query, null);

//        //заполнить коллекции для поиска сущестующего логина или email для авторизации
//        fillMap();

    }//onCreate

//    // заполняем коллекции логинов и email-ов
//    private void fillMap() {
//        int n = authorCursor.getCount();
//
//        //значение ключа коллекции
//        int j = 0;
//        //цикл для заполнения коллекций существующих логинов(email) и их паролей
//        for (int i = 0; i < n; i++, j = j + 2) {
//            authorCursor.moveToPosition(i); // переходим в курсоре на текущую позицию
//            loginMap.put(j,authorCursor.getString(0));      //логин пользователя
//            loginMap.put(j+1,authorCursor.getString(1));    //email пользователя
//            passwordMap.put(i,authorCursor.getString(2));   //пароль пользователя
//        }//fori
//    }//fillMap

    // Обработка нажатия кнопки авторизации
    public void signIn() {
        int numberLogin;        //_id записи логина в таблице БД
        String login = etLogin.getText().toString();          //введенное значение в поле логин
        String password = etPassword.getText().toString();    //введенное значение в поле пароль

        try(BackgroundWorker bg = new BackgroundWorker()){
            bg.execute("authorization", login, password);

            result = bg.get();
            JSONObject jResult = new JSONObject(result);
            if(jResult.getString("error").toString().equals("")){
                User user = new User();
                user.id = jResult.getJSONObject("user").getString("id");
                user.name = jResult.getJSONObject("user").getString("name");

                Intent intent = new Intent(this, NavigationDrawerLogInActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }else{
                Toast.makeText(this, jResult.getString("error").toString(), Toast.LENGTH_LONG).show();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        /*boolean checkLogin = false, checkPassword = false;    //флаги проверки пароля и логина

        //если коллекция loginMap содержит введеное значение
        if(loginMap.containsValue(login)){
            checkLogin = true;              //флаг проверки логина
            int n = loginMap.size();        //длина коллекции
            for (int i = 0; i < n ; i++) {  //цикл для поиска ключа совпавшего логина
                if(loginMap.valueAt(i).equals(login)) {
                    //numberLogin это _id пользователя в БД
                    //делим на 2 для корректности, если мы нашли email
                    //так записана коллекция loginMap
                    numberLogin = i/2;
                    if(passwordMap.valueAt(numberLogin).equals(password))
                        checkPassword = true; //флаг проверки пароля
                    break;
                }//if
            }//for
        }//if*/

        //проверка существующего Логина или почти и совпадающего пароля
        /*if(checkLogin && checkPassword){
            //переходин в актиность LoginActivity
            Intent intent = new Intent(this, NavigationDrawerLogInActivity.class);
            startActivity(intent);
        } //if-else*/

    }//signIn

    //обработка нажатия кнопки создать новый аккаунт
    public void createAccount() {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }//createAccount

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:            //кнопка авторизации
                signIn();
                break;
            case R.id.btnSignUp:               //кнопка создакния нового аккаунта
                createAccount();
                break;
        }//switch
    }//onClick
}//AuthorizationActivity
