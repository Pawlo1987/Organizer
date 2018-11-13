package com.example.user.organizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.user.organizer.fragment.AdvertisingAndInformationFragment;
import com.example.user.organizer.fragment.SelectLogoDialog;
import com.example.user.organizer.inteface.SelectLogoInterface;

//---------------Активность для выбора логотипа--------------------------
public class SelectLogoActivity extends AppCompatActivity implements SelectLogoInterface {
    ActionBar actionBar;                //стрелка НАЗАД
    DBUtilities dbUtilities;
    Context context;
    String table;       //параметр указывающий в какой таблице менять логотип
    TextView tvBufferSeLoAc;

    RecyclerView rvUserSeLoAc;      //RecyclerView для пользователей
    String idAuthUser;                 //авторизированный пользователь

    // адаптер для отображения recyclerView
    SelectLogoRecyclerAdapter selectLogoRecyclerAdapter;

    //параметр для вызова диалога "selectLogo"
    final String ID_SELECT_LOGO_DIALOG = "callDialogSelectLogo";

    SelectLogoDialog selectLogoDialog =
            new SelectLogoDialog(); // диалог подтверждения выбора логотипа

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_logo);
        tvBufferSeLoAc  = findViewById(R.id.tvBufferSeLoAc);
        tvBufferSeLoAc.setVisibility(View.GONE);

        //добавляем actionBar (стрелка сверху слева)
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);

        // прочитать данные, переданные из активности (из точки вызова)
        idAuthUser = getIntent().getStringExtra("idAuthUser");
        table = getIntent().getStringExtra("table");

        // создаем адаптер, передаем в него курсор
        selectLogoRecyclerAdapter
                = new SelectLogoRecyclerAdapter(this, context, idAuthUser, table);

        // RecycerView для отображения таблицы users БД
        rvUserSeLoAc = (RecyclerView) findViewById(R.id.rvUserSeLoAc);

        rvUserSeLoAc.setAdapter(selectLogoRecyclerAdapter);
        tvBufferSeLoAc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Intent intent = new Intent();
                intent.putExtra(CreateNewsNoteActivity.PAR_LOGO, s.toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }//onCreate

    //обработчик actionBar (стрелка сверху слева)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
//                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }//switch
    }//onOptionsItemSelected

    @Override
    public void callDialogSelectLogo(Context context, String logo, String userId, String table) {
        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("logo", logo);
        args.putString("user_id", idAuthUser);
        args.putString("table", this.table);

        selectLogoDialog.setArguments(args);

        // Точка вызова отображение диалогового окна
        selectLogoDialog.show( getSupportFragmentManager(), ID_SELECT_LOGO_DIALOG);

    }//callDialogSelectLogo

}//SelectLogoActivity
