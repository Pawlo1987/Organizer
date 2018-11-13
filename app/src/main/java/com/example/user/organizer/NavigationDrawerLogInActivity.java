package com.example.user.organizer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.user.organizer.fragment.AdvertisingAndInformationFragment;
import com.example.user.organizer.fragment.ExitConfirmDialog;
import com.example.user.organizer.fragment.ShowAllEventsFragment;
import com.example.user.organizer.fragment.ShowAuthUserEventsFragment;
import com.example.user.organizer.fragment.ShowFieldCatalogFragment;
import com.example.user.organizer.inteface.CustomInterface;

//--------Боковое меню и управление им( посути основная активность)
public class NavigationDrawerLogInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CustomInterface{

    DBUtilities dbUtilities;
    User user;
    String idAuthUser;                  //id Авторизированого пользователя

    final String ID_EXIT_DIALOG = "dialogExitConfirm";          //пааметр для вызова диалога "выход"
    private ExitConfirmDialog exitConfirmDialog; // диалог подтверждения выхода из приложения

    ShowAllEventsFragment showAllEventsFragment;
    ShowFieldCatalogFragment showFieldCatalogFragment;
    ShowAuthUserEventsFragment showAuthUserEventsFragment;
    AdvertisingAndInformationFragment advertisingAndInformationFragment;
    FragmentTransaction fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_log_in);
        idAuthUser = getIntent().getStringExtra("idAuthUser");

        dbUtilities = new DBUtilities(getBaseContext());

        exitConfirmDialog = new ExitConfirmDialog();
        showAllEventsFragment = new ShowAllEventsFragment();
        showFieldCatalogFragment = new ShowFieldCatalogFragment();
        showAuthUserEventsFragment = new ShowAuthUserEventsFragment();
        advertisingAndInformationFragment = new AdvertisingAndInformationFragment();

        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("idAuthUser", idAuthUser);     //завершить работу приложения
        showAuthUserEventsFragment.setArguments(args);
        showAllEventsFragment.setArguments(args);
        showFieldCatalogFragment.setArguments(args);
        advertisingAndInformationFragment.setArguments(args);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getBaseContext(), CreateEventActivity.class);
                intent.putExtra("idAuthUser",idAuthUser);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        fTrans = getFragmentManager().beginTransaction();

        if (id == R.id.show_field_catalog) {
            fTrans.replace(R.id.container, showFieldCatalogFragment);
        } else if (id == R.id.show_user_events) {
            fTrans.replace(R.id.container, showAuthUserEventsFragment);
        } else if (id == R.id.show_all_events) {
            fTrans.replace(R.id.container, showAllEventsFragment);
        } else if (id == R.id.news_line) {
            fTrans.replace(R.id.container, advertisingAndInformationFragment);
        } else if (id == R.id.log_out) {
            Bundle args1 = new Bundle();    // объект для передачи параметров в диалог
            args1.putBoolean("flag", true);      //выйти из аккаунта
            exitConfirmDialog.setArguments(args1);
            // отображение диалогового окна
            exitConfirmDialog.show(getSupportFragmentManager(), ID_EXIT_DIALOG);
        } else if (id == R.id.exit_app) {
            Bundle args1 = new Bundle();    // объект для передачи параметров в диалог
            args1.putBoolean("flag", false);     //завершить работу приложения
            exitConfirmDialog.setArguments(args1);
            // отображение диалогового окна
            exitConfirmDialog.show(getSupportFragmentManager(), ID_EXIT_DIALOG);
        }
        fTrans.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Завершить работу приложение
    public void closeApp() {
        moveTaskToBack(true);
        finish();
        System.runFinalizersOnExit(true);
        System.exit(0);
    }//closeApp

    @Override
    public void signOut() {
        moveTaskToBack(true);
        finish();
        System.runFinalizersOnExit(true);
        Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivity(intent);
    }//signOut

}//NavigationDrawerLogInActivity
