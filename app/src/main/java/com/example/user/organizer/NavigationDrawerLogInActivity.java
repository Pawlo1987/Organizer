package com.example.user.organizer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.organizer.fragment.AboutUserInfoDialog;
import com.example.user.organizer.fragment.AdvertisingAndInformationFragment;
import com.example.user.organizer.fragment.ExitConfirmDialog;
import com.example.user.organizer.fragment.SettingsFragment;
import com.example.user.organizer.fragment.ShowAllEventsFragment;
import com.example.user.organizer.fragment.ShowAuthUserEventsFragment;
import com.example.user.organizer.fragment.ShowFieldCatalogFragment;
import com.example.user.organizer.inteface.NavigationDrawerInterface;

//--------Боковое меню и управление им( посути основная активность)
public class NavigationDrawerLogInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationDrawerInterface {

    DBUtilities dbUtilities;
    User user;
    String idAuthUser;                  //id Авторизированого пользователя
    String userInfo;

    //параметр для вызова диалога "aboutUserInfoDialog"
    final String ID_ABOUT_USER_INFO_DIALOG = "aboutUserInfoDialog";
    final String ID_EXIT_DIALOG = "dialogExitConfirm";          //параметр для вызова диалога "выход"
    private ExitConfirmDialog exitConfirmDialog; // диалог подтверждения выхода из приложения
    ShowAllEventsFragment showAllEventsFragment;
    ShowFieldCatalogFragment showFieldCatalogFragment;
    ShowAuthUserEventsFragment showAuthUserEventsFragment;
    AdvertisingAndInformationFragment advertisingAndInformationFragment;
    SettingsFragment settingsFragment;
    FragmentTransaction fTrans;

    AboutUserInfoDialog aboutUserInfoDialog =
            new AboutUserInfoDialog(); // диалог информация о пользователе

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
        settingsFragment = new SettingsFragment();

        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        //передаем данные об  авторизированном пользователе
        args.putString("idAuthUser", idAuthUser);
        showAuthUserEventsFragment.setArguments(args);
        showAllEventsFragment.setArguments(args);
        advertisingAndInformationFragment.setArguments(args);
        settingsFragment.setArguments(args);

        // + фрагмент showFieldCatalogFragment считыват данные с сервера
        args.putBoolean("connection", true);
        showFieldCatalogFragment.setArguments(args);

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
//          Ссылка на вариант как можно достучатся до logo в юоковом меню
//        http://android-help1.blogspot.com/2016/03/how-to-set-navigation-drawer-header.html
        // Наполняем шапку бокового меню элементами
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_navigation_drawer_log_in);
        TextView tvProfileName = headerLayout.findViewById(R.id.tvProfileName);
        TextView tvProfileRating = headerLayout.findViewById(R.id.tvProfileRating);
        ImageView ivProfileLogo = headerLayout.findViewById(R.id.ivProfileLogo);

        //прочитать данные по авторизированому пользователю
        User user = dbUtilities.getListUser(idAuthUser).get(0);
        //логотип пользователя отобразить в боковом меню
        ivProfileLogo.setImageResource(0);

        // Показать картинку
        new DownloadImageTask( headerLayout.findViewById(R.id.ivProfileLogo))
                .execute("http://strahovanie.dn.ua/football_db/logo/logo_" + user.getLogo() + ".png");

        //нажатие на logo
        ivProfileLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                aboutUserInfoDialog();
                return false;
            }
        });

        //имя пользователя отобразить в боковом меню
        tvProfileName.setText(dbUtilities.searchValueInColumn(
                "users",
                "id",
                "name",
                idAuthUser)
        );

        //рейтинг пользователя отобразить в боковом меню
        tvProfileRating.setText(dbUtilities.searchValueInColumn(
                "users",
                "id",
                "login",
                idAuthUser)
        );

        navigationView.setNavigationItemSelectedListener(this);

        //Запуск сервиса следящего за обновления информации в БД
        startServiceWatchingForDb();
    }//onCreate

    private void startServiceWatchingForDb() {
        Intent intent = new Intent(this, NotificationService.class);
        intent.putExtra("idAuthUser", idAuthUser);
        startService(intent);
    }//startServiceWatchingForDb

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
        Log.d("MyLog", String.format("%s",item));
        fTrans = getFragmentManager().beginTransaction();

        if (id == R.id.show_field_catalog) {
            fTrans.replace(R.id.container, showFieldCatalogFragment);
        } else if (id == R.id.show_user_events) {
            fTrans.replace(R.id.container, showAuthUserEventsFragment);
        } else if (id == R.id.show_all_events) {
            fTrans.replace(R.id.container, showAllEventsFragment);
        } else if (id == R.id.news_line) {
            fTrans.replace(R.id.container, advertisingAndInformationFragment);
        } else if (id == R.id.settings) {
            fTrans.replace(R.id.container, settingsFragment);
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

    //строка с полной информацией о пользователе
    private String fullInfoAboutUser() {
        User user = dbUtilities.getListUser(idAuthUser).get(0);
        return String.format("Логин: %s\n" +
                        "Пароль: %s \n" +
                        "Имя: %s\n" +
                        "Телефон: %s\n" +
                        "Город: %s\n" +
                        "Имейл: %s",
                user.getLogin(),
                user.getPassword(),
                user.getName(),
                user.getPhone(),
                dbUtilities.searchValueInColumn(
                        "cities", "id", "name", user.getCity_id()
                ),
                user.getEmail());
    }//fullInfoAboutEvent

    @Override
    public void signOut() {
        moveTaskToBack(true);
        finish();
        System.runFinalizersOnExit(true);
        Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivity(intent);
    }//signOut

    //вызов диалога
    @Override
    public void aboutUserInfoDialog() {
        userInfo = fullInfoAboutUser();
        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("message", userInfo);
        aboutUserInfoDialog.setArguments(args);

        // Точка вызова отображение диалогового окна
        aboutUserInfoDialog.show(getSupportFragmentManager(), ID_ABOUT_USER_INFO_DIALOG);
    }//aboutUserInfoDialog

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, NotificationService.class));
        super.onDestroy();
    }//onDestroy

}//NavigationDrawerLogInActivity
