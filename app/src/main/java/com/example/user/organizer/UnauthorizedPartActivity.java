package com.example.user.organizer;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.user.organizer.AuthorizationActivity;
import com.example.user.organizer.R;
import com.example.user.organizer.activity.ShowAllEventActivity;

////--Активность с инициализацией активных клавиш Tab Tools до авторизации-----

public class UnauthorizedPartActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unauthorized_part);

        // получаем TabHost
        TabHost tabHost = getTabHost();

        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Вкладка 1");
        tabSpec.setContent(new Intent(this, AuthorizationActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Вкладка 2");
        tabSpec.setContent(new Intent(this, ShowAllEventActivity.class));
        tabHost.addTab(tabSpec);
    }
}
