package com.example.user.organizer.inteface;

import android.view.View;

//------------------------Интерфейс для NavigationDrawer-------------------
public interface NavigationDrawerInterface {
    //закрыть приложение
    void closeApp();
    //выйти из аккаунта
    void signOut();
    //инфо об аккаунта
    void aboutUserInfoDialog();
}//NavigationDrawerInterface

