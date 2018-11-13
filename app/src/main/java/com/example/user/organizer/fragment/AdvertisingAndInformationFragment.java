package com.example.user.organizer.fragment;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.organizer.AdvertisingAndInformationRecyclerAdapter;
import com.example.user.organizer.BackgroundWorker;
import com.example.user.organizer.CreateNewsNoteActivity;
import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.NavigationDrawerLogInActivity;
import com.example.user.organizer.Note;
import com.example.user.organizer.R;
import com.example.user.organizer.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AdvertisingAndInformationFragment extends Fragment {

    // коды для идентификации активностей при получении результата
    public final int REQ_CREATE_NOTE = 1001;

    //Параметр -- "ИМЯ КЛЮЧА"
    public static final String PAR_HEAD = "head";
    public static final String PAR_NOTE = "note";
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
    Cursor infoCursor;                // прочитанные данные
    List<String> spListCity;             // Данные для спинера выбора города

    TextView tvAdAnInAc;
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
//        buildUserRecyclerView();

        //получение записией новостей из БД
        List<Note> notes = dbUtilities.getNotesfromDB();
        Log.d("FOOTBALL", notes.toString());

        //обращаемся к базе для получения списка имен городов
        spListCity = dbUtilities.getStringListFromDB("getAllCities", "cities");
//        //обращаемся к базе для получения списка имен городов
//        try(BackgroundWorker bg = new BackgroundWorker()){
//            bg.execute("getAllCities");
//
//            String resultdb = bg.get();
//            Log.d("FOOTBALL", resultdb);
//            JSONObject jResult = new JSONObject(resultdb);
//
//            if(jResult.getString("error").toString().equals("")){
//                JSONObject jCities = jResult.getJSONObject("cities");
//                Iterator<String> i = jCities.keys();
//
//                while(i.hasNext()){
//                    spListCity.add(jCities.getString(i.next()));
//                }//while
//
//                Log.d("FOOTBALL", spListCity.toString());
//            }else{
//                Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
//            }//if-else
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }//try-catch


//        //запрос для получения курсор с данными
//        String query = "SELECT name FROM cities;";

//        //заполнить spListCity данные для отображения в Spinner
//        spListCity = dbUtilities.fillListStr(query);
        spListCity.add("ВСЕ ГОРОДА");

//        spCityAdAnInAc.setAdapter(buildSpinner(spListCity));
        spCityAdAnInAc.setSelection(spListCity.size()-1);

        //Слушатель для позиции спинера и фильтрации RecyclerView по изменению позиции
//        spCityAdAnInAc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String query;
//                spPos = position;
//                //если выбран элемент "ВСЕ ГОРОДА"
//                if (spCityAdAnInAc.getItemAtPosition(position).equals("ВСЕ ГОРОДА")){
//                    // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
//                    query = "SELECT head, date, cities.name FROM infonotes INNER JOIN cities ON cities._id = infonotes.city_id;";
//                    //инициализация коллекции для спинера
//                    List<String> notes =  new ArrayList<>();
//
//                    //обращаемся к базе для получения списка имен городов
//                    notes = dbUtilities.getStringListFromDB("getAllNotes", "notes");
//
//
//
//                }else {
//                    // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
//                    query = "SELECT head, date, cities.name FROM infonotes " +
//                            "INNER JOIN cities ON cities._id = infonotes.city_id WHERE cities.name = \"" +
//                            spCityAdAnInAc.getItemAtPosition(position) + "\";";
//                }//if-else

//                buildUserRecyclerView(query);     //Строим RecyclerView

//            }//onItemSelected
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }//onNothingSelected
//        });

//         получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
//       String query = "SELECT head, date, cities.name FROM infonotes " +
//                "INNER JOIN cities ON cities._id = infonotes.city_id;";
//
//        buildUserRecyclerView(query);     //Строим RecyclerView
//
//        tvAdAnInAc = result.findViewById(R.id.tvAdAnInAc);
//        tvAdAnInAc.setText(String.valueOf(infoCursor.getCount()));

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
            String note = data.getStringExtra(PAR_NOTE);
            String date = data.getStringExtra(PAR_DATE);
            int city = data.getIntExtra(PAR_CITY, 0);

            writeDataToBD(head, note, date, city);
        }//RESULT_OK

        spCityAdAnInAc.setSelection(spListCity.size()-1);

        //обновим список после обновления
        // получаем данные из БД в виде курсора (коллекция, возвращенная запросом)
        String query = "SELECT head, date, cities.name FROM infonotes " +
                "INNER JOIN cities ON cities._id = infonotes.city_id;";

//        buildUserRecyclerView(query);     //Строим RecyclerView
    }//onActivityResult

    private void writeDataToBD(String head, String note, String date, int city) {

        ContentValues cv = new ContentValues();
        cv.put("head", head);
        cv.put("note", note);
        cv.put("date", date);
        cv.put("city_id", city);

        //добваить данные через объект ContentValues(cv), в таблицу "infonotes"
        dbUtilities.insertInto(cv, "infonotes");
    }//writeDataToBD


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
    private void buildUserRecyclerView() {

//        //обращаемся к базе для получения списка имен городов
//        List<String> list1 = dbUtilities.getStringListFromDB("getAllNotes", "notes");
//        List<Note> notes = new ArrayList<>();
//        int n = list1.size();
//        int i = 0;
//        while(i < n) {
//           notes.add(new Note(list1.get(i), list1.get(i+1), list1.get(i+2), list1.get(i+3)));
//           i = i+4;
//        }//for

//        Log.d("FOOTBALL", notes.toString());

        // создаем адаптер, передаем в него курсор
        advertisingAndInformationRecyclerAdapter
                = new AdvertisingAndInformationRecyclerAdapter(context, infoCursor);

        // RecycerView для отображения таблицы users БД
        rvNoteLineAdAnInAc.setAdapter(advertisingAndInformationRecyclerAdapter);
    }//buildUserRecyclerView

}//AdvertisingAndInformationFragment
