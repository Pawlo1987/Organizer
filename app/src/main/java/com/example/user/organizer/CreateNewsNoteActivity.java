package com.example.user.organizer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.organizer.fragment.AdvertisingAndInformationFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


//-------Активность для создания новой записи в новостной и рекламной ленте-----------------

public class CreateNewsNoteActivity extends AppCompatActivity {
    // коды для идентификации активностей при получении результата
    public final int REQ_SELECT_LOGO = 1001;

    //Параметр -- "ИМЯ КЛЮЧА"
    public static final String PAR_LOGO = "logo";

    DBUtilities dbUtilities;
    Spinner spCityCrNeNoAc;
    Spinner spSize;
    Spinner spStyle;

    String logo = "1";
    Calendar calendar = Calendar.getInstance();      // объект для работы с датой и временем
    String date;                                 // назначения дата записи
    ActionBar actionBar;                //стрелка НАЗАД

    TextView tvDateCrNeNoAc;            // TextView для вывода даты
    EditText etHeadCrNeNoAc;            // EditText для заголовока
    EditText etMessCrNeNoAc;            // EditText для новой записи

    Context context;
    String idAuthUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news_note);

        new DownloadImageTask( findViewById(R.id.ivLogoCrNeNoAc))
                .execute("http://strahovanie.dn.ua/football_db/logo/logo_" + logo + ".png");

        //добавляем actionBar (стрелка сверху слева)
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        idAuthUser = getIntent().getStringExtra("idAuthUser");

        context = getBaseContext();
        dbUtilities = new DBUtilities(context);

        tvDateCrNeNoAc = (TextView) findViewById(R.id.tvDateCrNeNoAc);
        etHeadCrNeNoAc = (EditText) findViewById(R.id.etHeadCrNeNoAc);
        etMessCrNeNoAc = (EditText) findViewById(R.id.etMessCrNeNoAc);
        spCityCrNeNoAc = (Spinner) findViewById(R.id.spCityCrNeNoAc);
        spSize = (Spinner) findViewById(R.id.spSize);
        spStyle = (Spinner) findViewById(R.id.spStyle);
        etMessCrNeNoAc.setTextSize(14);
        etMessCrNeNoAc.setTypeface(Typeface.DEFAULT);

        setInitialDate();           //формирование даты для записи

        buildCitySpinner();     //строим Spinner City

        //слушатель для спинера выбора размера шрифта
        spSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: etMessCrNeNoAc.setTextSize(14); break;
                    case 1: etMessCrNeNoAc.setTextSize(16); break;
                    case 2: etMessCrNeNoAc.setTextSize(18); break;
                    case 3: etMessCrNeNoAc.setTextSize(20); break;
                }//switch
            }//onItemSelected
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }//onNothingSelected
        });

        //слушатель для спинера выбора типа шрифта
        spStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: etMessCrNeNoAc.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL)); break;
                    case 1: etMessCrNeNoAc.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD)); break;
                    case 2: etMessCrNeNoAc.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC)); break;
                    case 3: etMessCrNeNoAc.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC)); break;
                }//switch
            }//onItemSelected
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }//onNothingSelected
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

    //строим Spinner City
    private void buildCitySpinner() {
        //строим спинер для городов
        List<String> spListCity = new ArrayList<>();    // Данные для спинера выбора города
        //заполнить spListCity данные для отображения в Spinner
        //обращаемся к базе для получения списка имен городов
        spListCity = dbUtilities.getStrListTableFromDB("cities", "name");
        ArrayAdapter<String> spinnerAdapter;
        //создание адаптера для спинера
        spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListCity
        );
        // назначение адапетра для списка
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCityCrNeNoAc.setAdapter(spinnerAdapter);


        //строим спинер для размера шрифта
        spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>( Arrays.asList(new String[]{"14", "16", "18", "20"}))
        );
        // назначение адапетра для списка
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSize.setAdapter(spinnerAdapter);

        //строим спинер для типа шрифта
        spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>( Arrays.asList(new String[]{"Н", "Ж", "К", "ЖК"}))
        );
        // назначение адапетра для списка
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spStyle.setAdapter(spinnerAdapter);
    }//buildCitySpinner

    //-----------------------Метод для приема результатов из активностей----------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
        }//RESULT_CANCELED

        // обработка результатов по активностям
        if (resultCode == RESULT_OK) {
            logo = data.getStringExtra(PAR_LOGO);
            // Показать картинку
            new DownloadImageTask( findViewById(R.id.ivLogoCrNeNoAc))
                    .execute("http://strahovanie.dn.ua/football_db/logo/logo_" + logo + ".png");
        }//RESULT_OK
    }//onActivityResult

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSelectLogoNoNeNoAc:         //кнопка выбора логотипа
                Intent intent = new Intent(context, SelectLogoActivity.class);
                intent.putExtra("idAuthUser", idAuthUser);
                intent.putExtra("table", "notes");    //для указания в какой таблице менять логотип
                startActivityForResult(intent, REQ_SELECT_LOGO);
                break;
            case R.id.btnConfirmCrNeNoAc:            //кнопка подтвердить выбор
                confirmSelect();
                break;
            case R.id.btnCancelCrNeNoAc:             //кнопка отменить выбор
                cancelSelect();
                break;
        }//switch
    }//onClick

    //возврат к предедыщему меню
    private void cancelSelect() {
        setResult(RESULT_CANCELED);
        finish();
    }//cancelSelect

    //сохранить и передать выборанных игроков
    private void confirmSelect() {
        if (etHeadCrNeNoAc.getText().toString().equals("") || etMessCrNeNoAc.getText().toString().equals("")) {
            Toast.makeText(this, "Есть пустые поля!", Toast.LENGTH_SHORT).show();
        } else {
            // создать для передачи результатов
            Intent intent = new Intent();
            //пакуем при помощи Extra - объект
            intent.putExtra(AdvertisingAndInformationFragment.PAR_LOGO, logo);
            intent.putExtra(AdvertisingAndInformationFragment.PAR_HEAD, etHeadCrNeNoAc.getText().toString());
            intent.putExtra(AdvertisingAndInformationFragment.PAR_MESSAGE, etMessCrNeNoAc.getText().toString());
            intent.putExtra(AdvertisingAndInformationFragment.PAR_DATE, date);
            intent.putExtra(AdvertisingAndInformationFragment.PAR_CITY,
                    dbUtilities.getIdByValue(
                            "cities",              //название таблицы
                            "name",               //название столбца
                            spCityCrNeNoAc.getSelectedItem().toString() //значение для поиска
                    )
            );
            intent.putExtra(AdvertisingAndInformationFragment.PAR_TSIZE_MESSAGE,
                    String.valueOf(spSize.getSelectedItemPosition()));
            intent.putExtra(AdvertisingAndInformationFragment.PAR_TSTYLE_MESSAGE,
                    String.valueOf(spStyle.getSelectedItemPosition()));

            // собственно передача параметров
            setResult(RESULT_OK, intent);
            finish();
        }
    }//confirmSelect

    // установка даты записи
    private void setInitialDate() {
        //задаем дату в нужном формате для БД
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(calendar.getTimeInMillis());

        //установка текста в TextView
        tvDateCrNeNoAc.setText(date);
    } // setInitialDateTim

}//CreateNewsNoteActivity
