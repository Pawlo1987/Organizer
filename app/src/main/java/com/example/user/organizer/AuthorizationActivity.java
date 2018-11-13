package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

//--------Активность для авторизация пользователя или перехода в актиность создания нового акаунта-

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class AuthorizationActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
    Context context;

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
    }//onCreate

    // Обработка нажатия кнопки авторизации
    public void signIn() {
        String login = etLogin.getText().toString();          //введенное значение в поле логин
        String password = etPassword.getText().toString();    //введенное значение в поле пароль

        User user = dbUtilities.getUserParam(login, password);

        if(user.id != null){
            Intent intent = new Intent(context, NavigationDrawerLogInActivity.class);
            intent.putExtra("idAuthUser", user.id);
            startActivity(intent);
            finish();
        }//if

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
