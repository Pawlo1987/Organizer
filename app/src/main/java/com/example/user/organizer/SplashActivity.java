package com.example.user.organizer;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//----------------Активность для анимации при включении приложения-------------
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //для подключения совместимости gif использованна библиотека по ссылке
        //https://github.com/koral--/android-gif-drawable
        //https://www.youtube.com/watch?v=EOFY0cwNjuk
        //для анимации слов использован генератор по ссылке
        //http://gfto.ru/index/animacija_raskalyvajushhijsja_tekst/0-72

        //Создаем таймер обратного отсчета на 10 секунд с шагом отсчета
        //в 1 секунду (задаем значения в миллисекундах):
        new CountDownTimer(10000, 1000) {

            //Здесь можно выполнить какието дейстивия через кажду секунду
            //до конца счета таймера
            public void onTick(long millisUntilFinished) { }
            //Задаем действия после завершения отсчета (запускаем главную активность)
            public void onFinish(){ startMainActivity(); }
        }.start();

    }//onCreate

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        //флаг отчистки стека активностей
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }//startMainActivity
}//SplashActivity