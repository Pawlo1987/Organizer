package com.example.user.organizer.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.user.organizer.AdvertisingAndInformationRecyclerAdapter;
import com.example.user.organizer.CreateNewsNoteActivity;
import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.Note;
import com.example.user.organizer.R;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AdvertisingAndInformationFragment extends Fragment {

    // коды для идентификации активностей при получении результата
    public final int REQ_CREATE_NOTE = 1001;

    //Параметр -- "ИМЯ КЛЮЧА"
    public static final String PAR_HEAD = "head";
    public static final String PAR_MESS = "message";
    public static final String PAR_CITY = "city";
    public static final String PAR_DATE = "date";

    RecyclerView rvNoteLineAdAnInAc;

    // адаптер для отображения recyclerView
    AdvertisingAndInformationRecyclerAdapter advertisingAndInformationRecyclerAdapter;
    DBUtilities dbUtilities;
    Spinner spCityAdAnInAc;
    int spPos;                      //позиция спинера

    Button btnNewNoteAdAnInAc;      // кнопка создания новости

    // поля для доступа к записям БД
    List<String> spListCity;             // Данные для спинера выбора города

    Context context;
    int idAuthUser = 6;

    public AdvertisingAndInformationFragment newInstance() {

        AdvertisingAndInformationFragment fragment = new AdvertisingAndInformationFragment();

        return fragment;
    } // FirstPageFragment

    // Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь
    // мы можем получить контекст фрагмента, в качестве которого выступает класс MainActivity.
    @Override
    public void onAttach(Context context) {
        this.context = context;
        dbUtilities = new DBUtilities(context);
       // dbUtilities.open();

        super.onAttach(context);
    } // onAttach

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_advertising_and_information,  container, false);

        btnNewNoteAdAnInAc = result.findViewById(R.id.btnNewNoteAdAnInAc);

        // RecycerView для отображения таблицы users БД
        rvNoteLineAdAnInAc = result.findViewById(R.id.rvNoteLineAdAnInAc);

        //Обработка нажатия клавишы "Создать новость"
        btnNewNoteAdAnInAc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, CreateNewsNoteActivity.class);
                startActivityForResult(intent, REQ_CREATE_NOTE);
            }
        });

        //привязка ресурсов к объектам
        spCityAdAnInAc = result.findViewById(R.id.spCityAdAnInAc);

        //инициализация коллекции для спинера
        spListCity = new ArrayList<>();

        //обращаемся к базе для получения списка имен городов
        spListCity = dbUtilities.getStrListTableFromDB("cities", "name");

        //добавляем вариант "Все города"
        spListCity.add("ВСЕ ГОРОДА");

        spCityAdAnInAc.setAdapter(buildSpinner(spListCity));
        spCityAdAnInAc.setSelection(spListCity.size()-1);

        //Слушатель для позиции спинера и фильтрации RecyclerView по изменению позиции
        spCityAdAnInAc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spPos = position;
                //строим новый адаптер RecyclerView
                buildUserRecyclerView(
                        spCityAdAnInAc.getItemAtPosition(spPos).toString()
                );

            }//onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }//onNothingSelected
        });

        //строим новый адаптер RecyclerView
        buildUserRecyclerView(
                spCityAdAnInAc.getItemAtPosition(spPos).toString()
        );
        return result;
    } // onCreateView

    //-----------------------Метод для приема результатов из активностей----------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
//            Toast.makeText(this, "Ошибка ввода!!!", Toast.LENGTH_LONG).show();
        }//RESULT_CANCELED

        // обработка результатов по активностям
        if (resultCode == RESULT_OK) {
            // читаем из объекта data полученные данные и выводим в поле результата

            String head = data.getStringExtra(PAR_HEAD);
            String message = data.getStringExtra(PAR_MESS);
            String date = data.getStringExtra(PAR_DATE);
            String city_id = data.getStringExtra(PAR_CITY);

            //обращяемся к БД на сервер для создания новой записи в таблицу notes
            dbUtilities.insertIntoNotes(head, message, date, city_id);
        }//RESULT_OK

        //устанавливаем спинер в позицию "ВСЕ ГОРОДА"
        spCityAdAnInAc.setSelection(spListCity.size()-1);

        //строим новый адаптер RecyclerView
        buildUserRecyclerView(
                spCityAdAnInAc.getItemAtPosition(spPos).toString()
        );

    }//onActivityResult

    //строим Spinner
    private ArrayAdapter buildSpinner(List<String> list) {

        ArrayAdapter<String> spinnerAdapter;

        //создание адаптера для спинера
        spinnerAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item,
                list
        );

        // назначение адапетра для списка
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return spinnerAdapter;
    }//buildCitySpinner

    //Строим RecyclerView
    private void buildUserRecyclerView(String cityName) {
        List<Note> notes = null;    //список новостей

        //получение записией новостей из БД
        if(cityName.equals("ВСЕ ГОРОДА")) {
            notes = dbUtilities.getAllNotesfromDB();
        }else{
            //получаем записи новостей из БД по определенному городу
            notes = dbUtilities.getSomeNotesfromDB(cityName);
        }//if-else

        Log.d("FOOTBALL", notes.toString());

        // создаем адаптер, передаем в него курсор
        advertisingAndInformationRecyclerAdapter
                = new AdvertisingAndInformationRecyclerAdapter(context, notes);

        // RecycerView для отображения таблицы users БД
        rvNoteLineAdAnInAc.setAdapter(advertisingAndInformationRecyclerAdapter);
    }//buildUserRecyclerView

}//AdvertisingAndInformationFragment
