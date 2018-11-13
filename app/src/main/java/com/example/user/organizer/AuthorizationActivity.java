package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//--------Активность для авторизация пользователя или перехода в актиность создания нового акаунта-

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class AuthorizationActivity extends AppCompatActivity {

    DBUtilities dbUtilities;
    Context context;
    ActionBar actionBar;                //стрелка НАЗАД
    String idAuthUser;                 //авторизированный пользователь

    EditText etLogin;         //поле ввода для логина или email
    EditText etPassword;      //поле ввода для пароля
    TextView tvSymCountPas; //кол-во символов для пароля
    String temp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        context = getBaseContext();
        dbUtilities = new DBUtilities(context);

        //добавляем actionBar (стрелка сверху слева)
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvSymCountPas = (TextView) findViewById(R.id.tvSymCountPas);
        tvSymCountPas.setText(String.valueOf(10));
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                dbUtilities.inputFilterForListener(etPassword,
                        10,
                        MainActivity.FILTER_STR
                );
                tvSymCountPas.setText(String.valueOf(10 - s.length()));
            }//afterTextChanged
        });
    }//onCreate

    //обработчик actionBar (стрелка сверху слева)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }//switch
    }//onOptionsItemSelected

    // Обработка нажатия кнопки авторизации
    public void signIn() {
        //проверка пустых полей
        if (etLogin.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Есть пустые поля!", Toast.LENGTH_SHORT).show();
        } else {
            String login = etLogin.getText().toString();          //введенное значение в поле логин
            String password = etPassword.getText().toString();    //введенное значение в поле пароль
            idAuthUser = dbUtilities.getAuthUserParam(login, password);
            if (idAuthUser != null) {
                //прервать поток Service
                //на всякий случай продублировал
                stopService(new Intent(this, NotificationService.class));
                //процедура запуска сервиса отслеживания изменения БД
                //для получения сообщений
                Intent intent = new Intent(this, NotificationService.class);
                intent.putExtra("idAuthUser", idAuthUser);
                startService(intent);

                intent = new Intent(context, NavigationDrawerLogInActivity.class);
                intent.putExtra("idAuthUser", idAuthUser);
                intent.putExtra("notificationServiceFlag", false);
                startActivity(intent);
                finish();
            }//if
        }
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
